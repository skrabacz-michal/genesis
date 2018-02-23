package io.dka.genesis.algebra.persistence.model

data class UserEntity(val id: Long, val name: String, val username: String, val email: String)
{
    companion object
    {
        fun empty() = UserEntity(id = 0, name = "", username = "", email = "")
    }
}
