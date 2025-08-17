package org.deblock.exercise.infrastructure.supplier

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.mockk
import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchQuery
import org.deblock.exercise.domain.model.SupplierType
import org.deblock.exercise.infrastructure.config.CrazyAirConfig
import org.deblock.exercise.infrastructure.dto.CabinClass.BUSINESS
import org.deblock.exercise.infrastructure.dto.CrazyAirResponseDto
import org.deblock.exercise.infrastructure.exceptions.RemoteServiceException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class CrazyAirSupplierTest {

    private val crazyAirConfig = mockk<CrazyAirConfig>()
    private val restTemplate = mockk<RestTemplate>()
    private val supplier = CrazyAirSupplier(crazyAirConfig, restTemplate)

    private val query = FlightSearchQuery(
        origin = "JFK",
        destination = "LAX",
        departureDate = LocalDate.parse("2025-08-20"),
        returnDate = LocalDate.parse("2025-08-23"),
        numberOfPassengers = 2
    )

    @BeforeEach
    fun init() {
        every { crazyAirConfig.baseUrl } returns "http://crazyair.com"
    }

    @Test
    fun `should map response correctly`() {
        val responseDto = arrayOf(
            CrazyAirResponseDto(
                airline = "Airline",
                price = BigDecimal("200.00"),
                cabinclass = BUSINESS,
                departureAirportCode = "JFK",
                destinationAirportCode = "LAX",
                departureDate = LocalDateTime.parse("2025-08-20T10:00"),
                arrivalDate = LocalDateTime.parse("2025-08-20T18:00")
            )
        )
        every { restTemplate.postForEntity(any<String>(), any(), Array<CrazyAirResponseDto>::class.java) }
            .returns(ResponseEntity.ok(responseDto))

        supplier.findFlights(query) shouldBe listOf(
            Flight(
                airline = "Airline",
                supplier = SupplierType.CRAZY_AIR,
                fare = BigDecimal("200.00"),
                departureAirportCode = "JFK",
                destinationAirportCode = "LAX",
                departureDate = LocalDateTime.parse("2025-08-20T10:00"),
                arrivalDate = LocalDateTime.parse("2025-08-20T18:00")
            )
        )
    }

    @Test
    fun `should throw RemoteServiceException when response is not successful`() {
        every { restTemplate.postForEntity(any<String>(), any(), Array<CrazyAirResponseDto>::class.java) }
            .returns(ResponseEntity.internalServerError().build())

        val exception = assertThrows<RemoteServiceException> { supplier.findFlights(query) }
        exception.message shouldContain  "Unexpected error from CRAZY_AIR supplier"
    }
}