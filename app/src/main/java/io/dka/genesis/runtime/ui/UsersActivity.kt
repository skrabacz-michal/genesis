package io.dka.genesis.runtime.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import arrow.effects.ev
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapter.Type
import io.dka.genesis.BR
import io.dka.genesis.R
import io.dka.genesis.algebra.ui.UsersListView
import io.dka.genesis.algebra.ui.model.UserViewModel
import io.dka.genesis.algebra.ui.presentation
import io.dka.genesis.databinding.ItemUserBinding
import io.dka.genesis.runtime.instances.TargetRuntime
import kotlinx.android.synthetic.main.activity_users.*

class UsersActivity : AppCompatActivity(), UsersListView
{
    private val presentation = presentation<TargetRuntime>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        usersList.setHasFixedSize(true)
        usersList.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume()
    {
        super.onResume()
        presentation.drawUsers(this).ev().unsafeRunAsync { }
    }

    /**
     * Presentation
     */
    override fun drawUsers(users: List<UserViewModel>) = runOnUiThread {
        LastAdapter(users, BR.user)
                .map<UserViewModel>(Type<ItemUserBinding>(R.layout.item_user)
                        .onClick {
                            it.binding.user?.id?.let { userId ->
                                presentation.onUserClick(this, userId).ev().unsafeRunAsync { }
                            }
                        }
                )
                .into(usersList)
    }

    override fun showNotFoundError()
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showGenericError()
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
