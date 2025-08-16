package org.deblock.exercise.domain.port

import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightQuery

interface FlightSupplier {
    fun findFlights(query: FlightQuery): List<Flight>
}