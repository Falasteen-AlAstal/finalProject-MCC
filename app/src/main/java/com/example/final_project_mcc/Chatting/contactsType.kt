package com.example.final_project_mcc.Chatting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.final_project_mcc.R

class contactsType : AppCompatActivity() {
    lateinit var doctors: Button
    lateinit var patients: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_type)


        doctors = findViewById(R.id.doctors)
        patients = findViewById(R.id.patients)

        doctors.setOnClickListener {
            var i = Intent(this,Contacts_Activity::class.java)
            i.putExtra("role","doctor")
            startActivity(i)
        }
        patients.setOnClickListener {
            var i = Intent(this,Contacts_Activity::class.java)
            i.putExtra("role","Patient")
            startActivity(i)
        }
    }
}