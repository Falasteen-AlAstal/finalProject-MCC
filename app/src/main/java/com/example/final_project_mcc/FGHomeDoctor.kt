package com.example.final_project_mcc

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList


class FGHomeDoctor : Fragment() {
    var recview: RecyclerView? = null
    var FB: FloatingActionButton? = null
    var arr: ArrayList<ClassTopic> = ArrayList()
    var db: FirebaseFirestore? = null
    var adapter: AdapterTopic? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var value = inflater.inflate(R.layout.fragment_f_g_home_doctor, container, false)
        recview = value.findViewById<View>(R.id.recyclerView) as RecyclerView
        FB = value.findViewById(R.id.normalFAB) as FloatingActionButton


        recview!!.layoutManager = LinearLayoutManager(context)
        adapter = AdapterTopic(context, arr)
        recview!!.adapter = adapter

        FB!!.setOnClickListener {
            startActivity(Intent(context, AddTopicDoctor::class.java))
        }
        getdata()
        return value
    }

    private fun getdata() {
            db = FirebaseFirestore.getInstance()
            db!!.collection("topic").get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    for(document in queryDocumentSnapshots) {
                        Log.d("ttt", "${document.id} ")
                    }
                    val list = queryDocumentSnapshots.documents
                    for (d in list) {

                        val obj = d.toObject(ClassTopic::class.java)
                        arr!!.add(obj!!)
                    }
                    adapter!!.notifyDataSetChanged()
                }
        }


    }
