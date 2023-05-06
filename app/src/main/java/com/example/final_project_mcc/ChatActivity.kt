package com.example.final_project_mcc

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project_mcc.MessagesAdapter
import com.example.final_project_mcc.R
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference

class ChatActivity : AppCompatActivity() {

    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button

    private lateinit var senderUid: String
    private lateinit var receiverUid: String
    private lateinit var messagesRef: DatabaseReference

    private lateinit var messagesAdapter: MessagesAdapter
    private val messagesList = mutableListOf<Message>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        receiverUid = "UTw20GLq3Wd4fHzBHCjoFKWDN2i1"
        senderUid = "LvzshbxSTBe6Dh3PCuiRE9uDyPs2"

        messagesRecyclerView = findViewById(R.id.messages_recycler_view)
        messageEditText = findViewById(R.id.message_input)
        sendButton = findViewById(R.id.send_button)

        messagesRef = FirebaseDatabase.getInstance().getReference("chat")

        messagesAdapter = MessagesAdapter(this, messagesList,senderUid)
        messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        messagesRecyclerView.adapter = messagesAdapter

        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString().trim()
            var timestamp = System.currentTimeMillis()

            Log.e("Hanan",messageText.toString())
            if (messageText.isNotEmpty()) {
                sendMessage(messageText,timestamp)
                messageEditText.setText("")
            }
        }

        messagesRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    messagesList.add(message)

                    messagesAdapter.
                    notifyItemInserted(messagesList.size - 1)

                    messagesRecyclerView.
                    scrollToPosition(messagesList.size - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("Hanan", "onDataChange: Time snapshot: " + snapshot);
            }


            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                System.err.println("Listener was cancelled");
            }
        })
    }

    private fun sendMessage(messageText: String, timestamp: Long) {
        val message = Message(messageText, senderUid, receiverUid, timestamp)
        Log.e("Hanan","1")

        FirebaseDatabase.getInstance().reference.child("chat").push().setValue(message)

        Log.e("Hanan","2")



    }
}

