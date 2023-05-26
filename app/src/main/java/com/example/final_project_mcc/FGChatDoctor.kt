package com.example.final_project_mcc

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.final_project_mcc.Chatting.Contacts_Activity

class FGChatDoctor : Fragment() {

    lateinit var doctors: Button
    lateinit var patients: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_contacts_type, container, false)

        doctors = view.findViewById(R.id.doctors)
        patients = view.findViewById(R.id.patients)

        doctors.setOnClickListener {
            val i = Intent(activity, Contacts_Activity::class.java)
            i.putExtra("role", "doctor")
            startActivity(i)
        }
        patients.setOnClickListener {
            val i = Intent(activity, Contacts_Activity::class.java)
            i.putExtra("role", "Patient")
            startActivity(i)
        }

        return view
    }





}