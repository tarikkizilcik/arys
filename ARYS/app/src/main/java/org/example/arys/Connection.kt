package org.example.arys

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object Connection {
    private const val TAG = "Connection"
    const val URL = "http://10.0.3.2:23058"
    private const val EVENT_AUTHENTICATE = "authenticate"
    const val EVENT_AUTHENTICATED = "authenticated"
    private const val EVENT_UNAUTHORIZED = "unauthorized"
    const val EVENT_ROOMS = "rooms"
    const val EVENT_JOIN = "join"

    var socket: Socket = IO.socket(URL)

    init {
        socket
            .on(Socket.EVENT_CONNECT) { Log.d(TAG, "Connected to server") }
            .on(EVENT_AUTHENTICATED) { Log.d(TAG, "Authenticated") }
            .on(Socket.EVENT_ERROR) { err -> Log.e(TAG, err.toString()) }
            .on(EVENT_UNAUTHORIZED) { err -> Log.e(TAG, err.toString()) }
    }

    fun authenticate(jwt: JSONObject) {
        socket.connect()
        socket.emit(EVENT_AUTHENTICATE, jwt)
    }
}