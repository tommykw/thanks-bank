package com.tommykw.webapp

import com.tommykw.model.EPSession
import com.tommykw.repository.EmojiRepository
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.html.respondHtml
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import kotlinx.html.*
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

const val ABOUT = "/about"

@Location(ABOUT)
class About

fun Route.about() {
    get<About> {
        val repository by kodein().instance<EmojiRepository>()

        val user = call.sessions.get<EPSession>()?.let { repository.user(it.userId) }

        call.respondHtml {
            head {
                bootStrapLink()
                title { +"About" }
            }
            body {
                nav("navbar navbar-default") {
                    div("container-fluid") {
                        div("navbar-header") {
                            button(type = ButtonType.button, classes = "navbar-toggle collapsed") {
                                span("sr-only") {
                                    +"Toggle navigation"
                                }

                                span("icon-bar") {

                                }

                                span("icon-bar") {

                                }

                                span("icon-bar") {

                                }
                            }
                        }

                        div(classes = "collapse navbar-collapse") {
                            ul("nav navbar-nav") {
                                li() {
                                    a("/") { +"Home" }
                                }
                                li() {
                                    a("/about") { +"Abot" }
                                }
                                li() {
                                    a("/playground") { +"playground" }
                                }
                            }
                        }
                    }
                }
            }
        }

        /**
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="/">Playground</a>

         */
        //call.respond(FreeMarkerContent("about.ftl", mapOf("user" to user)))
    }
}

@HtmlTagMarker
fun FlowOrPhrasingOrMetaDataContent.bootStrapLink() {
    link(
        rel = "stylesheet",
        href = "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css",
        type = "text/css"
    )
}