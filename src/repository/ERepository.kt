package com.tommykw.repository

import com.tommykw.model.Playground

interface ERepository {
    suspend fun playgrounds(): List<Playground>
    suspend fun playground(id: Int): Playground?
    suspend fun addPlayground(name: String, code: String): Playground?
    suspend fun updatePlayground(id: Int, name: String, code: String): Int
    suspend fun removePlayground(id: Int): Boolean
}