package com.example.final_project_mcc

import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import kotlinx.android.synthetic.main.activity_add_topic_doctor.btn_add
import kotlinx.android.synthetic.main.activity_add_topic_doctor.btn_vde
import kotlinx.android.synthetic.main.activity_add_topic_doctor.edit_descTopic
import kotlinx.android.synthetic.main.activity_add_topic_doctor.edit_nameTopic
import kotlinx.android.synthetic.main.activity_add_topic_doctor.imageupload
import kotlinx.android.synthetic.main.activity_add_topic_doctor.txt_topic_inform
import java.util.UUID

class AddTopicDoctor : AppCompatActivity(), IPickResult {
    val db = Firebase.firestore
    lateinit var path: String
    lateinit var storage: FirebaseStorage
    lateinit var referance: StorageReference
    lateinit var auth: FirebaseAuth
    val VIDEO : Int = 3
    lateinit var pathvideo: String
    lateinit var uri : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_topic_doctor)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        auth = Firebase.auth
        storage = Firebase.storage
        referance = storage.reference

//add
        btn_add.setOnClickListener {
            AddTopic(
                auth.currentUser!!.uid,
                edit_nameTopic.text.toString(),
                edit_descTopic.text.toString(),
                path,
                txt_topic_inform.text.toString(),
                pathvideo
            )
        }

        imageupload.setOnClickListener {
            PickImageDialog.build(PickSetup()).show(this)
        }
        //video
        btn_vde.setOnClickListener {
            selecteVideo()
        }

    }
    fun selecteVideo(){

        val intent = Intent()
        intent.setType ("video/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select Video"), VIDEO)
    }
    override fun onPickResult(r: PickResult?) {

        imageupload.setImageBitmap(r!!.bitmap)
        upoladImage(r.uri)
    }

    private fun upoladImage(uri: Uri) {
        // progressDialoge!!.show()
        referance.child("profile/" + UUID.randomUUID().toString()).putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                //      progressDialoge!!.hide()
                Toast.makeText(this, "تم التحميل ", Toast.LENGTH_LONG).show()

                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    path = uri.toString()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "فشل التحميل ", Toast.LENGTH_LONG).show()
            }
    }

    private fun AddTopic(
        idUser:String,
        name: String,
        description: String,
        image: String,
        information: String,
        video: String,
    ) {


        var Topic =
            hashMapOf(
                "idUser" to idUser,
                "name" to name,
                "description" to description,
                "image" to image,
                "information" to information,
                "video" to video
            )
        db.collection("topic")
            .add(Topic)
            .addOnSuccessListener { documentReference ->
                val id = documentReference.id
                db.collection("topic").document(documentReference.id)
                    .update("id", id)
                    .addOnSuccessListener {
                        Toast.makeText(this, "تمت الاضافة بنجاح", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "فشلت الاضافة", Toast.LENGTH_LONG).show()
                    }
                Toast.makeText(this, "تمت الاضافة بنجاح", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(this, "فشلت الاضافة", Toast.LENGTH_LONG).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //val uriTxt = findViewById<View>(R.id.evideo) as TextView
        if (resultCode == RESULT_OK) {
      if (requestCode == VIDEO) {
                uri = data!!.data!!
               //uriTxt.text = uri.toString()
                upoladviedo (uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun upoladviedo(uri: Uri) {
        // progressDialoge!!.show()
        referance.child("viedo/" + UUID.randomUUID().toString()).putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                //      progressDialoge!!.hide()
                Toast.makeText(this, "تم التحميل الفيديو ", Toast.LENGTH_LONG).show()

                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    pathvideo = uri.toString()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "فشل التحميل الفيديو ", Toast.LENGTH_LONG).show()
            }
    }
//
//    fun initVideo(){
//        player = SimpleExoPlayer.Builder(this).build()
//        video.player = player
//
//        val mediaItem = MediaItem.Builder().setUri(pathvideo).setMimeType(MimeTypes.APPLICATION_MP4).build()
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
//    }
//
//    override fun onPause() {
//        super.onPause()
//        releseVideo()
//    }

}