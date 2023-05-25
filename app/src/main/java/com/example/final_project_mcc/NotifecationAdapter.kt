package com.example.final_project_mcc

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cart_mytopic.view.Unfollow
import kotlinx.android.synthetic.main.cart_mytopic.view.myTopic_image
import kotlinx.android.synthetic.main.cart_mytopic.view.myTopic_name
import kotlinx.android.synthetic.main.item_notification.view.imgtopicnot
import kotlinx.android.synthetic.main.item_notification.view.tvsickname
import kotlinx.android.synthetic.main.item_notification.view.tvtopicnamenot

class NotifecationAdapter(var activity: Context?, var data:ArrayList<TopicMoodle>): RecyclerView.Adapter<NotifecationAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val namesick = itemView.tvsickname
        val imagest = itemView.imgtopicnot
        val nametopic = itemView.tvtopicnamenot

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifecationAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return NotifecationAdapter.MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val subsc = data[position]

        var imageUri : String? = null
        imageUri = subsc.image
        if (!imageUri.isNullOrEmpty()) {
            Picasso.get().load(imageUri).into(holder.imagest)
        }
       // Picasso.get().load(imageUri).into(holder.imagest)

        holder.namesick.setText("بمرض : ${data[position].name}")
        holder.nametopic.setText("تم الأشتراك بواسطة : ${data[position].name}")

    }
    override fun getItemCount(): Int {


        return data.size
    }
}