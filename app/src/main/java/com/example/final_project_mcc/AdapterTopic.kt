package com.example.final_project_mcc

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class AdapterTopic (var context: Context? = null ,private var datalist: ArrayList<ClassTopic>): RecyclerView.Adapter<AdapterTopic.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = datalist.get(position)

        var imageUri : String? = null
        imageUri = topic.image
        Picasso.get().load(imageUri).into(holder.img)

        holder.txt_topic.text = topic.name
       holder.itemView.setOnClickListener {
           var intent = Intent(context, UpdateDeleteTopic::class.java)
           intent.putExtra("topic_name", holder.txt_topic.text)
           context!!.startActivity(intent)
       }
    }
    override fun getItemCount(): Int {
        return datalist.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

       val txt_topic : TextView = itemView.findViewById(R.id.tv_name)
        val img : ImageView = itemView.findViewById(R.id.img_topic)

    }
}