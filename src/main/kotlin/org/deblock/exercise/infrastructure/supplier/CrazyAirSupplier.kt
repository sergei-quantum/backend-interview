package org.deblock.exercise.infrastructure.supplier

import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightQuery
import org.deblock.exercise.domain.model.SupplierType.CRAZY_AIR
import org.deblock.exercise.domain.port.FlightSupplier
import org.deblock.exercise.infrastructure.dto.CrazyAirRequest
import org.deblock.exercise.infrastructure.dto.CrazyAirResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class CrazyAirSupplier(
    private val crazyAirApiUrl: String,
    private val restTemplate: RestTemplate
) : FlightSupplier {

    override fun findFlights(query: FlightQuery): List<Flight> {
        val request = query.toRequest()
        val response = restTemplate.postForEntity("${crazyAirApiUrl}/flights", request, Array<CrazyAirResponse>::class.java)
        // TODO add error handling
        return response.body!!.toList().map { it.toFlightResponse()}
    }

    private fun FlightQuery.toRequest(): CrazyAirRequest {
        return CrazyAirRequest(
            origin = origin,
            destination = destination,
            departureDate = departureDate,
            returnDate = returnDate,
            passengerCount = numberOfPassengers
        )
    }

    private fun CrazyAirResponse.toFlightResponse(): Flight {
        return Flight(
            airline = airline,
            supplier = CRAZY_AIR,
            fare = price,
            departureAirportCode = departureAirportCode,
            destinationAirportCode = destinationAirportCode,
            departureDate = departureDate,
            arrivalDate = arrivalDate
        )
    }
}