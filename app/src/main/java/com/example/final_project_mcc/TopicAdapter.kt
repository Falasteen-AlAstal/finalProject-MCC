package com.example.final_project_mcc

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.card_topic.view.*

class TopicAdapter (var activity: Activity, var data:ArrayList<TopicMoodle>): RecyclerView.Adapter<TopicAdapter.MyTopic>() {


    class MyTopic(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.topic_name
        val imageTopic = itemView.topic_image


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTopic {
        val root = LayoutInflater.from(activity).inflate(R.layout.card_topic, parent, false)
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

    }

        override fun getItemCount(): Int {
            return data.size
        }
    }

