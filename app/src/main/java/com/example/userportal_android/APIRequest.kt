package com.example.userportal_android

import java.net.HttpURLConnection
import java.net.URL

import kotlinx.serialization.*
import kotlinx.serialization.json.Json

class APIRequest {
    private var config : Config = Config()

    public fun APIRequest(config : Config?) {
        if (config != null)
            this.config = config
    }

    @kotlinx.serialization.UnstableDefault
    public fun Execute(deserializer : DeserializationStrategy<out Any?>?): Any? {
        var result : Any? = null

        if (config.method == "GET" ) {
            val response = URL(this.config.baseAddress + this.config.endpoint).readText()
            deserializer?.let { result = Json.parse(it, response) }
        }

        return result
    }

    data class Config (
        val baseAddress : String = "https://userportal-web.azurewebsites.net/",
        val endpoint : String = "",
        val method : String = "GET")
}
