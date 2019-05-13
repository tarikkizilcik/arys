package org.example.arys.data

data class SentMessage(override val body: String, override val createdAt: Long) : Message