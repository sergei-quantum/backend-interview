package org.deblock.exercise.application.service

import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchQuery
import org.deblock.exercise.domain.port.FlightSupplier
import org.springframework.stereotype.Service

@Service
class FlightService(private val suppliers: List<FlightSupplier>) {

    companion object {
        private val FARE_COMPARATOR: Comparator<Flight> = compareBy { it.fare }
    }

    fun findFlights(query: FlightSearchQuery): List<Flight> {
//        TODO handle concurrently
        return suppliers
            .map { it.findFlights(query) }
            .flatten()
            .sortedWith(FARE_COMPARATOR)
    }
}