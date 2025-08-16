package org.deblock.exercise.infrastructure.supplier

import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightQuery
import org.deblock.exercise.domain.model.SupplierType
import org.deblock.exercise.domain.model.SupplierType.TOUGH_JET
import org.deblock.exercise.domain.port.FlightSupplier
import org.deblock.exercise.infrastructure.dto.ToughJetRequestDto
import org.deblock.exercise.infrastructure.dto.ToughJetResponseDto
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.time.LocalDateTime

@Component
class ToughJetSupplier(private val toughJetApiUrl: String, private val restTemplate: RestTemplate) : FlightSupplier {

    override fun findFlights(query: FlightQuery): List<Flight> {
        val request = query.toRequest()
        val response = restTemplate.postForEntity("${toughJetApiUrl}/flights", request, Array<ToughJetResponseDto>::class.java)
        // TODO add error handling
        return response.body!!.toList().map { it.toFlightResponse()}
    }


    private fun FlightQuery.toRequest(): ToughJetRequestDto {
        return ToughJetRequestDto(
            from = origin,
            to = destination,
            outboundDate = departureDate.atStartOfDay(),
            arrivalDate = returnDate.atStartOfDay(), // TODO double check it
            numberOfAdults = numberOfPassengers
        )
    }

    private fun ToughJetResponseDto.toFlightResponse(): Flight {
        return Flight(
            airline = carrier,
            supplier = TOUGH_JET,
            fare = calculateFare(), // calculate
            departureAirportCode = departureAirportName,
            destinationAirportCode = arrivalAirportName,
            departureDate = outboundDateTime.toLocalDate(), // todo double check it
            arrivalDate = inboundDateTime.toLocalDate()
        )
    }

    private fun ToughJetResponseDto.calculateFare(): BigDecimal {
        return basePrice
            .multiply(BigDecimal.ONE - discount)
            .multiply(BigDecimal.ONE + tax)
    }
}