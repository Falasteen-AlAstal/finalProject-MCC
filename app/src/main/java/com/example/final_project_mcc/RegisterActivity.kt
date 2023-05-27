package com.example.final_project_mcc

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private  lateinit var role : String
    var myCalendar = Calendar.getInstance()
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        role = intent.getStringExtra("role").toString()
        auth = Firebase.auth
        db = Firebase.firestore
        analytics = Firebase.analytics

        but_Register.setOnClickListener {

            if (editText_Email.text.toString().isNotEmpty()
                && editText_Password.text.toString().isNotEmpty()
                && confirmation_Password.text.toString().isNotEmpty()
                && First_Name.text.toString().isNotEmpty()
                && middle_name.text.toString().isNotEmpty()
                && last_name.text.toString().isNotEmpty()
                && editText_Phone.text.toString().isNotEmpty()
                && edit_Text_Address.text.toString().isNotEmpty()
                && date_birth.text.toString().isNotEmpty())

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


        val date =
            OnDateSetListener { view, year, month, day ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = month
                myCalendar[Calendar.DAY_OF_MONTH] = day
                updateLabel()
            }

        date_birth.setOnClickListener {
                DatePickerDialog(
                    this,
                    date,
                    myCalendar[Calendar.YEAR],
                    myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH]
                ).show()
            }
        screenTrack("RegisterActivity" , "Register")




    }



  /*  private fun createNewAccount(email: String , password: String){

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

                logSignUpEvent()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

            }else{
                Log.d("Falasteen Auth", "createUserWithEmail:failure")
                Toast.makeText(this , "فشل التسجيل" , Toast.LENGTH_SHORT).show()
            }

        }
    }*/

    private fun createNewAccount(email: String, password: String) {
        val confirmPassword = confirmation_Password.text.toString()

        if (password == confirmPassword) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("Falasteen Auth", "createUserWithEmail:success")
                    Toast.makeText(this, "نجح التسجيل", Toast.LENGTH_SHORT).show()
                    addUserToDB(
                        user!!.uid,
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

                    logSignUpEvent()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d("Falasteen Auth", "createUserWithEmail:failure")
                    Toast.makeText(this, "فشل التسجيل", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "كلمة المرور وتأكيد كلمة المرور غير متطابقتين", Toast.LENGTH_SHORT).show()
        }
    }


    private fun addUserToDB(id : String, firstName: String, middleName: String, lastName: String, dateBirth: String,
                            address : String,
                            email: String,
                            phone :String,
                            password:String,
                            role: String) {


        val dateFormat = SimpleDateFormat("MM/dd/yy")
        val date: Date = dateFormat.parse(dateBirth)

        val user = hashMapOf("id" to id ,
            "firstName" to firstName,
            "middleName" to middleName,
            "lastName" to lastName,
            "dateBirth" to date,
            "address" to address,
            "email" to email ,
            "phone" to phone ,
            "password" to password,
            "role" to role,
            )

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

    private fun updateLabel() {
        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        date_birth.setText(dateFormat.format(myCalendar.getTime()))
    }


    fun screenTrack(screenClass:String , screenName: String){

        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW){

            param(FirebaseAnalytics.Param.SCREEN_CLASS , screenClass)
            param(FirebaseAnalytics.Param.SCREEN_NAME , screenName)

        }
    }


    private fun logSignUpEvent() {
        analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, null)
    }










}