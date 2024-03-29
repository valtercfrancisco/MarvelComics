package com.valter.marvelcomics.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersContainer {
    val Default: CoroutineDispatcher
    val Main: CoroutineDispatcher
    val Unconfined: CoroutineDispatcher
    val IO: CoroutineDispatcher
}
