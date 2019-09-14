package com.tommykw.repository

import com.tommykw.model.Playground

class InMemoryRepository {
    private var playground: Playground? = null

    suspend fun playground() = playground

    suspend fun updatePlayground(code: String): Playground {
        playground = if (playground == null) {
            Playground(1, "No title", code)
        } else {
            playground!!.copy(code = code)
        }

        return playground!!
    }
}