package com.tommykw.module

import com.slack.api.bolt.App
import com.slack.api.bolt.response.Response
import com.slack.api.model.kotlin_extension.view.blocks
import com.slack.api.model.view.Views
import io.ktor.application.Application

fun Application.slackCommand(app: App) {
    app.command("/thanks") { _, ctx ->
        val res = ctx.client().viewsOpen {
            it.triggerId(ctx.triggerId)
            it.view(
                Views.view { thisView -> thisView
                    .callbackId("thanks-message")
                    .type("modal")
                    .notifyOnClose(true)
                    .title(Views.viewTitle { title -> title.type("plain_text").text("あなたのありがと〜〜！を教えて!!").emoji(true) })
                    .submit(Views.viewSubmit { submit -> submit.type("plain_text").text("送信").emoji(true) })
                    .close(Views.viewClose { close -> close.type("plain_text").text("キャンセル").emoji(true) })
                    .privateMetadata("{\"response_url\":\"https://hooks.slack.com/actions/T1ABCD2E12/330361579271/0dAEyLY19ofpLwxqozy3firz\"}")
                    .blocks {
                        input {
                            blockId("user-block")
                            label(text = "🔛 誰に届けますか？", emoji = true)
                            element {
                                multiUsersSelect {
                                    actionId("user-action")
                                    placeholder("選択してみよう")
                                }
                            }
                        }
                        input {
                            blockId("message-block")
                            element {
                                plainTextInput {
                                    actionId("message-action")
                                    multiline(true)
                                }
                            }
                            label(text = "メッセージをどうぞ", emoji = true)
                        }
                    }
                }
            )
        }

        if (res.isOk) {
            ctx.ack()
        } else {
            Response.builder().statusCode(500).body(res.error).build()
        }
    }
}