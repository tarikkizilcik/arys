package org.example.arys.data

data class ReceivedMessage(override val body: String, val sender: User, override val createdAt: Long) : Message