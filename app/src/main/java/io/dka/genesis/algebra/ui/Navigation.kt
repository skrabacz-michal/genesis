package io.dka.genesis.algebra.ui

import android.content.Context
import arrow.HK
import arrow.TC
import arrow.typeclass
import arrow.typeclasses.Applicative
import io.dka.genesis.runtime.ui.UserDetailsActivity

@typeclass
interface Navigation<F> : TC
{
    fun applicative(): Applicative<F>

    fun goToUserDetails(ctx: Context, userId: Long): HK<F, Unit> =
            applicative().pure(UserDetailsActivity.launch(ctx, userId))
}
