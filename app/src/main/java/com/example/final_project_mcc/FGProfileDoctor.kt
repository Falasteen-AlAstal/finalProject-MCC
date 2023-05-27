package com.example.final_project_mcc

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_f_g_profile_doctor.First_Nameprof
import kotlinx.android.synthetic.main.fragment_f_g_profile_doctor.date_birthprof
import kotlinx.android.synthetic.main.fragment_f_g_profile_doctor.editText_Emailprof
import kotlinx.android.synthetic.main.fragment_f_g_profile_doctor.editText_Phoneprof
import kotlinx.android.synthetic.main.fragment_f_g_profile_doctor.edit_Text_Addressprof
import kotlinx.android.synthetic.main.fragment_f_g_profile_doctor.last_nameprof
import kotlinx.android.synthetic.main.fragment_f_g_profile_doctor.middle_nameprof
import java.text.SimpleDateFormat
import java.util.*

class FGProfileDoctor : Fragment() {

    lateinit var auth: FirebaseAuth
    lateinit var DB: FirebaseFirestore
    var updateprofile: Button? = null
    var butLogoutprof:Button? =null
    var myCalendar = Calendar.getInstance()
    private val dateOfBirthListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        val selectedDate = Calendar.getInstance()
        selectedDate.set(Calendar.YEAR, year)
        selectedDate.set(Calendar.MONTH, month)
        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val dateFormat = SimpleDateFormat("MM/dd/yy")
        val formattedDate = dateFormat.format(selectedDate.time)

        date_birthprof.setText(formattedDate)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val value2 =  inflater.inflate(R.layout.fragment_f_g_profile_doctor, container, false)
        auth = Firebase.auth
        DB = Firebase.firestore
        val user = auth.currentUser
        //date_birth = value2.findViewById(R.id.date_birthprof)
        val dateOfBirthProf = value2.findViewById<TextView>(R.id.date_birthprof)
        getUser()
        updateprofile = value2.findViewById<View>(R.id.but_UPDATEPROFILE) as Button
        butLogoutprof =  value2.findViewById<View>(R.id.butLogoutprof) as Button
        updateprofile!!.setOnClickListener {
            updateUser(
                First_Nameprof.text.toString(),
                middle_nameprof.text.toString(),
                last_nameprof.text.toString(),
                date_birthprof.text.toString(),
                edit_Text_Addressprof.text.toString(),
                editText_Emailprof.text.toString(),
                editText_Phoneprof.text.toString()
            )
            val email = editText_Emailprof.text.toString()
            user!!.updateEmail(email).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(context,"تم التعديل الايميل", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(context,"فشل التعديل", Toast.LENGTH_LONG).show()
                }
            }
        }

        butLogoutprof!!.setOnClickListener {
            Firebase.auth.signOut()

            val i = Intent(context, LoginActivity::class.java)
            startActivity(i)
            Toast.makeText(context , "successfully singOut" , Toast.LENGTH_SHORT).show()
        }



        val date =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = month
                myCalendar[Calendar.DAY_OF_MONTH] = day
                updateLabel()
            }

        dateOfBirthProf.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    date,
                    myCalendar[Calendar.YEAR],
                    myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH]
                ).show()
            }
        }
        return value2
    }
    fun getUser() {
        val currentUser = auth.currentUser
        DB.collection("users").whereEqualTo("id" , currentUser!!.uid)
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
                    First_Nameprof.setText(firstName)
                    middle_nameprof.setText(middleName)
                    last_nameprof.setText(lastName)
                    editText_Emailprof.setText(email)
                    edit_Text_Addressprof.setText(address)
                    date_birthprof.setText(formattedDate)
                    editText_Phoneprof.setText(phone)

                }


            }
            .addOnFailureListener { exception ->
                Log.w("Read Data", "Error getting documents.", exception)
            }


    }



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

        DB.collection("users").whereEqualTo("id", currentUser!!.uid).get()
            .addOnSuccessListener { querySnapshot ->
                DB.collection("users").document(querySnapshot.documents.get(0).id)
                    .update(newData)
                Toast.makeText(context,"تم التعديل", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { exception ->
                Log.e("bb", "" + exception.message)
            }
    }
    /*private fun getUsreData() {
        DB.collection("users").whereEqualTo("id", auth.currentUser!!.uid).get()
            .addOnSuccessListener { querySnapshot ->

                First_Nameprof.setText(querySnapshot.documents!!.get(0).get("firstName").toString())
                middle_nameprof.setText(querySnapshot.documents!!.get(0).get("middleName").toString())
                last_nameprof.setText(querySnapshot.documents!!.get(0).get("lastName").toString())
                val dateBirth= querySnapshot.documents!!.get(0).get("dateBirth")
                val dateFormat = SimpleDateFormat("MM/dd/yy")
                val formattedDate = dateFormat.format(dateBirth)
                date_birthprof.setText(formattedDate)
                edit_Text_Addressprof.setText(querySnapshot.documents!!.get(0).get("address").toString())
                editText_Emailprof.setText(querySnapshot.documents!!.get(0).get("email").toString())
                editText_Phoneprof.setText(querySnapshot.documents!!.get(0).get("phone").toString())
            }
    }

    private fun updateuser(name: String, mname: String, lname: String, dateBirth: String, address: String, email: String, phone: String) {
        val dateFormat = SimpleDateFormat("MM/dd/yy")
        val date: Date = dateFormat.parse(dateBirth)
        val user = hashMapOf<String, Any>()
        user["firstName"] = name
        user["middleName"] = mname
        user["lastName"] = lname
        user["dateBirth"] = date
        user["address"] = address
        user["email"] = email
        user["phone"] = phone

        DB.collection("users").whereEqualTo("id", auth.currentUser!!.uid).get()
            .addOnSuccessListener { querySnapshot ->
                DB.collection("users").document(querySnapshot.documents.get(0).id)
                    .update(user)
                Toast.makeText(context,"تم التعديل", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { exception ->
                Log.e("bb", "" + exception.message)
            }
    }*/

    private fun updateLabel() {
        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        date_birthprof.setText(dateFormat.format(myCalendar.getTime()))
    }

    }
