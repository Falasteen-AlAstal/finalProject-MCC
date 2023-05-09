package com.example.final_project_mcc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private  lateinit var role : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        role = intent.getStringExtra("role").toString()
        auth = Firebase.auth
        db = Firebase.firestore

        but_Register.setOnClickListener {

            if (editText_Email.text.toString().isNotEmpty()
                && editText_Password.text.toString().isNotEmpty()
                && confirmation_Password.text.toString().isNotEmpty()
                && First_Name.text.toString().isNotEmpty()
                && middle_name.text.toString().isNotEmpty()
                && last_name.text.toString().isNotEmpty()
                && editText_Phone.text.toString().isNotEmpty()
                && edit_Text_Address.text.toString().isNotEmpty())

            {

                createNewAccount(editText_Email.text.toString() , editText_Password.text.toString())

            }else{

                Toast.makeText(this , "يرجى إدخال جميع البيانات أولا" , Toast.LENGTH_SHORT).show()

            }
        }

        text_Login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }




    }



    private fun createNewAccount(email: String , password: String){

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->

            if (task.isSuccessful){
                val  user = auth.currentUser
                Log.d("Falasteen Auth", "createUserWithEmail:success")
                Toast.makeText(this , "نجح التسجيل" , Toast.LENGTH_SHORT).show()
                addUserToDB(
                    user!!.uid ,
                    First_Name.text.toString(),
                    middle_name.text.toString(),
                    last_name.text.toString(),
                    date_birth.text.toString(),
                    edit_Text_Address.text.toString(),
                    editText_Email.text.toString(),
                    editText_Phone.text.toString(),
                    editText_Password.text.toString(),
                    role
                )

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

            }else{
                Log.d("Falasteen Auth", "createUserWithEmail:failure")
                Toast.makeText(this , "فشل التسجيل" , Toast.LENGTH_SHORT).show()
            }

        }
    }



    private fun addUserToDB(id : String, firstName: String, middleName: String, lastName: String, dateBirth: String,
                            address : String,
                            email: String,
                            phone :String,
                            password:String,
                            role: String) {

        val user = hashMapOf("id" to id ,
            "firstName" to firstName,
            "middleName" to middleName,
            "lastName" to lastName,
            "dateBirth" to dateBirth,
            "address" to address,
            "email" to email ,
            "phone" to phone ,
            "password" to password,
            "role" to role)

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                //Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                Log.e("add user", "Success")
            }
            .addOnFailureListener { exception ->
                Log.e("Error add user", exception.message!!)
            }
    }







}