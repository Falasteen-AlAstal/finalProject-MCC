package com.example.final_project_mcc

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Color.GREEN
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.card_topic.view.*

class TopicAdapter (var activity: Activity, var data:ArrayList<TopicMoodle>): RecyclerView.Adapter<TopicAdapter.MyTopic>() {
    private lateinit var db: FirebaseFirestore
    var follow : Boolean = false


    class MyTopic(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.topic_name
        val imageTopic = itemView.topic_image
        val follow = itemView.follow



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTopic {
        val root = LayoutInflater.from(activity).inflate(R.layout.card_topic, parent, false)
        db = Firebase.firestore
        return MyTopic(root)
    }

    override fun onBindViewHolder(holder: MyTopic, position: Int) {
        holder.name.text = data[position].name
        Glide.with(activity).load(data[position].image).into(holder.imageTopic)
        holder.itemView.setOnClickListener {

            val i = Intent(activity, TopicsDetailsActivity::class.java)
            i.putExtra("TopicName", data[position].name)
            i.putExtra("TopicDescription", data[position].description)
            i.putExtra("TopicInformation", data[position].information)
            i.putExtra("TopicVideo", data[position].video)
            i.putExtra("TopicImage", data[position].image)

            activity.startActivity(i)
        }




       holder.follow.setOnClickListener {
           val topicId = data[position].id
           val userId = Firebase.auth.currentUser?.uid
           val myTopic = hashMapOf(
               "topicId" to topicId,
               "userId" to userId,
               "isFollow" to true
           )

           val myTopicRef = db.collection("MyTopics").document()
           db.collection("MyTopics")
               .whereEqualTo("topicId", topicId)
               .whereEqualTo("userId", userId)
               .get()
               .addOnSuccessListener { querySnapshot ->
                   if (querySnapshot.isEmpty) {
                       myTopicRef.set(myTopic)
                           .addOnSuccessListener {
                               Toast.makeText(activity, "تم متابعة هذا الموضوع بنجاح", Toast.LENGTH_SHORT).show()
                           }
                           .addOnFailureListener { e ->
                               Toast.makeText(activity, "فشلت عملية المتابعة لهذا الموضوع", Toast.LENGTH_SHORT).show()
                           }
                   } else {

                       Toast.makeText(activity, "الموضوع متابع بالفعل", Toast.LENGTH_SHORT).show()
                   }
               }
               .addOnFailureListener { e ->
                   Toast.makeText(activity, "حدث خطأ أثناء التحقق من الموضوع", Toast.LENGTH_SHORT).show()
               }
       }

       }


        override fun getItemCount(): Int {
            return data.size
        }










}

