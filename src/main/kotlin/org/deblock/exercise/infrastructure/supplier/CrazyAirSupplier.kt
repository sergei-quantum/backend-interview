package org.deblock.exercise.infrastructure.supplier

import org.deblock.exercise.application.utils.twoDigitsScale
import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchQuery
import org.deblock.exercise.domain.model.SupplierType.CRAZY_AIR
import org.deblock.exercise.domain.port.FlightSupplier
import org.deblock.exercise.infrastructure.config.CrazyAirConfig
import org.deblock.exercise.infrastructure.dto.CrazyAirRequestDto
import org.deblock.exercise.infrastructure.dto.CrazyAirResponseDto
import org.deblock.exercise.infrastructure.exceptions.RemoteServiceException
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

/**
 * Flight supplier implementation for the CrazyAir service.
 */
@Component
class CrazyAirSupplier(
    private val crazyAirConfig: CrazyAirConfig,
    private val restTemplate: RestTemplate
) : FlightSupplier {

    override fun findFlights(query: FlightSearchQuery): List<Flight> {
        val request = query.toRequest()
        val response = restTemplate.postForEntity(
            "${crazyAirConfig.baseUrl}/flights",
            request,
            Array<CrazyAirResponseDto>::class.java
        )
        if (!response.statusCode.is2xxSuccessful) throw RemoteServiceException("Unexpected error from $CRAZY_AIR supplier: ${response.body}")
        return response.body!!.toList().map { it.toFlightResponse()}
    }

    private fun FlightSearchQuery.toRequest(): CrazyAirRequestDto {
        return CrazyAirRequestDto(
            origin = origin,
            destination = destination,
            departureDate = departureDate,
            returnDate = returnDate,
            passengerCount = numberOfPassengers
        )
    }

    private fun CrazyAirResponseDto.toFlightResponse(): Flight {
        return Flight(
            airline = airline,
            supplier = CRAZY_AIR,
            fare = price.twoDigitsScale(),
            departureAirportCode = departureAirportCode,
            destinationAirportCode = destinationAirportCode,
            departureDate = departureDate,
            arrivalDate = arrivalDate
        )
    }
}