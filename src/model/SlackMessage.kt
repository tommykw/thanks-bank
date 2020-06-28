package com.tommykw.model

import org.jetbrains.exposed.dao.IntIdTable
import java.io.Serializable

data class SlackMessage(
    val id: Int,
    val message: String,
    val userName: String
) : Serializable

object SlackMessages: IntIdTable() {
    val message = varchar("message", 255)
    val userName = varchar("userName", 255)
}