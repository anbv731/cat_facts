package com.example.catfacts.ui.facts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.catfacts.databinding.FragmentFactsBinding
import com.example.catfacts.ui.Cat
import com.example.catfacts.ui.CatAdapter
import org.json.JSONArray


class FactsFragment : Fragment() {
    lateinit var textOffline: TextView
    lateinit var buttonOffline: Button
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

        textOffline = binding.textViewOffline
        textOffline.visibility = GONE
        textOffline.text = "Что-то пошло не так, " +
                "проверьте соединение с интернетом и попробуйте загрузить еще раз"
        buttonOffline = binding.buttonOffline
        buttonOffline.visibility = GONE
        buttonOffline.text = "Загрузить еще раз"


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val queue = Volley.newRequestQueue(requireContext())
        getFactsFromServer(queue)
        buttonOffline.setOnClickListener {
            getFactsFromServer(queue)

        }
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
                buttonOffline.visibility = GONE
                textOffline.visibility = GONE

                val catList = parseResponse(response)
                setList(catList)
            },
            {
                println(it.message)
                textOffline.visibility = VISIBLE
                buttonOffline.visibility = VISIBLE

            }

        )
        queue.add(stringRequest)

    }

    private fun parseResponse(responseText: String): List<Cat> {
        val catList: MutableList<Cat> = mutableListOf()
        val jsonArray = JSONArray(responseText)
        for (index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            val catText = jsonObject.getString("text")
            val cat = Cat()
            cat.text = catText
            catList.add(cat)
        }
        return catList
    }

    fun setList(cats: List<Cat>) {
        val adapter = CatAdapter(cats)
        binding.recyclerViewId.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewId.layoutManager = layoutManager
    }

}