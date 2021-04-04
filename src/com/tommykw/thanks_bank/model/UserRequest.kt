package com.tommykw.thanks_bank.model

data class UserRequest(
    val slackUserId: String,
    val realName: String,
    val userImage: String,
)
