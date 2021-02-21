package com.tommykw.thanks_bank.model

data class ThankRequest(
    val slackUserId: String,
    val body: String,
    val targetSlackUserId: String,
)