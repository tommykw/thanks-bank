package com.tommykw.model

import org.jetbrains.exposed.sql.Table
import java.io.Serializable

data class User(
    val userId: String,
    val email: String,
    val displayName: String,
    val passwordhash: String
): Serializable

object Users: Table() {
    val id = varchar("id", 20).primaryKey()
    val email = varchar("email", 128).uniqueIndex()
    val displayName = varchar("display_name", 256)
    val passwordHash = varchar("password_hash", 64)
}