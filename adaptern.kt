package com.example.final_project_mcc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class adaptern (var context: Context? = null, private var datalist: ArrayList<user>): RecyclerView.Adapter<adaptern.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_role, parent, false)
        return MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val topic = datalist.get(position)
        holder.txt_topic.text = topic.firstName
    }
    override fun getItemCount(): Int {
        return datalist.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val txt_topic : TextView = itemView.findViewById(R.id.tv_n)

    }
}