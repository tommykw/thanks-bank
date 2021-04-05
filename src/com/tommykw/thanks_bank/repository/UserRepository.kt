package com.tommykw.thanks_bank.repository

import com.slack.api.Slack
import com.slack.api.methods.request.users.UsersInfoRequest
import com.slack.api.methods.response.users.UsersInfoResponse
import com.tommykw.thanks_bank.model.User
import com.tommykw.thanks_bank.model.UserRequest
import com.tommykw.thanks_bank.model.UsersTable
import com.tommykw.thanks_bank.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserRepository {
    suspend fun createUser(request: UserRequest) {
        return dbQuery {
            UsersTable.insert {
                it[slackUserId] = request.slackUserId
                it[realName] = request.realName
                it[userImage] = request.userImage
            }
        }
    }

    suspend fun getUser(slackUserId: String): User? {
        return dbQuery {
            UsersTable.select {
                UsersTable.slackUserId eq slackUserId
            }.map { UsersTable.toUser(it) }.singleOrNull()
        }
    }

    suspend fun getSlackUsersInfo(slackUserId: String): UsersInfoResponse {
        val slack = Slack.getInstance()
        val apiClient = slack.methods(System.getenv("SLACK_BOT_TOKEN"))

        val request = UsersInfoRequest
            .builder()
            .token(System.getenv("SLACK_BOT_TOKEN"))
            .user(slackUserId)
            .build()

        return dbQuery {
            apiClient.usersInfo(request)
        }
    }
}