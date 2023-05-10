package com.example.final_project_mcc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_diseased.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        db = Firebase.firestore
        auth = Firebase.auth
        val currentUser = auth.currentUser
        Log.e("currentUser" ,"${currentUser!!.uid}")

        getUser()

        but_Save.setOnClickListener {

            updateUser(First_Name.text.toString(), middle_name.text.toString() ,last_name.text.toString() ,
                date_birth.text.toString() ,
                Text_Address.text.toString(),
                Text_Email.text.toString(),
                editText_Phone.text.toString()
            )

        }

    }


    fun getUser(){
        val currentUser = auth.currentUser
        db.collection("users").whereEqualTo("id" , currentUser!!.uid)
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {

                    val firstName = document.getString("firstName")
                    val email = document.getString("email")
                    val middleName = document.getString("middleName")
                    val lastName  = document.getString("lastName")
                    val address  = document.getString("address")
                    val dateBirth  = document.getDate("dateBirth")
                    val dateFormat = SimpleDateFormat("MM/dd/yy")
                    val formattedDate = dateFormat.format(dateBirth)
                    Log.d("Read Data", "${document.id} => ${document.data}")
                    val phone  = document.getString("phone")
                    First_Name.setText(firstName)
                    middle_name.setText(middleName)
                    last_name.setText(lastName)
                    Text_Email.setText(email)
                    Text_Address.setText(address)
                    date_birth.setText(formattedDate)
                    editText_Phone.setText(phone)

                }


            }
            .addOnFailureListener { exception ->
                Log.w("Read Data", "Error getting documents.", exception)
            }


    }



    fun updateUser(firstName: String, middleName: String, lastName: String, dateBirth: String,
                   address : String,
                   email: String,
                   phone :String,)

    {
        val currentUser = auth.currentUser
        val newData = hashMapOf<String, Any>()
            newData["firstName"] = firstName
            newData["middleName"] = middleName
            newData["lastName"] = lastName
            newData ["email"]  = email
            newData["address" ] = address
            newData["dateBirth"]  = dateBirth
            newData ["phone" ] = phone


        db.collection("users").document(currentUser!!.uid)
            .update(newData)
            .addOnSuccessListener {
                Log.e("Update Data", "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.e("Update Data", "Error updating document")

            }
    }


}