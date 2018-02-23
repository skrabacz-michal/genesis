package io.dka.genesis.algebra.persistence

import arrow.HK
import arrow.TC
import arrow.data.Try
import arrow.effects.Async
import arrow.syntax.either.right
import arrow.typeclass
import arrow.typeclasses.binding
import io.dka.genesis.BuildConfig
import io.dka.genesis.algebra.persistence.model.ApiError
import io.dka.genesis.algebra.persistence.model.UserEntity
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@typeclass
interface DataSource<F> : TC
{
    fun AC(): Async<F>

    private val apiClient
        get() = Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)

    fun fetchAllUsers(): HK<F, List<UserEntity>> =
            AC().binding {
                runInAsyncContext(
                        f = { fetchUsers() },
                        onError = { AC().raiseError<List<UserEntity>>(it) },
                        onSuccess = { AC().pure(it) },
                        AC = AC()
                ).bind()
            }

    fun fetchUserDetails(userId: Long): HK<F, UserEntity> =
            AC().binding {
                runInAsyncContext(
                        f = { fetchUser(userId) },
                        onError = { AC().raiseError<UserEntity>(it) },
                        onSuccess = { AC().pure(it) },
                        AC = AC()
                ).bind()
            }

    // FIXME msq -
    private fun fetchUsers(): List<UserEntity> =
            apiClient.users().execute().let { response ->
                when (response.isSuccessful)
                {
                    true -> response.body() ?: emptyList()
                    false -> throw ApiError(httpCode = response.code(), description = response.message())
                }
            }

    private fun fetchUser(userId: Long): UserEntity =
            apiClient.user(userId).execute().let { response ->
                when (response.isSuccessful)
                {
                    true -> response.body() ?: throw ApiError(httpCode = HttpURLConnection.HTTP_NOT_FOUND, description = response.message())
                    false -> throw ApiError(httpCode = response.code(), description = response.message())
                }
            }

    /**
     * Run async and fold result
     */
    private fun <F, A, B> runInAsyncContext(
            f: () -> A,
            onError: (Throwable) -> B,
            onSuccess: (A) -> B, AC: Async<F>): HK<F, B> = AC.async { process ->
        async(CommonPool) {
            val result = Try { f() }.fold(onError, onSuccess)
            process(result.right())
        }
    }
}
