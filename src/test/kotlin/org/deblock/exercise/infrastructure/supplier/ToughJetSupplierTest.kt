package org.deblock.exercise.infrastructure.supplier

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.mockk
import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchQuery
import org.deblock.exercise.domain.model.SupplierType
import org.deblock.exercise.infrastructure.config.ToughJetConfig
import org.deblock.exercise.infrastructure.dto.ToughJetResponseDto
import org.deblock.exercise.infrastructure.exceptions.RemoteServiceException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime

class ToughJetSupplierTest {

    private val toughJetConfig = mockk<ToughJetConfig>()
    private val restTemplate = mockk<RestTemplate>()
    private val supplier = ToughJetSupplier(toughJetConfig, restTemplate)
    private val query = FlightSearchQuery(
        origin = "JFK",
        destination = "LAX",
        departureDate = LocalDate.parse("2025-08-20"),
        returnDate = LocalDate.parse("2025-08-25"),
        numberOfPassengers = 2
    )

    @BeforeEach
    fun init() {
        every { toughJetConfig.baseUrl } returns "http://toughjet.com"
    }

    @Test
    fun `should map response and calculate fare correctly`() {
        val responseDto = arrayOf(
            ToughJetResponseDto(
                carrier = "KLM",
                basePrice = BigDecimal("200.00"),
                tax = BigDecimal("25"),
                discount = BigDecimal("10"),
                departureAirportName = "JFK",
                arrivalAirportName = "LAX",
                outboundDateTime = Instant.parse("2025-08-20T10:00:00Z"),
                inboundDateTime = Instant.parse("2025-08-20T19:00:00Z"),
            )
        )
        every {
            restTemplate.postForEntity(any<String>(), any(), Array<ToughJetResponseDto>::class.java)
        } returns ResponseEntity.ok(responseDto)

        supplier.findFlights(query) shouldBe listOf(
            Flight(
                airline = "KLM",
                supplier = SupplierType.TOUGH_JET,
                fare = BigDecimal("225.00"),
                departureAirportCode = "JFK",
                destinationAirportCode = "LAX",
                departureDate = LocalDateTime.parse("2025-08-20T10:00"),
                arrivalDate = LocalDateTime.parse("2025-08-20T19:00"),
            )
        )
    }

    @Test
    fun `should throw RemoteServiceException when response is not successful`() {
        every { restTemplate.postForEntity(any<String>(), any(), Array<ToughJetResponseDto>::class.java) }
            .returns(ResponseEntity.internalServerError().build())

        val exception = org.junit.jupiter.api.assertThrows<RemoteServiceException> { supplier.findFlights(query) }
        exception.message shouldContain  "Unexpected error from TOUGH_JET supplier"
    }
}