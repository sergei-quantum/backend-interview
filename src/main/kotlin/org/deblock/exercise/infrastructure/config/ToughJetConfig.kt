package org.deblock.exercise.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "suppliers.tough-jet")
data class ToughJetConfig(val baseUrl: String)