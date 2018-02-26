package io.dka.genesis.runtime.instances

import arrow.effects.IO
import arrow.effects.IOHK
import arrow.instance
import io.dka.genesis.algebra.buisness.UserUseCases
import io.dka.genesis.algebra.persistence.DataSource
import io.dka.genesis.algebra.persistence.UserRepository
import io.dka.genesis.algebra.ui.Navigation
import io.dka.genesis.algebra.ui.Presentation

typealias TargetRuntime = IOHK

@instance(IO::class)
interface IOPresentationInstance<F> : Presentation<F>

@instance(IO::class)
interface IONavigationInstance<F> : Navigation<F>

@instance(IO::class)
interface IOUserUseCasesInstance<F> : UserUseCases<F>

@instance(IO::class)
interface IOUserRepositoryInstance<F> : UserRepository<F>

@instance(IO::class)
interface IODataSourceInstance<F> : DataSource<F>
