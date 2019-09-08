package com.tommykw.repository

import com.tommykw.model.EmojiData
import com.tommykw.model.Playground
import com.tommykw.model.Playgrounds
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class PlaygroundRepository : ERepository {
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

    override suspend fun playground(id: Int): Playground? {
        return DatabaseFactory.dbQuery {
            EmojiData.select {
                (EmojiData.id eq id)
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

    private fun toPlayground(row: ResultRow): Playground {
        return Playground(
            id = row[Playgrounds.id].value,
            name = row[Playgrounds.name],
            code = row[Playgrounds.code]
        )
    }
}