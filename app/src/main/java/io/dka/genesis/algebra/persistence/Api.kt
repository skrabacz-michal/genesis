package io.dka.genesis.algebra.persistence

import io.dka.genesis.algebra.persistence.model.UserEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Api
{
    @GET("users")
    fun users(): Call<List<UserEntity>>

    @GET("users/{userId}")
    fun user(@Path("userId") userId: Long): Call<UserEntity>
}
