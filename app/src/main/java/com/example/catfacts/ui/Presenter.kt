package com.example.catfacts.ui

import android.content.Context
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import io.realm.Realm
import io.realm.RealmConfiguration
import org.json.JSONArray

class Presenter(context: Context) {

    val queue = Volley.newRequestQueue(context)
    private val url: String = "https://cat-fact.herokuapp.com/facts"
    private fun getFactsFromServer(queue: RequestQueue,context: Context) {
        val stringRequest = StringRequest(
            0,
            url,
            { response ->

                val catList = parseResponse(response)
                saveIntoDB(catList)
                //showListFromDB()


            },
            {
                println(it.message)
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()

            }

        )
        queue.add(stringRequest)

    }


    private fun parseResponse(responseText: String): List<Cat> {
        //создаем пустой список объектов класса Cat
        val catList: MutableList<Cat> = mutableListOf()
        //преобразуем текст ответа сервера в JSON массива
        val jsonArray = JSONArray(responseText)
        //в цикле по количеству элементов массива JSON объектов
        for (index in 0 until jsonArray.length()) {
            // получаем каждый элемент в виде JSON объекта
            val jsonObject = jsonArray.getJSONObject(index)
            //получаем значение параметра text
            val catText = jsonObject.getString("text")
            //получаем значение параметра image
//            val catImage = jsonObject.getString("image")
            //создаем объект класса Cat с вышеполученными параметрами
            val cat = Cat()
            cat.text = catText
//            cat.image = catImage
            catList.add(cat) //добавляем в список
        }
        return catList //возвращаемое значение функции
    }
    private fun initRealm(context:Context) {
        Realm.init(context)
        val config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)
    }
    fun saveIntoDB (cats:List<Cat>){
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val catsDB=realm.where(Cat::class.java).findAll()
        realm.copyToRealmOrUpdate(cats)
        realm.commitTransaction()
    }

    fun loadFromDB ():List<Cat>{
        val realm= Realm.getDefaultInstance()
        return realm.where(Cat::class.java).findAll()
    }

    fun showListFromDB (){
        val cats=loadFromDB()
        println(cats.last().text)

    }

}