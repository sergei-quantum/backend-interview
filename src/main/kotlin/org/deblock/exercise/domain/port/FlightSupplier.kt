package org.deblock.exercise.domain.port

import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchQuery

interface FlightSupplier {
    fun findFlights(query: FlightSearchQuery): List<Flight>
}