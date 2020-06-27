package com.tommykw.auth

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.calendar.CalendarScopes
import java.io.ByteArrayInputStream

class GoogleAuth {
    val scopes = listOf(CalendarScopes.CALENDAR_READONLY)
    val credentialsFilePath = "/credentials/cleint_secret.json"
    val clientSecret = System.getenv("GOOGLE_CLIENT_SECRET")
    val accountUser = System.getenv("GOOGLE_ACCOUNT_USER")

    fun getCredential(
        httpTransport: NetHttpTransport,
        jsonFactory: JacksonFactory
    ): Credential {
        val reader = if (clientSecret.isNullOrBlank()) {
            this::class.java.getResourceAsStream(credentialsFilePath)
        } else {
            ByteArrayInputStream(clientSecret.toByteArray(Charsets.UTF_8))
        }

        val credential = GoogleCredential.fromStream(reader)
        return credential.toBuilder()
            .setTransport(httpTransport)
            .setJsonFactory(jsonFactory)
            .setServiceAccountScopes(scopes)
            .setServiceAccountUser(accountUser)
            .build()
    }
}