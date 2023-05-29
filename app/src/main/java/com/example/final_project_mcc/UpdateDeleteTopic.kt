package com.example.final_project_mcc

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.get
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import kotlinx.android.synthetic.main.activity_update_delete_topic.btn_Delete
import kotlinx.android.synthetic.main.activity_update_delete_topic.btn_update
import kotlinx.android.synthetic.main.activity_update_delete_topic.update_descTopic
import kotlinx.android.synthetic.main.activity_update_delete_topic.update_information
import kotlinx.android.synthetic.main.activity_update_delete_topic.update_nameTopic
import java.util.UUID

class UpdateDeleteTopic : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var path1: String
    lateinit var DB: FirebaseFirestore
    lateinit var storage: FirebaseStorage
    private lateinit var referance: StorageReference
    var player: SimpleExoPlayer? = null
    var palyerRead = true
    var currentwindo = 0
    var playerpostion:Long = 0
 //   lateinit var path1video: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_delete_topic)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        auth = Firebase.auth
        DB = Firebase.firestore
        storage = Firebase.storage
        referance = storage.reference

        //path1video = ""

        getData()

        btn_update.setOnClickListener {
            updateTopic(update_nameTopic.text.toString(),update_descTopic.text.toString(),update_information.text.toString())
        }

//        img_update.setOnClickListener {
//            PickImageDialog.build(PickSetup()).show(this)
//        }

        btn_Delete.setOnClickListener{
            var tv_namer=intent!!.getStringExtra("topic_name")
            DB!!.collection("topic").whereEqualTo("name",tv_namer).get()
                .addOnSuccessListener { querySnapshot ->
                    DB!!.collection("topic").document(querySnapshot.documents.get(0).id)
                        .delete()
                    Toast.makeText(this, "تم الحذف  ", Toast.LENGTH_LONG).show()

//                    var i=Intent(this,FGHomeDoctor::class.java)
//                    applicationContext.startActivity(i)

                }.addOnFailureListener { exception ->
                    Toast.makeText(this, "لم يتم الحذف  ", Toast.LENGTH_LONG).show()
                }
        }

    }
    private fun getData() {
        var tv_namer=intent!!.getStringExtra("topic_name")
        DB.collection("topic").whereEqualTo("name",tv_namer).get()
            .addOnSuccessListener { querySnapshot ->
                update_nameTopic.setText(querySnapshot.documents!!.get(0).get("name").toString())
                update_descTopic.setText(querySnapshot.documents!!.get(0).get("description").toString())
                update_information.setText(querySnapshot.documents.get(0).get("information").toString())
//                Picasso.get().load(
//                    querySnapshot.documents.get(0).get("image")
//                        .toString()
//                ).into(img_update)

               // initVideo()
            }
    }

    private fun updateTopic(name: String, description: String , information : String) {
        val user = HashMap<String, Any>()
        user["name"] = name
        user["description"] = description
        user["information"] = information
      //  user["image"] = path1
        //user["video"] = path1video
        DB.collection("topic").whereEqualTo("id",auth.currentUser!!.uid).get()
            .addOnSuccessListener { querySnapshot ->
                DB.collection("topic").document( querySnapshot.documents.get(0).id)
                    .update(user)
                Toast.makeText(this, "تم التعديل  ", Toast.LENGTH_LONG).show()

            }
            .addOnFailureListener { exception ->
                Log.e("bb", "" + exception.message)
            }

    }

//    override fun onPickResult(r: PickResult?) {
//
//        img_update.setImageBitmap(r!!.bitmap)
//        upoladImage(r.uri)
//    }

//    private fun upoladImage(uri: Uri) {
//        // progressDialoge!!.show()
//        referance.child("profile/" + UUID.randomUUID().toString()).putFile(uri)
//            .addOnSuccessListener { taskSnapshot ->
//                //      progressDialoge!!.hide()
//                Toast.makeText(this, "تم التحميل ", Toast.LENGTH_LONG).show()
//
//                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
//                    path1 = uri.toString()
//                }
//            }.addOnFailureListener {
//                Toast.makeText(this, "فشل التحميل ", Toast.LENGTH_LONG).show()
//            }
//    }

//        fun initVideo(){
//        player = SimpleExoPlayer.Builder(this).build()
//        video.player = player
//
//        val mediaItem = MediaItem.Builder().setUri(path1video).setMimeType(MimeTypes.APPLICATION_MP4).build()
//
//        val mediasource = ProgressiveMediaSource.Factory(
//            DefaultDataSource.Factory(this)
//        ).createMediaSource(mediaItem)
//        player!!.playWhenReady = palyerRead
//        player!!.seekTo(currentwindo,playerpostion)
//        player!!.prepare(mediasource,false,false)
//    }
//
//    fun releseVideo(){
//        if(player !=null){
//            palyerRead = player!!.playWhenReady
//            playerpostion = player!!.currentPosition
//            currentwindo = player!!.currentWindowIndex
//            player!!.release()
//            player = null
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        releseVideo()
//        initVideo()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        releseVideo()
//    }
}