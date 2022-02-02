package com.example.catfacts.ui.facts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.catfacts.databinding.FragmentFactsBinding
import com.example.catfacts.ui.Cat
import com.example.catfacts.ui.CatAdapter
import io.realm.Realm
import io.realm.RealmConfiguration
import org.json.JSONArray


class FactsFragment : Fragment() {
    private val url: String = "https://cat-fact.herokuapp.com/facts"

        private var _binding: FragmentFactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {


        _binding = FragmentFactsBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val text = binding.textFacts

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



//        initRealm()
//        showListFromDB()
        val queue = Volley.newRequestQueue(requireContext())
        getFactsFromServer(queue)



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun getFactsFromServer(queue: RequestQueue) {
        val stringRequest = StringRequest(
            0,
            url,
            { response ->

                val catList = parseResponse(response)
                setList(catList)

               // saveIntoDB(catList)
               // showListFromDB()


            },
            {
                println(it.message)
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()

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

    private fun initRealm() {
        Realm.init(requireContext())
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
        setList(cats)
    }
    fun setList(cats: List<Cat>) {
        val adapter = CatAdapter(cats)
        binding.recyclerViewId.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewId.layoutManager = layoutManager
    }

}