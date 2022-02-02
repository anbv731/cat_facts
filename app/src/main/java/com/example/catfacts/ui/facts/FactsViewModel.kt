//package com.example.catfacts.ui.home
//
//import android.app.Application
//import android.content.Context
//import android.widget.Toast
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.android.volley.RequestQueue
//import com.android.volley.toolbox.StringRequest
//import com.android.volley.toolbox.Volley
//import com.example.catfacts.ui.Cat
//import io.realm.Realm
//import io.realm.Realm.*
//import io.realm.RealmConfiguration
//
//import org.json.JSONArray
//import org.json.JSONObject
//
//
//class FactsViewModel (application: Application) : AndroidViewModel(application) {
//    private val url: String = "https://cat-fact.herokuapp.com/facts"
//    var context: Context = getApplication()
//    val queue = Volley.newRequestQueue(context)
//
//
//
//fun getList (){
//    getFactsFromServer(queue)
//}
//
//
//
//
//
//    private var catList = MutableLiveData<List<Cat>>()
//
//     fun getData () {
//        if (catList == null) {
//            catList =  MutableLiveData<List<Cat>>()}
//
//        initRealm()
//        getFactsFromServer(queue)
//        val catList=loadFromDB()
//        FactsFragment().setList(catList)
//
//    }
//
//     fun getFactsFromServer(queue: RequestQueue) {
//        val stringRequest = StringRequest(
//            0,
//            url,
//            { response ->
//
//                val catList = parseResponse(response)
//                FactsFragment().setList(catList)
//
//
//            },
//            {
//                println(it.message)
//                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
//
//            }
//
//        )
//        queue.add(stringRequest)
//
//    }
//
//
//    private fun parseImageResponse(responseText: String): String {
//        val jsonObject = JSONObject(responseText)
//        val catImage = jsonObject.getString("file")
//        return catImage//возвращаемое значение функции
//    }
//
//    private fun parseResponse(responseText: String): List<Cat> {
//        //создаем пустой список объектов класса Cat
//        val catList: MutableList<Cat> = mutableListOf()
//        //преобразуем текст ответа сервера в JSON массива
//        val jsonArray = JSONArray(responseText)
//        //в цикле по количеству элементов массива JSON объектов
//        for (index in 0 until jsonArray.length()) {
//            // получаем каждый элемент в виде JSON объекта
//            val jsonObject = jsonArray.getJSONObject(index)
//            //получаем значение параметра text
//            val catText = jsonObject.getString("text")
//            //получаем значение параметра image
////            val catImage = jsonObject.getString("image")
//            //создаем объект класса Cat с вышеполученными параметрами
//            val cat = Cat()
//            cat.text = catText
////            cat.image = catImage
//            catList.add(cat) //добавляем в список
//        }
//        return catList //возвращаемое значение функции
//    }
//
////    private fun setList(cats: List<Cat>) {
////        val adapter = CatAdapter(cats)
////        recyclerViewId.adapter = adapter
////
////        val layoutManager = LinearLayoutManager(this)
////        recyclerViewId.layoutManager = layoutManager
////    }
//
//    private fun initRealm() {
//        Realm.init(context)
//        val config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
//        Realm.setDefaultConfiguration(config)
//    }
//    fun saveIntoDB (cats:List<Cat>){
//        val realm = Realm.getDefaultInstance()
//        realm.beginTransaction()
//        val catsDB=realm.where(Cat::class.java).findAll()
//        realm.copyToRealmOrUpdate(cats)
//        realm.commitTransaction()
//    }
//
//    fun loadFromDB ():List<Cat>{
//        val realm= Realm.getDefaultInstance()
//        return realm.where(Cat::class.java).findAll()
//    }
//
////    fun showListFromDB (){
////        val cats=loadFromDB()
////        FactsFragment().setList(cats)
////    }
////
//
//
//
//
//}