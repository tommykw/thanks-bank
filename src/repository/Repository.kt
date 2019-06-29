package com.tommykw.repository

import com.tommykw.model.Emoji

interface Repository {
    suspend fun add(emoji: Emoji): Emoji
    suspend fun emoji(id: Int): Emoji?
    suspend fun emojis(): ArrayList<Emoji>
    suspend fun remove(id: Int): Boolean
    suspend fun remove(emoji: Emoji): Boolean
    suspend fun clear()
}