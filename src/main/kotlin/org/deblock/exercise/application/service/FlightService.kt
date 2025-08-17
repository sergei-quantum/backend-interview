package org.deblock.exercise.application.service

import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchQuery
import org.deblock.exercise.domain.port.FlightSupplier
import org.springframework.stereotype.Service

/**
 * Service responsible for querying flight suppliers and aggregating flight results.
 */
@Service
class FlightService(private val suppliers: List<FlightSupplier>) {

    companion object {
        private val FARE_COMPARATOR: Comparator<Flight> = compareBy { it.fare }
    }

    /**
     * Finds flights from all suppliers based on the given [query] and returns
     * a list of [Flight] sorted by fare in ascending order.
     *
     * @param query The flight search query containing origin, destination, dates, and passenger count.
     * @return A list of [Flight] sorted by fare.
     */
    fun findFlights(query: FlightSearchQuery): List<Flight> {
//        TODO handle concurrently
        return suppliers
            .map { it.findFlights(query) }
            .flatten()
            .sortedWith(FARE_COMPARATOR)
    }
}