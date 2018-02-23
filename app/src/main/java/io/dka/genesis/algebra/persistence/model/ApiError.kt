package io.dka.genesis.algebra.persistence.model

class ApiError(val httpCode: Int, description: String?) : Throwable(description)
