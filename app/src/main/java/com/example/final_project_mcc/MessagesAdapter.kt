package com.example.final_project_mcc

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*


class MessagesAdapter(
    private val context: Context,
    private val messages: List<Message>,
    private val currentUserUid: String

) : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageDate : TextView= itemView.findViewById(R.id.text_chat_timestamp_me)
        val messageText: TextView = itemView.findViewById(R.id.message_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false)
        return MessageViewHolder(view)
    }
    @SuppressLint("SimpleDateFormat")
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        holder.messageText.text = message.text
        holder.messageDate.text = convertLongToTime(message.timestamp)

        val layoutParams = holder.messageText.layoutParams as
                LinearLayout.LayoutParams
        layoutParams.gravity = if (message.senderId == currentUserUid)
            Gravity.END else Gravity.START
        holder.messageText.layoutParams = layoutParams
        holder.messageDate.layoutParams = layoutParams
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}
