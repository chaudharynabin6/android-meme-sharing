package com.chaudharynabin6.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chaudharynabin6.memeshare.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentUrl: String? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(this.layoutInflater)
        setContentView(this.binding.root)
        loadImage()


    }


    private fun loadImage(){
    this.binding.progressCircular.visibility = View.VISIBLE
    // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

    // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                Log.w("json response",response.toString(1))
                Log.w("title",response.getString("preview"))
                this.currentUrl = response.getString("url")

                Glide.with(this).load(this.currentUrl).listener(object :RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                       this@MainActivity.binding.progressCircular.visibility = View.GONE
                        return false
                    }
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        this@MainActivity.binding.progressCircular.visibility = View.GONE
                        return false
                    }
                    }
                ).into(this.binding.memeImageView)
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

    fun shareMeme(view: View) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, this@MainActivity.currentUrl)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Share Image URL")
        startActivity(shareIntent)
    }


}