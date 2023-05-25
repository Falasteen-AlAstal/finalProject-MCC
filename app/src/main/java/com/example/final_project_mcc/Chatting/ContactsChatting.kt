package com.example.final_project_mcc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.play.integrity.internal.e
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.util.Calendar

class ContactsChatting : AppCompatActivity() {

    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button

    private lateinit var senderUid: String
    private lateinit var receiverUid: String
    private lateinit var chatId: String
    private lateinit var messagesRef: DatabaseReference

    private lateinit var messagesAdapter: ChatMessageAdapter
    private val messagesList = mutableListOf<ChatMessageModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_chatting)

        receiverUid = intent.getStringExtra("reciverId")!!
        senderUid = Firebase.auth.currentUser!!.uid
        chatId = generateChatId(senderUid, receiverUid)

        messagesRecyclerView = findViewById(R.id.messages_recycler_view)
        messageEditText = findViewById(R.id.message_input)
        sendButton = findViewById(R.id.send_button)

        messagesRef = FirebaseDatabase.getInstance().getReference("chat").child(chatId)

        messagesAdapter = ChatMessageAdapter(
            this, messagesList,
            senderUid
        )
        messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        messagesRecyclerView.adapter = messagesAdapter

        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                messageEditText.setText("")
            }
        }

        messagesRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(ChatMessageModel::class.java)
                if (message != null) {
                    messagesList.add(message)
                    messagesAdapter.notifyItemInserted(messagesList.size - 1)
                    messagesRecyclerView.scrollToPosition(messagesList.size - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun sendMessage(messageText: String) {
        val time = Calendar.getInstance().time
        val timestamp = DateFormat.getTimeInstance(DateFormat.SHORT).format(time)
        val message = ChatMessageModel(messageText, senderUid, receiverUid, timestamp)

        messagesRef.push().setValue(message)
            .addOnSuccessListener {
                Log.e("kh", "Message sent successfully")
            }
            .addOnFailureListener { e ->
                Log.e("kh", "Failed to send message: ${e.message}")
            }
    }

    private fun generateChatId(senderId: String, receiverId: String): String {
        val sortedIds = listOf(senderId, receiverId).sorted()
        return sortedIds.joinToString("_")
    }

}
