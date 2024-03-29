package com.valter.marvelcomics.dispatchers

import com.valter.marvelcomics.dispatchers.DispatchersContainer
import kotlinx.coroutines.Dispatchers

/**
 * DispatcherContainer for tests
 */
object TestDispatcherContainer : DispatchersContainer {
    override val Default = Dispatchers.Unconfined
    override val Main = Dispatchers.Unconfined
    override val Unconfined = Dispatchers.Unconfined
    override val IO = Dispatchers.Unconfined
}