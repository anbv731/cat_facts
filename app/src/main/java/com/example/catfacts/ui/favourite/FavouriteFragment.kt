package com.example.catfacts.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catfacts.databinding.FragmentFavouriteBinding
import com.example.catfacts.ui.Cat
import com.example.catfacts.ui.CatAdapter
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmConfiguration


class FavouriteFragment : Fragment() {
    lateinit var realm: Realm
    lateinit var realmChangeListener: RealmChangeListener<Realm>

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        initRealm()
        realm = Realm.getDefaultInstance()
        val cats = realm.where(Cat::class.java).findAll()
        realmChangeListener = RealmChangeListener<Realm> {
            println("REALM CHANGEed")
            setList(cats)
        }
        realm.addChangeListener(realmChangeListener)
        setList(cats)
    }


    override fun onResume() {
        super.onResume()
        realm = Realm.getDefaultInstance()
        val cats = realm.where(Cat::class.java).findAll()
        realmChangeListener = RealmChangeListener<Realm> {
            println("REALM CHANGEed")
            setList(cats)
        }
        realm.addChangeListener(realmChangeListener)
        setList(cats)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        realm.removeAllChangeListeners()
        realm.close()

    }

    private fun initRealm() {
        Realm.init(requireContext())
        val config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)
    }

    fun setList(cats: List<Cat>) {
        val adapter = CatAdapter(cats)
        binding.recyclerViewFv.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFv.layoutManager = layoutManager
    }

}