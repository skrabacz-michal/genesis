package io.dka.genesis.algebra.buisness

import arrow.HK
import arrow.TC
import arrow.typeclass
import io.dka.genesis.algebra.persistence.UserRepository
import io.dka.genesis.algebra.persistence.model.UserEntity

@typeclass
interface UserUseCases<F> : TC
{
    fun repository(): UserRepository<F>

    fun getUsers(): HK<F, List<UserEntity>> =
            repository().getUsers(cachePolicy = UserRepository.CachePolicy.NetworkOnly)

    fun getUserDetails(userId: Long) =
            repository().getUser(cachePolicy = UserRepository.CachePolicy.NetworkOnly, userId = userId)
}
