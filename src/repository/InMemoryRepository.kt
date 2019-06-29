package com.tommykw.repository

import com.tommykw.model.Emoji
import java.lang.IllegalArgumentException
import java.util.concurrent.atomic.AtomicInteger

class InMemoryRepository : Repository {

    private val idCounter = AtomicInteger()
    private val emojis = ArrayList<Emoji>()

    override suspend fun add(emoji: Emoji): Emoji {
        if (emojis.contains(emoji)) {
            return emojis.find { it == emoji }!!
        }

        emoji.id = idCounter.incrementAndGet()
        emojis.add(emoji)
        return emoji
    }

    override suspend fun emoji(id: Int) = emojis.find { it.id == id }

    override suspend fun emojis() = emojis.toList()

    override suspend fun remove(id: Int): Boolean {
        emojis.find { it.id == id } ?: throw IllegalArgumentException("No emoji found for id $id")
        return emojis.removeIf { it.id == id }
    }

    override suspend fun remove(emoji: Emoji) = emojis.remove(emoji)

    override suspend fun clear() = emojis.clear()
}