package com.example.final_project_mcc

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.card_topic.view.*
import kotlinx.android.synthetic.main.comment_item.view.*

class CommentAdapter (var activity: Activity, var data:ArrayList<CommentMoodle>): RecyclerView.Adapter<CommentAdapter.MyComment>() {

    class MyComment(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val fullName = itemView.user_name
        val Comment =itemView.user_comment




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyComment {
        val root = LayoutInflater.from(activity).inflate(R.layout.comment_item, parent, false)

        return MyComment(root)
    }

    override fun onBindViewHolder(holder: MyComment, position: Int) {
        holder.fullName.text = data[position].fullName
        holder.Comment.text = data[position].comment
    }

    override fun getItemCount(): Int {
        return data.size
    }


}