package com.tommykw.thanks_bank.module

import com.slack.api.bolt.App
import com.slack.api.model.event.MessageEvent
import com.tommykw.thanks_bank.model.UserRequest
import com.tommykw.thanks_bank.repository.ThankRepository
import com.tommykw.thanks_bank.repository.UserRepository
import io.ktor.application.Application
import kotlinx.coroutines.launch

fun Application.slackMessageEvent(
    app: App,
    thankRepository: ThankRepository,
    userRepository: UserRepository
) {
    app.event(MessageEvent::class.java) { payload, ctx ->
        val event = payload.event

        if (event.channel == System.getenv("SLACK_THANKS_CHANNEL")) {
            launch {
                thankRepository.createThankReply(event)

                if (userRepository.getUser(event.user) == null) {
                    val slackUsersInfo = userRepository.getSlackUsersInfo(event.user)

                    userRepository.createUser(
                        UserRequest(
                            slackUserId = event.user,
                            realName = slackUsersInfo.user.realName,
                            userImage = slackUsersInfo.user.profile.image512
                        )
                    )
                }
            }
        }

        ctx.ack()
    }
}