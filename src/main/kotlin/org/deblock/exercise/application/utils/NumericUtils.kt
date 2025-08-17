package org.deblock.exercise.application.utils

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.twoDigitsScale(): BigDecimal = setScale(2, RoundingMode.HALF_UP)