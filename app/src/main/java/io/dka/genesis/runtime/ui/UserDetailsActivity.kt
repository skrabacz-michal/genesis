package io.dka.genesis.runtime.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import arrow.effects.ev
import io.dka.genesis.R
import io.dka.genesis.algebra.ui.UserDetailsView
import io.dka.genesis.algebra.ui.model.UserViewModel
import io.dka.genesis.algebra.ui.presentation
import io.dka.genesis.runtime.instances.TargetRuntime
import kotlinx.android.synthetic.main.activity_user_details.*

class UserDetailsActivity : AppCompatActivity(), UserDetailsView
{
    private val presentation = presentation<TargetRuntime>()

    companion object
    {
        private const val USER_ID = "user_id"

        fun launch(parent: Context, userId: Long) =
                parent.startActivity(Intent(parent, UserDetailsActivity::class.java).apply {
                    putExtra(USER_ID, userId)
                })
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
    }

    override fun onResume()
    {
        super.onResume()
        // TODO msq
        intent.extras?.getLong(USER_ID)?.let { userId ->
            presentation.drawUser(userId, this).ev().unsafeRunAsync { }
        } ?: closeWithError()
    }

    /**
     * Presentation
     */
    override fun drawUser(user: UserViewModel) = runOnUiThread {
        userName.text = user.name
    }

    override fun showNotFoundError()
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showGenericError()
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun closeWithError()
    {
        onBackPressed()
    }
}
