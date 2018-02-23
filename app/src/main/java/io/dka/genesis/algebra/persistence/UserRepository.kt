package io.dka.genesis.algebra.persistence

import arrow.HK
import arrow.TC
import arrow.typeclass
import io.dka.genesis.algebra.persistence.model.UserEntity

@typeclass
interface UserRepository<F> : TC
{
    sealed class CachePolicy
    {
        object NetworkOnly : CachePolicy()
        // TODO msq - will be more
    }

    fun dataSource(): DataSource<F>

    fun getUsers(cachePolicy: CachePolicy): HK<F, List<UserEntity>> =
            when (cachePolicy)
            {
                CachePolicy.NetworkOnly -> dataSource().fetchAllUsers()
            }

    fun getUser(cachePolicy: CachePolicy, userId: Long): HK<F, UserEntity> =
            when (cachePolicy)
            {
                CachePolicy.NetworkOnly -> dataSource().fetchUserDetails(userId)
            }
}
