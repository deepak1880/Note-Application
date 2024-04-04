package com.example.noteapplication.view.adapter

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.R
import com.example.noteapplication.databinding.ItemListBinding
import com.example.noteapplication.models.Note
import kotlin.random.Random

class NoteAdapter(
    private val onItemClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val noteList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()
    private val colorMap = HashMap<Int?, Int>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context))
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val bind = noteList[position]
        holder.bind(bind)
    }

    fun randomColor(): Int {

        val list = ArrayList<Int>()
        list.add(R.color.color1)
        list.add(R.color.color2)
        list.add(R.color.color3)
        list.add(R.color.color4)
        list.add(R.color.color5)
        list.add(R.color.color6)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

    fun upDateList(newList: List<Note>) {
        fullList.clear()
        fullList.addAll(newList)

        noteList.clear()
        noteList.addAll(fullList)
        notifyDataSetChanged()
    }

    fun filterList(search: String) {

        noteList.clear()

        for (item in fullList) {
            if (item.title?.lowercase()?.contains(search.lowercase()) == true ||
                item.note?.lowercase()?.contains(search.lowercase()) == true
            ) {

                noteList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.parentCv.setOnClickListener {
                onItemClick.invoke(noteList[adapterPosition])
            }
            binding.deleteIv.setOnClickListener {
                onDeleteClick.invoke(noteList[adapterPosition])
            }
        }

        fun bind(note: Note) {
            binding.titleTv.text = note.title
            binding.noteTv.text = note.note
            binding.dateTv.text = note.date

            if (!colorMap.containsKey(note.id)) {
                colorMap[note.id] = ContextCompat.getColor(binding.root.context, randomColor())
            }
            binding.parentCv.setCardBackgroundColor(colorMap[note.id]!!)
        }
    }
}

class StaggeredGridSpacingItemDecoration(
    private val spacing: Int,
    private val spanCount: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.top = spacing
        outRect.bottom = spacing
        outRect.left = spacing
        outRect.right = spacing

        // Add spacing between columns for StaggeredGridLayoutManager
        if (spanCount > 1) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount
        }
    }
}

