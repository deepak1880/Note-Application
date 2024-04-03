package com.example.noteapplication.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapplication.databinding.ActivityAddNotesBinding
import com.example.noteapplication.models.Note
import com.example.noteapplication.utilities.extensions.showToast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNotesBinding

    private var note: Note? = null
    private var oldNote: Note? = null
    private var isUpdate = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            oldNote = intent.getSerializableExtra("current_note") as? Note
            binding.titleTv.setText(oldNote?.title)
            binding.noteTv.setText(oldNote?.note)
            isUpdate = true

        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.checkIv.setOnClickListener {
            val title = binding.titleTv.text.toString()
            val noteDescription = binding.noteTv.text.toString()

            if (title.isNotEmpty() || noteDescription.isNotEmpty()) {

                val dateFormatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a", Locale.getDefault())
                val formattedDate = dateFormatter.format(Date())

                if (isUpdate) {
                    note = Note(oldNote?.id, title, noteDescription, formattedDate)
                } else {
                    note = Note(null, title, noteDescription, formattedDate)
                }

                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                showToast("Please Enter Some Data")
            }
        }

        binding.backIv.setOnClickListener {
            onBackPressed()
        }
    }
}