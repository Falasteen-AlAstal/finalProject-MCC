package com.example.final_project_mcc

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private  lateinit var role : String


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            updateUI()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        db = Firebase.firestore
        role = ""
        but_Login.setOnClickListener {

            if (editEmail.text.toString().isNotEmpty() && editPassword.text.toString().isNotEmpty()
                && (role == "doctor" || role == "Patient")){
                signInAccount(editEmail.text.toString() , editPassword.text.toString())

            }else{

                Toast.makeText(this , "يرجى إدخال  البيانات المطلوبة والتأكد من اختيار نوع الحساب" , Toast.LENGTH_SHORT).show()
            }


        }

        card_doctor.setOnClickListener {
            role = "doctor"
            card_doctor.setCardBackgroundColor(Color.GREEN)
            card_Patient.setCardBackgroundColor(Color.WHITE)
        }

        card_Patient.setOnClickListener {
            role = "Patient"
            card_doctor.setCardBackgroundColor(Color.WHITE)
            card_Patient.setCardBackgroundColor(Color.GREEN)
        }

        text_Register.setOnClickListener {
            if (role == "doctor" || role == "Patient"){
                val i = Intent(this, RegisterActivity::class.java)
                i.putExtra("role", role)
                startActivity(i)
            }else{
                Toast.makeText(this, "يجب اختيار نوع الحساب أولا",
                    Toast.LENGTH_SHORT).show()
            }

        }




    }

    private fun signInAccount(email: String , password: String){

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val currentUser = auth.currentUser
                      db.collection("users").whereEqualTo("id" , currentUser!!.uid)
                            .get()
                          .addOnSuccessListener { result ->
                              for (document in result) {
                                  Log.e("data", "${document.id} => ${document.data}")
                                  Log.e("role" , "${document.getString("role")}")
                                  val role= document.getString("role")
                                  if (role != null && role == "doctor") {
                                      Toast.makeText(this , " تم عملية تسجيل الدخول بنجاح" , Toast.LENGTH_SHORT).show()
                                      val intent = Intent(this, DoctorActivity::class.java)
                                      startActivity(intent)
                                  } else if (role != null && role == "Patient") {
                                      Toast.makeText(this , " تم عملية تسجيل الدخول بنجاح" , Toast.LENGTH_SHORT).show()
                                      val intent = Intent(this, DiseasedActivity::class.java)
                                      startActivity(intent)
                                  }
                              }
                          }
                            .addOnFailureListener { exception ->
                                Log.e("Falasteen Error", "Error getting document: ", exception)
                            }

                } else {
                    Log.e("Falasteen Error", "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "فشلت عملية تسجبل الدخول",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }




    private fun updateUI(){
        val currentUser = auth.currentUser
        db.collection("users").whereEqualTo("id" , currentUser!!.uid)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val role= document.getString("role")
                    if (role != null && role == "doctor") {
                        val intent = Intent(this, DoctorActivity::class.java)
                        startActivity(intent)
                    } else if (role != null && role == "Patient") {
                        val intent = Intent(this, DiseasedActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Falasteen Error", "Error getting document: ", exception)
            }
    }


}