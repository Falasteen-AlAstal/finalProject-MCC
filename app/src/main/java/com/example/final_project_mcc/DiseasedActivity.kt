package com.example.final_project_mcc

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_diseased.*

class  DiseasedActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    lateinit var progressDialog: ProgressDialog
    private lateinit var topicArrayList: ArrayList<TopicMoodle>
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diseased)

        db = Firebase.firestore
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("جاري تحميل البيانات")
        progressDialog.setCancelable(false)
        all_Topic.layoutManager = LinearLayoutManager(this)
        topicArrayList = arrayListOf<TopicMoodle>()
        getAllTopic()


        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_Navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.topic -> {
                    val intent = Intent(this, DiseasedActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.my_topics -> {
                  //  val intent = Intent(this, MyTopicsActivity::class.java)
                 //   startActivity(intent)
                    true
                }
                R.id.chat -> {
                   val intent = Intent(this, ChatActivity::class.java)
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

}