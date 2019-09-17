package com.example.exfunctionalkotlin.store

import androidx.arch.core.util.Function
import com.example.exfunctionalkotlin.model.Action

interface Store<T> {
    fun disPath(action: Action)

    fun subscribe(renderer: Renderer<T>, func: Function<T, T> = Function { it })
}