package org.deblock.exercise.application.service

import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightQuery
import org.deblock.exercise.domain.port.FlightSupplier
import org.springframework.stereotype.Service

@Service
class FlightService(private val suppliers: List<FlightSupplier>) {

    fun findFlights(query: FlightQuery): List<Flight> {
        return suppliers
            .map { it.findFlights(query) }
            .flatten()
            .sortedBy { it.fare }
//        TODO add concurrency
    }

}