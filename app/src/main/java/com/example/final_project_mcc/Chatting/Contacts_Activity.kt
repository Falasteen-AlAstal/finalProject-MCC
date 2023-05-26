package com.example.final_project_mcc.Chatting


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.final_project_mcc.ContactAdapter
import com.example.final_project_mcc.ContactModel
import com.example.final_project_mcc.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Contacts_Activity : AppCompatActivity() {
    lateinit var contactRV: RecyclerView
    lateinit var contacts : ArrayList<ContactModel>
    private var db = Firebase.firestore
    var reciverRole: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        Log.e("Kh", "contacts")

        contactRV = findViewById(R.id.contactRecyclerView)
        contactRV.layoutManager = LinearLayoutManager(this)
        contacts = ArrayList()

        var uid = Firebase.auth.currentUser!!.uid

       /* db.collection("users").whereEqualTo("id", uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val role = document.getString("role")
                    if (role != null && role == "doctor") {
                        reciverRole = "Patient"
                    } else if (role != null && role == "Patient") {
                        reciverRole = "doctor"
                    }*/

                    reciverRole = intent.getStringExtra("role")!!


                Log.e("Kh", "${reciverRole}")

                var contactsAdapter = ContactAdapter(this, contacts)

                db.collection("users")
                    .whereEqualTo("role", reciverRole)
                    .get()
                    .addOnSuccessListener {
                        if (!it.isEmpty) {
                            for (data in it.documents) {
                                val user: ContactModel? = data.toObject(ContactModel::class.java)
                                val id = data.getString("id")
                                if (id != uid){
                                    contacts.add(user!!)
                                }

                            }
                            contactsAdapter.notifyDataSetChanged()
                            contactRV.adapter = contactsAdapter
                            Log.e("Kh", "Succeeded")
                        }
                    }
                    .addOnFailureListener {
                        Log.e("Kh", "Failed")
                    }
            }
}




       /* db.collection("users").whereEqualTo("id", uid)
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
                Log.e("Kh", "${reciverRole}")

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
                    }*/



