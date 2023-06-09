package com.example.final_project_mcc


import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.final_project_mcc.Chatting.Contacts_Activity
import com.example.final_project_mcc.Chatting.contactsType

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_diseased.*

class  DiseasedActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    lateinit var progressDialog: ProgressDialog
    private lateinit var topicArrayList: ArrayList<TopicMoodle>
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diseased)



        db = Firebase.firestore
        analytics = Firebase.analytics
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("جاري تحميل البيانات")
        progressDialog.setCancelable(false)
        all_Topic.layoutManager = LinearLayoutManager(this)
        topicArrayList = arrayListOf<TopicMoodle>()
        getAllTopic()
        screenTrack("DiseasedActivity" ,"Home")

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_Navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.topic -> {
                    val intent = Intent(this, DiseasedActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.my_topics -> {
                    val intent = Intent(this, MyTopicActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.chat -> {
                   val intent = Intent(this, contactsType  ::class.java)
                    startActivity(intent)
                    true
                }
                R.id.profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }


        }


        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                searchTopic(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    topicArrayList.clear()
                    getAllTopic()
                }
                return false
            }
        })
    }

    fun getAllTopic(){

        progressDialog.show()
        db.collection("topic")
            .get()
            .addOnSuccessListener { result ->
                progressDialog.dismiss()
                for (document in result) {
                    val topic= document.toObject(TopicMoodle::class.java)
                    topicArrayList.add(topic)
                    Log.d("Read Data", "${document.id} => ${document.data}")



                }

                all_Topic.adapter = TopicAdapter(this,topicArrayList)

            }
            .addOnFailureListener { exception ->
                Log.w("Read Data", "Error getting documents.", exception)
            }


    }


    private fun searchTopic(query: String) {
        progressDialog.show()
        topicArrayList.clear()

        db.collection("topic")
            .whereEqualTo("name", query)
            .get()
            .addOnSuccessListener { result ->
                progressDialog.dismiss()
                for (document in result) {
                    val topic = document.toObject(TopicMoodle::class.java)
                    topicArrayList.add(topic)
                    Log.d("Read Data", "${document.id} => ${document.data}")
                }

                all_Topic.adapter = TopicAdapter(this, topicArrayList)

                logSearchEvent(query)
            }
            .addOnFailureListener { exception ->
                progressDialog.dismiss()
                Log.w("Read Data", "Error getting documents.", exception)
            }
    }


    fun screenTrack (screenClass:String , screenName: String){

        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW){

            param(FirebaseAnalytics.Param.SCREEN_CLASS , screenClass)
            param(FirebaseAnalytics.Param.SCREEN_NAME , screenName)

        }
    }

    private fun logSearchEvent(query: String) {
        val params = Bundle().apply {
            putString(FirebaseAnalytics.Param.SEARCH_TERM, query)
        }
        analytics.logEvent(FirebaseAnalytics.Event.SEARCH, params)
    }


}

