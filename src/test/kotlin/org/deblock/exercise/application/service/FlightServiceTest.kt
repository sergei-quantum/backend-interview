package org.deblock.exercise.application.service

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchQuery
import org.deblock.exercise.domain.model.SupplierType.CRAZY_AIR
import org.deblock.exercise.domain.model.SupplierType.TOUGH_JET
import org.deblock.exercise.domain.port.FlightSupplier
import org.junit.jupiter.api.Test
import org.springframework.core.task.SyncTaskExecutor
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

class FlightServiceTest {

    private val supplier1 = mockk<FlightSupplier>()
    private val supplier2 = mockk<FlightSupplier>()
    private val taskExecutor = SyncTaskExecutor()
    private val flightService = FlightService(listOf(supplier1, supplier2), taskExecutor)

    @Test
    fun `should aggregate and sort flights from multiple suppliers`() {
        val query = FlightSearchQuery(
            origin = "AMS",
            destination = "LND",
            departureDate = LocalDate.parse("2025-08-01"),
            returnDate = LocalDate.parse("2025-08-10"),
            numberOfPassengers = 1
        )
        val flight1 = Flight(
            airline = "KLM",
            supplier = CRAZY_AIR,
            fare = BigDecimal("200.00"),
            departureAirportCode = "AMS",
            destinationAirportCode = "LND",
            departureDate = LocalDate.parse("2025-08-01").atStartOfDay(),
            arrivalDate = LocalDate.parse("2025-08-01").atTime(LocalTime.MAX)
        )
        val flight2 = Flight(
            airline = "EasyJet",
            supplier = TOUGH_JET,
            fare = BigDecimal("150.00"),
            departureAirportCode = "AMS",
            destinationAirportCode = "LND",
            departureDate = LocalDate.parse("2025-08-01").atStartOfDay(),
            arrivalDate = LocalDate.parse("2025-08-01").atTime(LocalTime.MAX)
        )
        every { supplier1.findFlights(query) } returns listOf(flight1)
        every { supplier2.findFlights(query) } returns listOf(flight2)

        flightService.findFlights(query) shouldBe listOf(flight2, flight1)
    }
}