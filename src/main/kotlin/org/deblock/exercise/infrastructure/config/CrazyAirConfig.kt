package org.deblock.exercise.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "suppliers.crazy-air")
data class CrazyAirConfig(val baseUrl: String)