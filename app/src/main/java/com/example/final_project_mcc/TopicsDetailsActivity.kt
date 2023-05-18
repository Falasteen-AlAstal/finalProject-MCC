package com.example.final_project_mcc

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_diseased.*
import kotlinx.android.synthetic.main.activity_topics_details.*

class TopicsDetailsActivity : AppCompatActivity() {
    var player: SimpleExoPlayer? = null
    var videoURl = ""
    var playerWhenReady = true
    var currentWindow = 0
    var playBackPosition: Long = 0
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    lateinit var progressDialog: ProgressDialog
    private lateinit var commentArrayList: ArrayList<CommentMoodle>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topics_details)

        auth = Firebase.auth
        db = Firebase.firestore
        val currentUser = auth.currentUser
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("جاري تحميل البيانات")
        progressDialog.setCancelable(false)

        all_comments.layoutManager = LinearLayoutManager(this)
        commentArrayList = arrayListOf<CommentMoodle>()


        val name = intent.getStringExtra("TopicName")
        val description = intent.getStringExtra("TopicDescription")
        val information =intent.getStringExtra("TopicInformation")
        val image = intent.getStringExtra("TopicImage")
        videoURl = intent.getStringExtra("TopicVideo").toString()
        val topicId =  intent.getStringExtra("id")
        getCommentToTopic(topicId.toString())

        topic_name.text = name
        topic_description.text = description
        topic_info.text = information
        Glide.with(this).load(image).into(topic_image)



        send_btn.setOnClickListener {
            val comment=comment_edi.text.toString()
           if (comment.isNotEmpty()){

               addCommentToTopic(topicId.toString() , comment , currentUser!!.uid )
               comment_edi.setText("")
           }

        }



    }

    fun initVideo(){
        player =SimpleExoPlayer.Builder(this).build()
        topic_video.player = player

        val mediaItem = MediaItem.Builder()
            .setUri(videoURl).setMimeType(MimeTypes.APPLICATION_MP4).build()

        val mediaSource = ProgressiveMediaSource.Factory(DefaultDataSource.Factory(this)).createMediaSource(mediaItem)



        player!!.playWhenReady = playerWhenReady
        player!!.seekTo(currentWindow , playBackPosition)
        player!!.prepare(mediaSource , false , false)
    }


    fun releaseVideo(){

        if (player != null){
            playerWhenReady = player!!.playWhenReady
            playBackPosition =player!!.currentPosition
            currentWindow =player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }

    override fun onStart() {
        super.onStart()
        initVideo()
    }

    override fun onStop() {
        super.onStop()
        releaseVideo()
    }

    override fun onPause() {
        super.onPause()
        releaseVideo()
    }




    private fun addCommentToTopic(topicId: String, comment: String, userId: String) {
        val commentsRef = db.collection("comments").document(topicId).collection("comment")

        val newComment = hashMapOf(
            "comment" to comment,
            "userId" to userId,
            "topicId" to topicId
        )

        // جلب اسم المستخدم من جدول المستخدمين
        val userRef = db.collection("users").whereEqualTo("id" , userId)

        userRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val firstName= document.getString("firstName")
                    val middleName= document.getString("middleName")
                    val lastName= document.getString("lastName")
                    val fullName = firstName + " " + middleName + " " + lastName
                    if (fullName != null) {
                        // إضافة اسم المستخدم إلى الـ newComment
                        newComment["fullName"] = fullName
                        commentsRef.add(newComment)
                            .addOnSuccessListener {
                                Toast.makeText(this, "تمت إضافة التعليق بنجاح", Toast.LENGTH_SHORT).show()
                                commentArrayList.clear()
                                getCommentToTopic(topicId)
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "حدث خطأ أثناء إضافة التعليق", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        //
                        Log.e("Read Data", "Username not found in documents")
                    }
                    }

                }


            .addOnFailureListener { e ->
                //
                Log.e("Read Data", "An error occurred while fetching data from the Users table")
            }
    }



    private fun getCommentToTopic(topicId: String) {

        val commentsRef = db.collection("comments")
            .document(topicId)
            .collection("comment")

        commentsRef.whereEqualTo("topicId", topicId)
            .get()
            .addOnSuccessListener { result ->
                progressDialog.dismiss()

                if (result.isEmpty) {
                    // لا توجد تعليقات
                    val emptyMessage = "لا يوجد تعليقات"

                    emptyTextView.text = emptyMessage
                    emptyTextView.visibility = View.VISIBLE
                    all_comments.visibility = View.GONE
                } else {
                    for (document in result) {
                        val comment = document.toObject(CommentMoodle::class.java)
                        commentArrayList.add(comment)
                    }
                    all_comments.adapter = CommentAdapter(this, commentArrayList)
                    emptyTextView.visibility = View.GONE
                    all_comments.visibility = View.VISIBLE
                }

            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "حدث خطأ أثناء عرض التعليقات", Toast.LENGTH_SHORT).show()
            }
    }

}