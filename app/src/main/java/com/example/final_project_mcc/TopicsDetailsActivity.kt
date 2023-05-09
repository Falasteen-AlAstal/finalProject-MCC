package com.example.final_project_mcc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.MimeTypes
import kotlinx.android.synthetic.main.activity_topics_details.*

class TopicsDetailsActivity : AppCompatActivity() {
    var player: SimpleExoPlayer? = null
    var videoURl = ""
    var playerWhenReady = true
    var currentWindow = 0
    var playBackPosition: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topics_details)

        val name = intent.getStringExtra("TopicName")
        val description = intent.getStringExtra("TopicDescription")
        val information =intent.getStringExtra("TopicInformation")
        val image = intent.getStringExtra("TopicImage")
        videoURl = intent.getStringExtra("TopicVideo").toString()

        topic_name.text = name
        topic_description.text = description
        topic_info.text = information
        Glide.with(this).load(image).into(topic_image)

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

}