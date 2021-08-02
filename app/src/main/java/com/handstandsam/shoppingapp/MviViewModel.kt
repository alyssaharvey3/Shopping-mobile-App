package com.handstandsam.shoppingapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class MviViewModel<State, Intention, SideEffect>(
    scope: CoroutineScope,
    initialState: State
) : ViewModel() {

    private val _states = MutableStateFlow(initialState)

    private val _sideEffects = MutableSharedFlow<SideEffect>(Channel.UNLIMITED)

    val states: Flow<State> = _states

    val sideEffects: Flow<SideEffect> = _sideEffects.asSharedFlow()

    private val actor = scope.actor<Intention> {
        channel.consumeEach { intention ->
            _states.value = reduce(
                state = _states.value,
                intention = intention
            )
        }
    }

    abstract fun reduce(state: State, intention: Intention): State

    protected fun sideEffect(sideEffect: SideEffect) {
        _sideEffects.tryEmit(sideEffect)
    }

    fun send(intention: Intention) {
        actor.trySend(intention)
    }
}