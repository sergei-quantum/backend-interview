package org.deblock.exercise.application.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import org.deblock.exercise.application.dto.FlightSearchQueryDto
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.util.stream.Stream
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post as mvcPost

@SpringBootTest
@AutoConfigureWireMock(port = 0)
@AutoConfigureMockMvc
class FlightsApiControllerIntegrationTest {
    @Autowired
    private lateinit var flightsApiController: FlightsApiController

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        @RegisterExtension
        private val crazyAirServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build()

        @RegisterExtension
        private val toughJetServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build()

        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            registry.add("suppliers.crazy-air.base-url") { "http://localhost:${crazyAirServer.port}" }
            registry.add("suppliers.tough-jet.base-url") { "http://localhost:${toughJetServer.port}" }
        }

        @JvmStatic
        fun errorQueries(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    FlightSearchQueryDto(
                        origin = "AMSD",
                        destination = "MLN",
                        departureDate = LocalDate.now(),
                        returnDate = LocalDate.now().plusDays(1),
                        numberOfPassengers = 2
                    ),
                    "origin must be 3 characters IATA code",
                ),
                Arguments.of(
                    FlightSearchQueryDto(
                        origin = "AMS",
                        destination = "MLNQ",
                        departureDate = LocalDate.now(),
                        returnDate = LocalDate.now().plusDays(1),
                        numberOfPassengers = 2
                    ), "destination must be 3 characters IATA code"
                ),
                Arguments.of(
                    FlightSearchQueryDto(
                        origin = "AMS",
                        destination = "MLN",
                        departureDate = LocalDate.now().minusDays(3),
                        returnDate = LocalDate.now().plusDays(1),
                        numberOfPassengers = 2
                    ), "departureDate must be a date in the present or in the future"
                ),
                Arguments.of(
                    FlightSearchQueryDto(
                        origin = "AMS",
                        destination = "MLN",
                        departureDate = LocalDate.now(),
                        returnDate = LocalDate.now().minusDays(4),
                        numberOfPassengers = 2
                    ), "returnDate must be a date in the present or in the future"
                ),
                Arguments.of(
                    FlightSearchQueryDto(
                        origin = "AMS",
                        destination = "AMS",
                        departureDate = LocalDate.now(),
                        returnDate = LocalDate.now().plusDays(1),
                        numberOfPassengers = 2
                    ), "origin and destination must not be equal"
                ),
                Arguments.of(
                    FlightSearchQueryDto(
                        origin = "AMS",
                        destination = "MLN",
                        departureDate = LocalDate.now(),
                        returnDate = LocalDate.now().plusDays(1),
                        numberOfPassengers = 0
                    ), "at least 1 passenger required"
                ),
                Arguments.of(
                    FlightSearchQueryDto(
                        origin = "AMS",
                        destination = "MLN",
                        departureDate = LocalDate.now(),
                        returnDate = LocalDate.now().plusDays(1),
                        numberOfPassengers = 6
                    ), "maximum 4 passengers allowed"
                ),

                )
        }
    }

    @Test
    fun `should aggregate result from all flight suppliers`() {
        crazyAirServer.stubFor(
            post(urlPathEqualTo("/flights"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("crazy-air-response.json")
                )
        )

        toughJetServer.stubFor(
            post(urlPathEqualTo("/flights"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("tough-jet-response.json")
                )
        )

        val query = FlightSearchQueryDto(
            origin = "AMS",
            destination = "MLN",
            departureDate = LocalDate.now(),
            returnDate = LocalDate.now().plusDays(1),
            numberOfPassengers = 2
        )

        val expectedFlights = """
            [
              {
                "airline": "EasyJet",
                "supplier": "ToughJet",
                "fare": 93.14,
                "departureAirportCode": "AMS",
                "destinationAirportCode": "LND",
                "departureDate": "2024-04-01T06:15:00",
                "arrivalDate": "2024-04-08T20:30:00"
              },
              {
                "airline": "KLM",
                "supplier": "ToughJet",
                "fare": 155.99,
                "departureAirportCode": "AMS",
                "destinationAirportCode": "LND",
                "departureDate": "2024-05-10T11:20:00",
                "arrivalDate": "2024-05-17T23:45:00"
              },
              {
                "airline": "TestAir",
                "supplier": "CrazyAir",
                "fare": 199.99,
                "departureAirportCode": "AMS",
                "destinationAirportCode": "LND",
                "departureDate": "2024-03-15T08:30:00",
                "arrivalDate": "2024-03-15T10:45:00"
              }
            ]
        """.trimIndent()

        mockMvc.perform(
            mvcPost("/flights/find")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query))
        )
            .andExpect(status().isOk())
            .andExpect(content().json(expectedFlights, true))
    }


    @ParameterizedTest
    @MethodSource("errorQueries")
    fun `should fail with bad request and error message`(query: FlightSearchQueryDto, message: String) {
        mockMvc.perform(
            mvcPost("/flights/find")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(query))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value(message))
    }
}