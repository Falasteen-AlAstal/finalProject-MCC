package com.example.final_project_mcc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_f_g_profile_doctor.First_Nameprof
import kotlinx.android.synthetic.main.fragment_f_g_profile_doctor.date_birthprof
import kotlinx.android.synthetic.main.fragment_f_g_profile_doctor.editText_Emailprof
import kotlinx.android.synthetic.main.fragment_f_g_profile_doctor.editText_Phoneprof
import kotlinx.android.synthetic.main.fragment_f_g_profile_doctor.edit_Text_Addressprof
import kotlinx.android.synthetic.main.fragment_f_g_profile_doctor.last_nameprof
import kotlinx.android.synthetic.main.fragment_f_g_profile_doctor.middle_nameprof

class FGProfileDoctor : Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var DB: FirebaseFirestore
    var updateprofile: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val value2 =  inflater.inflate(R.layout.fragment_f_g_profile_doctor, container, false)
        auth = Firebase.auth
        DB = Firebase.firestore
       val user = auth.currentUser
        getUsreData()
        updateprofile = value2.findViewById<View>(R.id.but_UPDATEPROFILE) as Button
        updateprofile!!.setOnClickListener {
           updateuser(First_Nameprof.text.toString(),
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
        return value2

    }

    private fun getUsreData() {
        DB.collection("users").whereEqualTo("id", auth.currentUser!!.uid).get()
            .addOnSuccessListener { querySnapshot ->
                First_Nameprof.setText(querySnapshot.documents!!.get(0).get("firstName").toString())
                middle_nameprof.setText(querySnapshot.documents!!.get(0).get("middleName").toString())
                last_nameprof.setText(querySnapshot.documents!!.get(0).get("lastName").toString())
                date_birthprof.setText(querySnapshot.documents!!.get(0).get("dateBirth").toString())
                edit_Text_Addressprof.setText(querySnapshot.documents!!.get(0).get("address").toString())
                editText_Emailprof.setText(querySnapshot.documents!!.get(0).get("email").toString())
                editText_Phoneprof.setText(querySnapshot.documents!!.get(0).get("phone").toString())

            }
    }

    private fun updateuser(name: String, mname: String, lname: String, datebirth: String, address: String, email: String, phone: String) {
        val user = HashMap<String, Any>()
        user["firstName"] = name
        user["middleName"] = mname
        user["lastName"] = lname
        user["dateBirth"] = datebirth
        user["address"] = address
        user["email"] = email
        user["phone"] = phone
        //user["image"]=path
        DB.collection("users").whereEqualTo("id", auth.currentUser!!.uid).get()
            .addOnSuccessListener { querySnapshot ->
                DB.collection("users").document(/*Id*/ querySnapshot.documents.get(0).id)
                    .update(user)
                Toast.makeText(context,"تم التعديل", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { exception ->
                Log.e("bb", "" + exception.message)
            }

    }

    }
