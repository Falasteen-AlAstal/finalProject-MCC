package com.example.final_project_mcc

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_update_delete_topic.img_update
import kotlinx.android.synthetic.main.activity_update_delete_topic.update_descTopic
import kotlinx.android.synthetic.main.activity_update_delete_topic.update_information
import kotlinx.android.synthetic.main.activity_update_delete_topic.update_nameTopic
import java.util.ArrayList

class notification : Fragment() {
    var arr: ArrayList<user> = ArrayList()
    var db: FirebaseFirestore? = null
    var adapter: adaptern? = null
    var recview: RecyclerView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       var  va = inflater.inflate(R.layout.fragment_notification, container, false)
        recview = va.findViewById<View>(R.id.recyclerViewn) as RecyclerView
        recview!!.layoutManager = LinearLayoutManager(context)
        adapter = adaptern(context, arr)
        recview!!.adapter = adapter

        getdata()
        return va
    }
    private fun getdata() {
        db = FirebaseFirestore.getInstance()
        db!!.collection("users").whereEqualTo("role","Patient")
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for(document in queryDocumentSnapshots) {
                    Log.d("ttt", "${document.id} ")
                }
                val list = queryDocumentSnapshots.documents
                for (d in list) {

                    val obj = d.toObject(user::class.java)
                    arr!!.add(obj!!)
                }
                adapter!!.notifyDataSetChanged()
            }
    }
//    private fun getData() {
//        db!!.collection("users").whereEqualTo("role","Patient").get()
//            .addOnSuccessListener { querySnapshot ->
//                txt!!.setText(querySnapshot.documents!!.get(0).get("name").toString())
//            }
//    }

}