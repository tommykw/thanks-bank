package com.tommykw.repository

import com.tommykw.model.Emoji
import com.tommykw.model.Playground
import com.tommykw.model.User

interface ERepository {
    suspend fun playgrounds(): List<Playground>
    suspend fun playground(id: Int): Playground?
    suspend fun addPlayground(name: String, code: String): Playground?
    suspend fun removePlayground(id: Int): Boolean
}