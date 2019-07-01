package com.tommykw.repository

import com.tommykw.model.Emoji
import com.tommykw.model.EmojiData
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class EmojiRepository : Repository {
    override suspend fun add(emojiValue: String) {
        transaction {
            EmojiData.insert {
                it[emoji] = emojiValue
            }
        }
    }

    override suspend fun emoji(id: Int): Emoji? {
        DatabaseFactory.dbQuery {
            
        }
        return emoji(id)
    }

    override suspend fun emojis(): List<Emoji> {
        return EmojiData.selectAll().map { toEmoji(it) }
    }

    override suspend fun remove(id: Int): Boolean {
        if (emoji(id) == null) {
            throw IllegalArgumentException("No phrase found for id $id")
        }
    }

    override suspend fun clear() {
        EmojiData.deleteAll()
    }

    private fun toEmoji(row: ResultRow): Emoji {
        return Emoji(
            id = row[EmojiData.id].value,
            name = row[EmojiData.emoji]
        )
    }
}