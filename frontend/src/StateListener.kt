package com.tommykw

interface StateListener<in T> {
    fun apply(state: T)
}

