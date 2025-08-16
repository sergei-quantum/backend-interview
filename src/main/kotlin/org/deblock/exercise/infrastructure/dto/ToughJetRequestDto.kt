package org.deblock.exercise.infrastructure.dto

import java.time.LocalDateTime

data class ToughJetRequestDto(
    val from: String,
    val to: String,
    val outboundDate: LocalDateTime, // use ISO_LOCAL_DATE format
    val arrivalDate: LocalDateTime, // use ISO_LOCAL_DATE format
    val numberOfAdults: Int
)
