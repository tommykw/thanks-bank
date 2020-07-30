package com.tommykw.model

data class ThankRequest(
    val slackUserId: String,
    val body: String,
    val targetSlackUserId: String
)