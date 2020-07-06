package com.tommykw.repository

import com.slack.api.Slack
import com.tommykw.model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class PlaygroundRepository : Repository {
    override suspend fun addPlayground(nameValue: String, codeValue: String) =
        DatabaseFactory.dbQuery {
            transaction {
                val insertStatement = Playgrounds.insert {
                    it[name] = nameValue
                    it[code] = codeValue
                }

                val result = insertStatement.resultedValues?.get(0)
                if (result != null) {
                    toPlayground(result)
                } else {
                    null
                }
            }
        }

    override suspend fun updatePlayground(idValue: Int, nameValue: String, codeValue: String): Int {
        return DatabaseFactory.dbQuery {
            transaction {
                val updateStatement = Playgrounds.update({ Playgrounds.id eq idValue }) {
                    it[name] = nameValue
                    it[code] = codeValue
                }

                updateStatement
            }
        }
    }

    override suspend fun playground(id: Int): Playground? {
        return DatabaseFactory.dbQuery {
            Playgrounds.select {
                (Playgrounds.id eq id)
            }.mapNotNull { toPlayground(it) }
                .singleOrNull()
        }
    }

    override suspend fun playgrounds(): List<Playground> {
        return transaction {
            Playgrounds.selectAll().map { toPlayground(it) }
        }
    }

    override suspend fun removePlayground(id: Int): Boolean {
        if (playground(id) == null) {
            throw IllegalArgumentException("No phrase found for id $id")
        }

        return DatabaseFactory.dbQuery {
            Playgrounds.deleteWhere { Playgrounds.id eq id } > 0
        }
    }

    override suspend fun slackMessages(): List<SlackMessage> {
        return transaction {
            SlackMessages.selectAll().map { toSlackMessage(it) }
        }
    }

    private fun toPlayground(row: ResultRow): Playground {
        return Playground(
            id = row[Playgrounds.id].value,
            name = row[Playgrounds.name],
            code = row[Playgrounds.code]
        )
    }

    private fun toSlackMessage(row: ResultRow): SlackMessage {
        return SlackMessage(
            id = row[SlackMessages.id].value,
            userName = row[SlackMessages.userName],
            message = row[SlackMessages.message]
        )
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
            .map {
                User(
                    it[Users.id],
                    email,
                    it[Users.displayName],
                    it[Users.passwordHash]
                )
            }.singleOrNull()
    }

    override suspend fun userById(userId: String) = DatabaseFactory.dbQuery {
        Users.select { Users.id.eq(userId) }
            .map { User(userId, it[Users.email], it[Users.displayName], it[Users.passwordHash]) }.singleOrNull()
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

    override suspend fun createSlackMessage(slackMessage: String, slackUserName: String) = DatabaseFactory.dbQuery {
        transaction {
            val insertStatement = SlackMessages.insert {
                it[message] = slackMessage
                it[userName] = slackUserName
            }

            val result = insertStatement.resultedValues?.get(0)
            if (result != null) {
                toSlackMessage(result)
            } else {
                null
            }
        }
        Unit
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