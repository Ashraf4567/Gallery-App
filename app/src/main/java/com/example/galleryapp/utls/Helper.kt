package com.example.galleryapp.utls

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import java.io.File

object Helper {
    suspend fun downloadImage(url: String, outputPath: String) {
        try {
            val client = HttpClient()
            val response: HttpResponse = client.get(url)
            val bytes = response.readBytes()
            val file = File(outputPath)
            file.writeBytes(bytes)
            client.close()
        } catch (e: Exception) {
            throw e
        }
    }
}