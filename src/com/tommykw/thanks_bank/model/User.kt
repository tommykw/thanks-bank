package com.tommykw.thanks_bank.model

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.io.Serializable

data class User(
    val id: Int,
    val slackUserId: String,
    val realName: String,
    val userImage: String,
    val createdAt: DateTime,
    val updatedAt: DateTime
) : Serializable

object UsersTable: IntIdTable(name = "users") {
    val slackUserId = varchar(name = "slack_user_id", length = 255)
    val realName = varchar(name = "real_name", length = 255)
    val userImage = varchar(name = "user_image", length = 255)
    val createdAt = datetime(name = "created_at").default(DateTime.now())
    val updatedAt = datetime(name = "updated_at").default(DateTime.now())

    fun toUser(row: ResultRow): User {
        return User(
            id = row[id].value,
            slackUserId = row[slackUserId],
            realName = row[realName],
            userImage = row[userImage],
            createdAt = row[createdAt],
            updatedAt = row[updatedAt],
        )
    }
}

data class UserRequest(
    val slackUserId: String,
    val realName: String,
    val userImage: String,
)