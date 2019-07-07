package com.tommykw.repository

import com.tommykw.model.Emoji
import com.tommykw.model.User

interface Repository {
    suspend fun add(userId: String, emojiValue: String)
    suspend fun emoji(id: Int): Emoji?
    suspend fun emojis(): List<Emoji>
    suspend fun remove(id: Int): Boolean
    suspend fun clear()
    suspend fun user(userId: String, hash: String? = null): User?
    suspend fun userByEmail(email: String): User?
    suspend fun createUser(user: User)
}