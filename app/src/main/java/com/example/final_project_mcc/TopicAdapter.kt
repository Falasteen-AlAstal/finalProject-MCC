package com.example.final_project_mcc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global.putString
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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.card_topic.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class TopicAdapter (var activity: Activity, var data:ArrayList<TopicMoodle>): RecyclerView.Adapter<TopicAdapter.MyTopic>() {
    private lateinit var db: FirebaseFirestore
    private lateinit var analytics: FirebaseAnalytics
    var follow : Boolean = false


    class MyTopic(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.topic_name
        val imageTopic = itemView.topic_image
        val follow = itemView.follow



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTopic {
        val root = LayoutInflater.from(activity).inflate(R.layout.card_topic, parent, false)
        db = Firebase.firestore
        analytics = Firebase.analytics
        return MyTopic(root)
    }

    override fun onBindViewHolder(holder: MyTopic, position: Int) {
        holder.name.text = data[position].name
        Glide.with(activity).load(data[position].image).into(holder.imageTopic)
        holder.itemView.setOnClickListener {
            val userId = Firebase.auth.currentUser?.uid
            val i = Intent(activity, TopicsDetailsActivity::class.java)
            i.putExtra("TopicName", data[position].name)
            i.putExtra("TopicDescription", data[position].description)
            i.putExtra("TopicInformation", data[position].information)
            i.putExtra("TopicVideo", data[position].video)
            i.putExtra("TopicImage", data[position].image)
            i.putExtra("id" ,data[position].id)
            logSelectContentEvent(data[position].id , data[position].name , "Topics" )
         //   addCommentToTopic(data[position].id ,i.putExtra("comment" , data[position].comment) ,

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

           FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
               if(!task.isSuccessful()){
                   Log.e("bal","faild to get token")
                   return@addOnCompleteListener
               }
               val token = task.result.toString()
               Log.e("bal",token)
               sendFCMMessage(token,"أصبحت تتابع هذا الموضوع")


           }



       }

       }


        override fun getItemCount(): Int {
            return data.size
        }


    fun sendFCMMessage(token: String,text:String) { val serverKey = "AAAAta51XPQ:APA91bEMAngR-swKm3a8CQS2XwkkyH46d074WQcw8tb0jVYJ_ExIp6iGWCcZ6gTcTW750btufb4HmHGjYyc6AEgKOaCbMXNllJYxyrXwhavsbppBs-kuObDr-3xl2oxZgOZ-Dn2MyMXj"
        val url = URL("https://fcm.googleapis.com/fcm/send")
        GlobalScope.launch {
            val connection = url.openConnection() as HttpURLConnection
            Log.e("bal","in GlobalScope")

            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.doInput = true
            connection.addRequestProperty("Content-Type", "application/json")
            connection.addRequestProperty("Authorization", "key=$serverKey")

            val message = JSONObject()
            val data = JSONObject()
            data.put("message", text)
            message.put("to", token)
            message.put("data", data)

            val messageBytes = message.toString().toByteArray(Charsets.UTF_8)
            connection.setFixedLengthStreamingMode(messageBytes.size)

            connection.connect()

            val outputStream: OutputStream = BufferedOutputStream(connection.outputStream)
            outputStream.write(messageBytes)
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Message sent successfully
                Log.e("bal","resopns is successfully ok")
            } else {
                // Handle failure
                Log.e("bal","respons failed")
            }
            connection.disconnect()
        }
    }


    private fun logSelectContentEvent(itemId: String, itemName: String, contentType: String) {
        val params = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
            putString(FirebaseAnalytics.Param.ITEM_NAME, itemName)
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
        }
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params)
    }











}

