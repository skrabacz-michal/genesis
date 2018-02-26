package io.dka.genesis.algebra.ui

import arrow.effects.Duration
import arrow.effects.IOHK
import arrow.effects.ev
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import java.util.concurrent.TimeUnit

typealias TargetTestRuntime = IOHK

class PresentationUnitTest
{
    private val presentation = presentation<TargetTestRuntime>()

    @Test
    fun drawUsers_isCorrect()
    {
        // given
        val view = mock<UsersListView> {}

        // when
        presentation.drawUsers(view).ev().unsafeRunTimed(Duration(10, TimeUnit.SECONDS))

        // given
        verify(view).drawUsers(any())
    }
}
