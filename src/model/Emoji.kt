package com.tommykw.model

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import java.io.Serializable

data class Emoji(
    val id: Int,
    val userId: String,
    val name: String
): Serializable

object EmojiData : IntIdTable() {
    val user: Column<String> = varchar("user_id", 20).index()
    val emoji: Column<String> = varchar("emoji", 255)
}
