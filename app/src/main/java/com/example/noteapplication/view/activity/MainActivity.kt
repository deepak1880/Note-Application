package com.example.noteapplication.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
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


    private val updateNote =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            if (it.resultCode == Activity.RESULT_OK) {
                val note = it.data?.getSerializableExtra("note") as? Note
                if (note != null) {
                    viewModel.updateNote(note)
                }
            }
        }

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
        adapter = NoteAdapter(this, onItemClick = {
            val intent = Intent(this, AddNotesActivity::class.java)
            intent.putExtra("current_note", it)
            updateNote.launch(intent)
        },onDeleteClick = {
            viewModel.deleteNote(it)
        }
        )
        binding.noteListingRv.adapter = adapter

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

                if (it.resultCode == Activity.RESULT_OK) {
                    val note = it.data?.getSerializableExtra("note") as? Note
                    if (note != null) {
                        viewModel.insertNote(note)
                    }
                }
            }

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
            getContent.launch(iNext)
        }
    }
}