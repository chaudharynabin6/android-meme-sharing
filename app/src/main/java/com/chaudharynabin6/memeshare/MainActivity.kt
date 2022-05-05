package com.chaudharynabin6.memeshare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chaudharynabin6.memeshare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(this.layoutInflater)
        setContentView(this.binding.root)

//        this.binding.memeImageView.setImageResource(R.mipmap.ic_launcher)
    }
}