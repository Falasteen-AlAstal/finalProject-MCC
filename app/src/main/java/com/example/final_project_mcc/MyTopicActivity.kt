package com.example.final_project_mcc

import android.app.ProgressDialog
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_diseased.*
import kotlinx.android.synthetic.main.activity_my_topic.*
import kotlinx.android.synthetic.main.card_topic.*

class MyTopicActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    lateinit var progressDialog: ProgressDialog
    private lateinit var myTopicArrayList: ArrayList<TopicMoodle>
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_topic)

        db = Firebase.firestore
        auth = Firebase.auth
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("جاري تحميل البيانات")
        progressDialog.setCancelable(false)
        all_myTopic.layoutManager = LinearLayoutManager(this)
        myTopicArrayList = arrayListOf<TopicMoodle>()
        getAllMyTopic()

    }

    fun getAllMyTopic() {
        val userId = auth.currentUser
        progressDialog.show()
        db.collection("MyTopics").whereEqualTo("userId", userId!!.uid)
            .get()
            .addOnSuccessListener { result ->
                progressDialog.dismiss()
                for (document in result) {
                   // val myTopic = document.toObject(TopicMoodle::class.java)
                    val topicId = document.getString("topicId")

                    getMyTopic(topicId.toString())
                    Log.d("Read Data", "${document.id} => ${document.data}")
                }

            }
            .addOnFailureListener { exception ->
                Log.w("Read Data", "Error getting documents.", exception)
            }
    }




    fun getMyTopic(id:String){

        db.collection("topic").whereEqualTo("id" , id)
            .get()
            .addOnSuccessListener { result ->
                progressDialog.dismiss()
                for (document in result) {
                    val topic= document.toObject(TopicMoodle::class.java)
                    myTopicArrayList.add(topic)
                    Log.d("Read Data", "${document.id} => ${document.data}")

                }
                all_myTopic.adapter = MyTopicAdapter(this,myTopicArrayList)


              //  all_myTopic.adapter?.notifyDataSetChanged()

            }
            .addOnFailureListener { exception ->
                Log.w("Read Data", "Error getting documents.", exception)
            }
    }



}