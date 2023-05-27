package com.example.final_project_mcc


import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ContactAdapter(var activity: Activity, var contactsList:ArrayList<ContactModel>) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView =itemView.findViewById(R.id.contactName)
        val img:ImageView = itemView.findViewById(R.id.contactImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        var view = LayoutInflater.from(activity).inflate(R.layout.contact_list_item,parent,false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.name.text = contactsList[position].firstName.plus(" ").plus(contactsList[position].middleName).plus(" ")
            .plus(contactsList[position].lastName) //latsName
        holder.itemView.setOnClickListener {
            var i = Intent(activity,ContactsChatting::class.java)
            i.putExtra("reciverId",contactsList[position].id)
            activity.startActivity(i)
        }
    }
}