package com.example.snapchat

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

import java.net.HttpURLConnection
import java.net.URL

class ViewSnapsActivity : AppCompatActivity() {

    var messageTextView: TextView? = null
    var snapsImageView: ImageView? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snaps)

        messageTextView = findViewById(R.id.messageTextView)
        snapsImageView = findViewById(R.id.snapsImageView)
        auth  = Firebase.auth

        messageTextView?.text = intent.getStringExtra("message")

        val task = ImageDownloader()
        val newImage: Bitmap
        try {
            newImage =
                task.execute(intent.getStringExtra("imageURL"))
                    .get()!!
            snapsImageView?.setImageBitmap(newImage)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }
    class ImageDownloader : AsyncTask<String?, Void?, Bitmap?>() {
        protected override fun doInBackground(vararg urls: String?): Bitmap? {
            return try {
                val url = URL(urls[0])
                val urlConnection =
                    url.openConnection() as HttpURLConnection
                urlConnection.connect()
                val `in` = urlConnection.inputStream
                BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()

        FirebaseDatabase.getInstance().getReference("users").child(auth.currentUser!!.uid).child("snaps").child(intent.getStringExtra("snapKey")).removeValue()

        FirebaseStorage.getInstance().getReference("images").child(intent.getStringExtra("imageName")).delete()
    }
}