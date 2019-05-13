package org.example.arys

import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_chat.*
import org.example.arys.adapters.ChatAdapter
import org.example.arys.data.Message
import org.example.arys.data.ReceivedMessage
import org.example.arys.data.SentMessage
import org.example.arys.data.User
import org.json.JSONObject


class ChatActivity : AppCompatActivity() {
    companion object {
        const val TAG = "ChatActivity"
    }

    private lateinit var mUserJson: JSONObject
    private lateinit var mRoomJson: JSONObject
    private val mMessages = mutableListOf<Message>()
    private val mChatAdapter = ChatAdapter(mMessages)
    private val mSocket = Connection.socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        recyclerViewMessages.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = mChatAdapter
        }

        val userStr = intent.getStringExtra(LoginActivity.EXTRA_USER)
        val roomStr = intent.getStringExtra(HomeActivity.EXTRA_ROOM)

        mUserJson = JSONObject(userStr)
        mRoomJson = JSONObject(roomStr)
        title = mRoomJson.getString("name")

        val messagesJsonArray = mRoomJson.getJSONArray("messages")
        val userEmail = mUserJson.getString("email")
        for (i: Int in 0 until messagesJsonArray.length()) {
            val messageJson = messagesJsonArray[i] as JSONObject
            val body = messageJson.getString("body")
            val createdAt = messageJson.getLong("createdAt")

            val senderJson = messageJson.getJSONObject("sender")
            val senderEmail = senderJson.getString("email")

            val message =
                if (senderEmail == userEmail) {
                    SentMessage(body, createdAt)
                } else {
                    ReceivedMessage(body, User(senderEmail, senderJson.getString("name")), createdAt)
                }
            mMessages.add(message)
        }

        mSocket.on(Socket.EVENT_MESSAGE, onEventMessage)
        buttonChatBoxSend.setOnClickListener(onChatBoxSendClick)
    }

    private val onEventMessage = Emitter.Listener {
        val bodyJson = it[0] as? JSONObject ?: throw IllegalArgumentException()

        val messageJson = bodyJson.getJSONObject("message")
        val senderJson = messageJson.getJSONObject("sender")

        val sender = User(
            senderJson.getString("email"),
            senderJson.getString("name")
        )

        val message = ReceivedMessage(
            messageJson.getString("body"),
            sender,
            messageJson.getLong("createdAt")
        )

        Log.d(TAG, "Received: ${message.body}")
        mMessages.add(message)

        this@ChatActivity.runOnUiThread {
            mChatAdapter.notifyDataSetChanged()
        }
    }

    private val onChatBoxSendClick = View.OnClickListener {
        val message = SentMessage(
            editTextChatBox.text.toString(),
            System.currentTimeMillis()
        )

        mMessages.add(message)

        this@ChatActivity.runOnUiThread {
            mChatAdapter.notifyDataSetChanged()
        }

        editTextChatBox.text.clear()

        val messageJson = JSONObject()
        messageJson.apply {
            put("body", message.body)
            put("sender", mUserJson.getString("_id"))
            put("room", mRoomJson.getString("_id"))
        }

        val bodyJson = JSONObject()
        bodyJson.apply {
            put("message", messageJson)
        }

        Log.d(TAG, "Sent: ${message.body}")
        mSocket.emit(Socket.EVENT_MESSAGE, bodyJson)
    }
}
