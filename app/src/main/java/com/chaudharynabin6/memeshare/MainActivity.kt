package com.chaudharynabin6.memeshare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.ASSERT
import android.view.View
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.chaudharynabin6.memeshare.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(this.layoutInflater)
        setContentView(this.binding.root)
        loadImage()


    }


    private fun loadImage(){

    // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

    // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                Log.w("json response",response.toString(1))
                Log.w("title",response.getString("preview"))
                Glide.with(this).load(response.getJSONArray("preview")[2]).into(this.binding.memeImageView)
            },
            { error ->
                // TODO: Handle error
                Log.w("json error",error.localizedMessage)
            }
        )

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

    fun nextMeme(view: View) {
        loadImage()
    }


}