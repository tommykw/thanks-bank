package com.tommykw.thanks_bank.repository

import com.slack.api.Slack
import com.slack.api.methods.request.users.UsersListRequest
import com.slack.api.methods.response.users.UsersListResponse
import com.tommykw.thanks_bank.model.User
import com.tommykw.thanks_bank.model.UserRequest
import com.tommykw.thanks_bank.model.UsersTable
import com.tommykw.thanks_bank.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

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

    suspend fun getUsers(): List<User> {
        return dbQuery {
            UsersTable.selectAll().map { UsersTable.toUser(it) }
        }
    }

    suspend fun getUser(slackUserId: String): User? {
        return dbQuery {
            UsersTable.select {
                UsersTable.slackUserId eq slackUserId
            }.map { UsersTable.toUser(it) }.singleOrNull()
        }
    }

    suspend fun getSlackMembers(): UsersListResponse {
        val slack = Slack.getInstance()
        val apiClient = slack.methods(System.getenv("SLACK_BOT_TOKEN"))

        val request = UsersListRequest
            .builder()
            .token(System.getenv("SLACK_BOT_TOKEN"))
            .build()

        return dbQuery {
            apiClient.usersList(request)
        }
    }
}