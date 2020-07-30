package com.tommykw.repository

import com.tommykw.model.*

interface Repository {
    suspend fun playgrounds(): List<Playground>
    suspend fun playground(id: Int): Playground?
    suspend fun addPlayground(name: String, code: String): Playground?
    suspend fun updatePlayground(id: Int, name: String, code: String): Int
    suspend fun removePlayground(id: Int): Boolean
    suspend fun user(userId: String, hash: String? = null): User?
    suspend fun userByEmail(email: String): User?
    suspend fun userById(userId: String): User?
    suspend fun createUser(user: User)
    suspend fun createSlackMessage(slackMessage: String, slackUserName: String)
    suspend fun slackMessages(): List<SlackMessage>
    suspend fun getThanks(): List<Thank>
    suspend fun createThank(thanks: ThankRequest)
    suspend fun getSlackMembers()
}