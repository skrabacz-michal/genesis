package io.dka.genesis.algebra.ui

import android.content.Context
import arrow.HK
import arrow.TC
import arrow.typeclass
import arrow.typeclasses.MonadError
import arrow.typeclasses.binding
import io.dka.genesis.algebra.buisness.UserUseCases
import io.dka.genesis.algebra.buisness.model.UserError
import io.dka.genesis.algebra.persistence.model.UserEntity
import io.dka.genesis.algebra.ui.model.UserViewModel

interface UserView
{
    fun showNotFoundError()
    fun showGenericError()
}

interface UsersListView : UserView
{
    fun drawUsers(users: List<UserViewModel>)
}

interface UserDetailsView : UserView
{
    fun drawUser(user: UserViewModel)
}

@typeclass
interface Presentation<F> : TC
{
    fun navigation(): Navigation<F>

    fun usersService(): UserUseCases<F>

    fun ME(): MonadError<F, Throwable>

    /**
     * Input
     */
    fun onUserClick(ctx: Context, userId: Long): HK<F, Unit> =
            navigation().goToUserDetails(ctx, userId)

    /**
     * Draw
     */
    fun drawUsers(view: UsersListView): HK<F, Unit> =
            ME().binding {
                val result = ME().handleError(usersService().getUsers(), { displayErrors(view, it); emptyList() }).bind()
                ME().pure(view.drawUsers(result.map { UserViewModel(id = it.id, name = it.name) }))
            }

    fun drawUser(userId: Long, view: UserDetailsView): HK<F, Unit> =
            ME().binding {
                val result = ME().handleError(usersService().getUserDetails(userId), { displayErrors(view, it); UserEntity.empty() }).bind()
                ME().pure(view.drawUser(UserViewModel(id = result.id, name = result.name)))
            }

    /**
     * Errors
     */
    private fun displayErrors(view: UserView, error: Throwable): HK<F, Unit> =
            ME().pure(when (UserError.from(error))
            {
                UserError.NotFoundError -> view.showNotFoundError()
                is UserError.UnknownServerError -> view.showGenericError()
            })
}
