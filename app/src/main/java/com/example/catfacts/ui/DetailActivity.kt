package com.example.catfacts.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.catfacts.R
import com.example.catfacts.ui.favourite.FavouriteFragment
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.kotlin.where
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {
    private lateinit var textViewDetail: TextView
    private lateinit var imageViewDetail: ImageView
    private lateinit var button: Button


    companion object {
        const val CAT_FACT_TEXT = "cat_fact_text"

    }

    private val imageUrl: String = "https://aws.random.cat/meow "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        textViewDetail = findViewById(R.id.textViewDetail)
        imageViewDetail = findViewById(R.id.imageViewDetail)
        button = findViewById(R.id.button)

        val queue = Volley.newRequestQueue(this)
        getImageFromServer(queue)
        setupActionBar()
        initRealm()
        setText()


    }

    private fun changeFavourite() {
        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)
    }

    fun deletFromDB(cat: Cat) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val catsDB: RealmResults<Cat> =
            realm.where(Cat::class.java).equalTo("text", cat.text).findAll()
        if (!catsDB.isEmpty()) {
            for (i in catsDB.size - 1 downTo 0) {
                catsDB.get(i)!!.deleteFromRealm()
            }
        }
        realm.commitTransaction()

    }

    fun saveIntoDB(cat: Cat) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val catsDB: RealmResults<Cat> =
            realm.where(Cat::class.java).equalTo("text", cat.text).findAll()
        if (catsDB.isEmpty()) {
            realm.copyToRealm(cat)
        }
        realm.commitTransaction()
    }

    fun isInRealm(cat: Cat): Boolean {
        val realm = Realm.getDefaultInstance()
        return !realm.where(Cat::class.java).equalTo("text", cat.text).findAll().isEmpty()
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
        val cat = Cat()
        cat.text = text!!
        val inRealm = isInRealm(cat)
        if (inRealm) {button.text= "Удалить из избранного"
            button.setOnClickListener {
                deletFromDB(cat)

            }
        }else {button.text="Добавить в избранное"
            button.setOnClickListener {
            saveIntoDB(cat)

        }}
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
                Glide.with(this).applyDefaultRequestOptions(requestOption).load(image)
                    .into(imageViewDetail)
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