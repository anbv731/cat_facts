package com.example.catfacts.ui.favourite

import android.database.Observable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.Volley
import com.example.catfacts.databinding.FragmentFavouriteBinding
import com.example.catfacts.ui.Cat
import com.example.catfacts.ui.CatAdapter
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmConfiguration
import io.realm.RealmResults


class FavouriteFragment : Fragment() {
    lateinit var  realm :Realm
    lateinit var realmChangeListener: RealmChangeListener<Realm>

    private var _binding: FragmentFavouriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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
         realm= Realm.getDefaultInstance()
        val cats = realm.where(Cat::class.java).findAll()
         realmChangeListener = RealmChangeListener<Realm> {println("REALM CHANGEed")
            setList(cats)}
        realm.addChangeListener(realmChangeListener)
        setList(cats)
        //        val realm= Realm.getDefaultInstance()
//        val realmListener= RealmChangeListener<Realm> { results -> }
//        realm.addChangeListener(realmListener)
        }


    override fun onResume() {
        super.onResume()
        realm= Realm.getDefaultInstance()
        val cats = realm.where(Cat::class.java).findAll()
        realmChangeListener = RealmChangeListener<Realm> {println("REALM CHANGEed")
            setList(cats)}
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
    fun saveIntoDB (cats:List<Cat>){
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val catsDB=realm.where(Cat::class.java).findAll()
        realm.copyToRealmOrUpdate(cats)
        realm.commitTransaction()
    }

//    fun loadFromDB ():List<Cat>{
//
//
//
//    }


//    fun showListFromDB (){
//        val cats=loadFromDB()
//
//        setList(cats)
//    }
    fun setList(cats: List<Cat>) {
        val adapter = CatAdapter(cats)
        binding.recyclerViewFv.adapter = adapter

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFv.layoutManager = layoutManager
    }

}