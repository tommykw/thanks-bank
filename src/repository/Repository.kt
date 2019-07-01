package com.tommykw.repository

import com.tommykw.model.Emoji

interface Repository {
    suspend fun add(emojiValue: String)
    suspend fun emoji(id: Int): Emoji?
    suspend fun emojis(): List<Emoji>
    suspend fun remove(id: Int): Boolean
    suspend fun clear()
}