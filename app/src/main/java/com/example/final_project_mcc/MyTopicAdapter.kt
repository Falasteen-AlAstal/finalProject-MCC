package com.example.final_project_mcc

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.card_topic.view.*
import kotlinx.android.synthetic.main.card_topic.view.topic_image
import kotlinx.android.synthetic.main.card_topic.view.topic_name
import kotlinx.android.synthetic.main.cart_mytopic.view.*

class MyTopicAdapter(var activity: Activity, var data:ArrayList<TopicMoodle>): RecyclerView.Adapter<MyTopicAdapter.MyTopics>() {

    private lateinit var db: FirebaseFirestore


    class MyTopics(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.myTopic_name
        val image = itemView.myTopic_image
        val unFollow = itemView.Unfollow




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTopics {

        val root = LayoutInflater.from(activity).inflate(R.layout.cart_mytopic, parent, false)
        db = Firebase.firestore
        return  MyTopics(root)

    }

    override fun onBindViewHolder(holder: MyTopics, position: Int) {

        holder.name.text = data[position].name
        Glide.with(activity).load(data[position].image).into(holder.image)
        holder.itemView.setOnClickListener {

            val i = Intent(activity, TopicsDetailsActivity::class.java)
            i.putExtra("TopicName", data[position].name)
            i.putExtra("TopicDescription", data[position].description)
            i.putExtra("TopicInformation", data[position].information)
            i.putExtra("TopicVideo", data[position].video)
            i.putExtra("TopicImage", data[position].image)

            activity.startActivity(i)


        }
           holder.unFollow.setOnClickListener {


             val topicId = data[position].id
             val userId = Firebase.auth.currentUser?.uid
               db.collection("MyTopics")
               .whereEqualTo("topicId", topicId)
               .whereEqualTo("userId", userId)
               .get()
               .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                document.reference.delete()
                    .addOnSuccessListener {
                        Toast.makeText(
                            activity,
                            "تم إلغاء متابعة هذا الموضوع",
                            Toast.LENGTH_SHORT
                        ).show()

                        // قم بإزالة الموضوع من القائمة المستخدمة في RecyclerView
                        data.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, data.size)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            activity,
                            "فشلت عملية إلغاء متابعة هذا الموضوع",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
               }
           }
    }



    }

    override fun getItemCount(): Int {


        return data.size
    }


}