package com.example.noteapplication.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapplication.databinding.ActivityMainBinding
import com.example.noteapplication.models.Note
import com.example.noteapplication.remote.database.NoteDataBase
import com.example.noteapplication.repository.NoteRepository
import com.example.noteapplication.view.adapter.NoteAdapter
import com.example.noteapplication.view.viewmodel.NoteViewModel
import com.example.noteapplication.view.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: NoteViewModel by viewModels {
        val dao = NoteDataBase.getDatabase(this).getNoteDao()
        val repo = NoteRepository(dao)
        NoteViewModelFactory(repo)
    }

    private var dataBase: NoteDataBase? = null
    private var adapter: NoteAdapter? = null
    private lateinit var selectedNote: Note


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()



        viewModel.allNotes?.observe(this) {
            it.let {
                adapter?.upDateList(it)
            }
        }
    }

    private fun initUi() {

        binding.noteListingRv.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NoteAdapter(this)
        binding.noteListingRv.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapter?.filterList(newText)
                }
                return true
            }
        })

        binding.floatingActionButton.setOnClickListener {
            val iNext = Intent(this, AddNotesActivity::class.java)
            startActivity(iNext)
        }
    }
}