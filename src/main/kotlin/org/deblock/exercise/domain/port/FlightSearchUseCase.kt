package org.deblock.exercise.domain.port

import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.FlightSearchQuery

/**
 * Service responsible for querying flight suppliers and aggregating flight results.
 */
interface FlightSearchUseCase {

    /**
     * Finds flights from all suppliers concurrently based on the given [query] and returns
     * a list of [Flight] sorted by fare in ascending order.
     *
     * @param query The flight search query containing origin, destination, dates, and passenger count.
     * @return A list of [Flight] sorted by fare.
     */
    fun findFlights(query: FlightSearchQuery): List<Flight>
}