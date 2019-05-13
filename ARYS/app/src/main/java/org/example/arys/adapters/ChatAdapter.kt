package org.example.arys.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.example.arys.R
import org.example.arys.data.Message
import org.example.arys.data.ReceivedMessage
import org.example.arys.data.SentMessage

class ChatAdapter(private val dataSet: List<Message>) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {
    companion object {
        const val RECEIVED = 0
        const val SENT = 1
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val layout = when (p1) {
            RECEIVED -> R.layout.list_item_message_received
            SENT -> R.layout.list_item_message_sent
            else -> throw ArrayIndexOutOfBoundsException()
        }
        val listItem = inflater.inflate(layout, p0, false)

        return when (p1) {
            RECEIVED -> ReceivedMessageViewHolder(listItem, p0.context)
            SENT -> SentMessageViewHolder(listItem, p0.context)
            else -> throw ArrayIndexOutOfBoundsException()
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(p0: MessageViewHolder, p1: Int) {
        val message = dataSet[p1]

        p0.bind(message)
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataSet[position]) {
            is ReceivedMessage -> RECEIVED
            is SentMessage -> SENT
            else -> throw IllegalArgumentException()
        }
    }

    abstract class MessageViewHolder(view: View, private val context: Context) :
        ViewHolder(view) {
        private val textViewBody: TextView = view.findViewById(R.id.textViewBody)
        private val textViewCreatedAt: TextView = view.findViewById(R.id.textViewCreatedAt)

        open fun bind(message: Message) {
            textViewBody.text = message.body
            textViewCreatedAt.text = DateUtils.formatDateTime(context, message.createdAt, DateUtils.FORMAT_24HOUR)
        }
    }

    class ReceivedMessageViewHolder(view: View, context: Context) :
        MessageViewHolder(view, context) {
        private val textViewName: TextView = view.findViewById(R.id.textViewName)

        override fun bind(message: Message) {
            super.bind(message)
            if (message is ReceivedMessage)
                textViewName.text = message.sender.name
            else
                throw IllegalArgumentException()
        }
    }

    class SentMessageViewHolder(view: View, context: Context) :
        MessageViewHolder(view, context)
}