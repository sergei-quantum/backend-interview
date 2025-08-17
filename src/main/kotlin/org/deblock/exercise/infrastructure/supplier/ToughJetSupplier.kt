package org.deblock.exercise.infrastructure.supplier

import org.deblock.exercise.application.utils.twoDigitsScale
import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchQuery
import org.deblock.exercise.domain.model.SupplierType.CRAZY_AIR
import org.deblock.exercise.domain.model.SupplierType.TOUGH_JET
import org.deblock.exercise.domain.port.FlightSupplier
import org.deblock.exercise.infrastructure.config.ToughJetConfig
import org.deblock.exercise.infrastructure.dto.ToughJetRequestDto
import org.deblock.exercise.infrastructure.dto.ToughJetResponseDto
import org.deblock.exercise.infrastructure.exceptions.RemoteServiceException
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.time.LocalTime

@Component
class ToughJetSupplier(private val toughJetConfig: ToughJetConfig, private val restTemplate: RestTemplate) : FlightSupplier {

    override fun findFlights(query: FlightSearchQuery): List<Flight> {
        val request = query.toRequest()
        val response = restTemplate.postForEntity(
            "${toughJetConfig.baseUrl}/flights",
            request,
            Array<ToughJetResponseDto>::class.java
        )
        if (!response.statusCode.is2xxSuccessful) throw RemoteServiceException("Unexpected error from $TOUGH_JET supplier: ${response.body}")
        return response.body!!.toList().map { it.toFlightResponse()}
    }


    private fun FlightSearchQuery.toRequest(): ToughJetRequestDto {
        return ToughJetRequestDto(
            from = origin,
            to = destination,
            outboundDate = departureDate.atStartOfDay(),
            arrivalDate = returnDate.atTime(LocalTime.MAX),
            numberOfAdults = numberOfPassengers
        )
    }

    private fun ToughJetResponseDto.toFlightResponse(): Flight {
        return Flight(
            airline = carrier,
            supplier = TOUGH_JET,
            fare = calculateFare(),
            departureAirportCode = departureAirportName,
            destinationAirportCode = arrivalAirportName,
            departureDate = outboundDateTime,
            arrivalDate = inboundDateTime
        )
    }

    private fun ToughJetResponseDto.calculateFare(): BigDecimal {
        val taxValue = BigDecimal.ONE.plus(tax.divide(BigDecimal("100")))
        val discountValue = BigDecimal.ONE.minus(discount.divide(BigDecimal("100")))
        return basePrice
            .multiply(taxValue)
            .multiply(discountValue)
            .twoDigitsScale()
    }
}