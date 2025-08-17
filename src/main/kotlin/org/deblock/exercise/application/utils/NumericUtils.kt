package org.deblock.exercise.application.utils

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Returns a [BigDecimal] rounded to 2 decimal places using [RoundingMode.HALF_UP].
 *
 * @receiver The original [BigDecimal] value.
 * @return A new [BigDecimal] with scale 2 and rounding applied.
 */
fun BigDecimal.twoDigitsScale(): BigDecimal = setScale(2, RoundingMode.HALF_UP)