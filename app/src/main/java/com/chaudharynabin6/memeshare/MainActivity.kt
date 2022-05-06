package com.chaudharynabin6.memeshare

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chaudharynabin6.memeshare.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(this.layoutInflater)
        setContentView(this.binding.root)
        loadImage()


    }


    private fun loadImage(){
    this.binding.progressCircular.visibility = View.VISIBLE
    // Instantiate the RequestQueue.
//        val queue = Volley.newRequestQueue(this)
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
                Log.w("json error",error.localizedMessage.toString())
            }
        )

        // Add the request to the RequestQueue.
//        queue.add(jsonObjectRequest)
        val queue = APIRequestQueue.getInstance(this.applicationContext).requestQueue
        queue.add(jsonObjectRequest)
    }

    fun nextMeme(view: View) {
        loadImage()
    }

    fun shareMeme(view: View) {
//        val sendIntent: Intent = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_TEXT, this@MainActivity.currentUrl)
//            type = "text/plain"
//        }
//
//        val shareIntent = Intent.createChooser(sendIntent, "Share Image URL")
//        startActivity(shareIntent)
        val bitmap = this.binding.memeImageView.drawable.toBitmap()
        shareImageAndText(bitmap)
    }

    private fun shareImageAndText(bitmap: Bitmap) {
        val uri: Uri? = getImageToShare(bitmap)
        val intent = Intent(Intent.ACTION_SEND)

        // putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        // adding text to share
        intent.putExtra(Intent.EXTRA_TEXT, "Sharing Image")

        // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")

        // setting type to image
        intent.type = "image/png"

        // calling startactivity() to share
//        startActivity(Intent.createChooser(intent, "Share Via"))

        val chooser = Intent.createChooser(intent, "Share File")

        val resInfoList =
            this.packageManager.queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            grantUriPermission(
                packageName,
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        startActivity(chooser)
    }


    // Retrieving the url to share
    private fun getImageToShare(bitmap: Bitmap): Uri? {
        val imageFolder = File(cacheDir, "images")
        var uri: Uri? = null
        try {
            imageFolder.mkdirs()
            val file = File(imageFolder, "shared_image.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
            uri = FileProvider.getUriForFile(this, "com.chaudharynabin6.memeshare.fileprovider", file)
        } catch (e: Exception) {
            Toast.makeText(this, "" + e.message, Toast.LENGTH_LONG).show()
        }
        return uri
    }
}