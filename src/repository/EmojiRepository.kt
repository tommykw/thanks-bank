package com.tommykw.repository

import com.tommykw.model.Emoji
import com.tommykw.model.EmojiData
import org.jetbrains.exposed.sql.*
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
        return DatabaseFactory.dbQuery {
            EmojiData.select {
                (EmojiData.id eq id)
            }.mapNotNull { toEmoji(it) }
                .singleOrNull()
        }
    }

    override suspend fun emojis(): List<Emoji> {
        return transaction {
            EmojiData.selectAll().map { toEmoji(it) }
        }
    }

    override suspend fun remove(id: Int): Boolean {
        if (emoji(id) == null) {
            throw IllegalArgumentException("No phrase found for id $id")
        }

        return DatabaseFactory.dbQuery {
            EmojiData.deleteWhere { EmojiData.id eq id } > 0
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