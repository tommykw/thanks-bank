package com.tommykw.repository

import com.tommykw.model.Emoji
import com.tommykw.model.EmojiData
import com.tommykw.model.User
import com.tommykw.model.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class EmojiRepository : Repository {
    override suspend fun add(userId: String, emojiValue: String) {
        transaction {
            EmojiData.insert {
                it[user] = userId
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

    override suspend fun user(userId: String, hash: String?): User? {
        val user = DatabaseFactory.dbQuery {
            Users.select {
                Users.id eq userId
            }.mapNotNull { toUser(it) }
                .singleOrNull()
        }

        return when {
            user == null -> null
            hash == null -> user
            user.passwordhash == hash -> user
            else -> null
        }
    }

    override suspend fun userByEmail(email: String) = DatabaseFactory.dbQuery {
        Users.select { Users.email.eq(email) }
            .map { User(
                it[Users.id],
                email,
                it[Users.displayName],
                it[Users.passwordHash]
            ) }.singleOrNull()
    }

    override suspend fun createUser(user: User) = DatabaseFactory.dbQuery {
        Users.insert {
            it[id] = user.userId
            it[displayName] = user.displayName
            it[email] = user.email
            it[passwordHash] = user.passwordhash
        }
        Unit
    }

    private fun toEmoji(row: ResultRow): Emoji {
        return Emoji(
            id = row[EmojiData.id].value,
            userId = row[EmojiData.user],
            name = row[EmojiData.emoji]
        )
    }

    private fun toUser(row: ResultRow): User {
        return User(
            userId = row[Users.id],
            email = row[Users.email],
            displayName = row[Users.displayName],
            passwordhash = row[Users.passwordHash]
        )
    }
}