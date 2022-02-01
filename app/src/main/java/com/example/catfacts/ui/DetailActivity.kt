package com.example.catfacts.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.catfacts.R
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {
    private lateinit var textViewDetail: TextView
    private  lateinit var imageViewDetail: ImageView


    companion object {
        const val CAT_FACT_TEXT = "cat_fact_text"

    }
    private val imageUrl: String = "https://aws.random.cat/meow "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        textViewDetail = findViewById(R.id.textViewDetail)
        imageViewDetail= findViewById(R.id.imageViewDetail)
        val queue = Volley.newRequestQueue(this)
        getImageFromServer(queue)
        setupActionBar()
        setText()

    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = "Detail"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setText() {
        val text = intent?.extras?.getString(CAT_FACT_TEXT)
        textViewDetail.text = text


    }
        private fun getImageFromServer(queue: RequestQueue) {
            val circularProgressDrawable = CircularProgressDrawable(this)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            val requestOption = RequestOptions()
                .placeholder(circularProgressDrawable).centerCrop()
            val imageRequest = StringRequest(
                0,
                imageUrl,
                { response ->
                    val image = parseImageResponse(response)
                    Glide.with(this).applyDefaultRequestOptions(requestOption).load(image).into(imageViewDetail)
                },
                {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            )

            queue.add(imageRequest)
    }
    private fun parseImageResponse(responseText: String): String {
        val jsonObject = JSONObject(responseText)
        val catImage = jsonObject.getString("file")
        return catImage
    }

}