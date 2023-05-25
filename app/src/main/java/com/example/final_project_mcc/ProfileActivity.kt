package com.example.final_project_mcc

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_diseased.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.First_Name
import kotlinx.android.synthetic.main.activity_profile.Text_Address
import kotlinx.android.synthetic.main.activity_profile.Text_Email
import kotlinx.android.synthetic.main.activity_profile.date_birth
import kotlinx.android.synthetic.main.activity_profile.editText_Phone
import kotlinx.android.synthetic.main.activity_profile.last_name
import kotlinx.android.synthetic.main.activity_profile.middle_name
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.user_profile.*
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    var myCalendar = Calendar.getInstance()
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        db = Firebase.firestore
        auth = Firebase.auth
        analytics = Firebase.analytics
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

        butLogout.setOnClickListener {

            Firebase.auth.signOut()

            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            Toast.makeText(this , "successfully singOut" , Toast.LENGTH_SHORT).show()
        }


        val date =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
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


        screenTrack("ProfileActivity","Profile")

    }


    fun getUser() {
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



    /*fun updateUser(firstName: String, middleName: String, lastName: String, dateBirth: String,
                   address : String,
                   email: String,
                   phone :String)

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
                Log.e("Update Data", "Error updating document" , e)

            }
    }*/

    fun updateUser(firstName: String, middleName: String, lastName: String,
                   dateBirth: String,
                   address: String,
                   email: String,
                   phone: String)

    {
        val dateFormat = SimpleDateFormat("MM/dd/yy")
        val date: Date = dateFormat.parse(dateBirth)

        val currentUser = auth.currentUser
        val newData = hashMapOf<String, Any>()
        newData["firstName"] = firstName
        newData["middleName"] = middleName
        newData["lastName"] = lastName
        newData["email"] = email
        newData["address"] = address
        newData["dateBirth"] = date
        newData["phone"] = phone

            db.collection("users").whereEqualTo("id", currentUser!!.uid).get()
            .addOnSuccessListener { querySnapshot ->
                db.collection("users").document(querySnapshot.documents.get(0).id)
                    .update(newData)
                Toast.makeText(this,"تم التعديل", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { exception ->
                Log.e("bb", "" + exception.message)
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




}