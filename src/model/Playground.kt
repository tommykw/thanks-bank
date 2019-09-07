package com.tommykw.model

import org.jetbrains.exposed.dao.IntIdTable
import java.io.Serializable

data class Playground(
    val id: Int,
    val name: String,
    val code: String
) : Serializable

object Playgrounds : IntIdTable() {
    val name = varchar("name", 255)
    val code = varchar("code", 255)
}