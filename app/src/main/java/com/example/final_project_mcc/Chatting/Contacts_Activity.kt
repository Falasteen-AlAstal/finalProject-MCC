package com.example.final_project_mcc.Chatting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Contacts_Activity : AppCompatActivity() {
    lateinit var contactRV: RecyclerView
    lateinit var contacts : ArrayList<ContactModel>
    private var db = Firebase.firestore
    lateinit var reciverRole: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        Log.e("Kh", "contacts")

        contactRV = findViewById(R.id.contactRecyclerView)
        contactRV.layoutManager = LinearLayoutManager(this)
        contacts = ArrayList()

        var uid = Firebase.auth.currentUser!!.uid
        db.collection("users").whereEqualTo("id", uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val role = document.getString("role")
                    if (role != null && role == "doctor") {
                        reciverRole = "Patient"
                    } else if (role != null && role == "Patient") {
                        reciverRole = "doctor"
                    }
                }
            }

                var contactsAdabter = ContactAdapter(this, contacts)

                db.collection("users")
                    .whereEqualTo("role", reciverRole)
                    .get()
                    .addOnSuccessListener {
                        if (!it.isEmpty) {
                            for (data in it.documents) {
                                val user: ContactModel? = data.toObject(ContactModel::class.java)
                                /*    val userName = hashMapOf(
                            data.get("firstName").toString() to "firstName",
                            data.get("middleName").toString() to "middleName",
                            data.get("lastName").toString() to "latsName"
                        )
                        contacts.add(userName as ContactModel)*/

                                contacts.add(user!!)
                            }
                            contactRV.adapter = contactsAdabter
                            Log.e("Kh", "Succeeded")
                        }
                    }.addOnFailureListener {
                        Log.e("Kh", "Fialed")
                    }


            }
    }