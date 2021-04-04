package com.tommykw.thanks_bank.repository

import com.slack.api.Slack
import com.slack.api.methods.request.users.UsersListRequest
import com.slack.api.methods.response.users.UsersListResponse
import com.tommykw.thanks_bank.model.User
import com.tommykw.thanks_bank.model.UserRequest
import com.tommykw.thanks_bank.model.UsersTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository {
    suspend fun createUser(request: UserRequest) {
        transaction {
            UsersTable.insert {
                it[slackUserId] = request.slackUserId
                it[realName] = request.realName
                it[userImage] = request.userImage
            }
        }
    }

    suspend fun getUsers(): List<User> {
        return DatabaseFactory.dbQuery {
            UsersTable.selectAll().map { UsersTable.toUser(it) }
        }
    }

    suspend fun getUser(slackUserId: String): User? {
        return DatabaseFactory.dbQuery {
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

        return withContext(Dispatchers.IO) {
            apiClient.usersList(request)
        }
    }
}