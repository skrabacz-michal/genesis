package io.dka.genesis.algebra.buisness.model

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import io.dka.genesis.algebra.persistence.model.ApiError
import java.net.HttpURLConnection

sealed class UserError
{
    companion object
    {
        fun from(throwable: Throwable): UserError =
                when (throwable)
                {
                    is ApiError -> when (throwable.httpCode)
                    {
                        HttpURLConnection.HTTP_NOT_FOUND -> NotFoundError
                        else -> UnknownServerError(Some(throwable))
                    }
                    else -> UnknownServerError(Some(throwable))
                }
    }

    object NotFoundError : UserError()
    data class UnknownServerError(val e: Option<Throwable> = None) : UserError()
}
