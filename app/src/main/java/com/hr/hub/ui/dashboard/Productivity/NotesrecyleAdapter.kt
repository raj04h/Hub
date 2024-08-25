package com.hr.hub.ui.dashboard.Productivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.hr.hub.R
import com.hr.hub.ui.dashboard.Productivity.dataProductivity.NotesDAO
import com.hr.hub.ui.dashboard.Productivity.dataProductivity.NotesDB
import com.hr.hub.ui.dashboard.Productivity.dataProductivity.NotesEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesrecyleAdapter(val context: Context) :
    RecyclerView.Adapter<NotesrecyleAdapter.ViewHolder>() {

    private var notesdao: NotesDAO = NotesDB.getData(context).notesdao()
    private var notesList: List<NotesEntity> = listOf()

    // Find Id's of notes viewing windows
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tittleTextView: TextView = itemView.findViewById(R.id.note_title)
        var contentTextView: TextView = itemView.findViewById(R.id.note_content)
        var llnoteitem: LinearLayout = itemView.findViewById(R.id.llRow_notesitem)
    }

    // Calling notes viewing layout
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.note_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    // Set data in the database
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notesList[position]
        holder.tittleTextView.text = note.tittle ?: " "
        holder.contentTextView.text = note.content ?: " "

        holder.llnoteitem.setOnLongClickListener {
            showdelDialog(holder.itemView.context, note)
            true
        }
    }

    private fun loadNotes() {
        CoroutineScope(Dispatchers.IO).launch {
            val notes = notesdao.getAllNotes() // Assuming this method exists
            withContext(Dispatchers.Main) {
                submitList(notes)
            }
        }
    }

    fun submitList(notesEntity: List<NotesEntity>) {
        notesList = notesEntity
        notifyDataSetChanged()
    }

    private fun showdelDialog(context: Context, notesEntity: NotesEntity) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Note?")
        builder.setMessage("Are you sure you want to delete this note?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            deleteNoteFromDB(notesEntity)
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun deleteNoteFromDB(notesEntity: NotesEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            notesdao.deleteNote(notesEntity)
            withContext(Dispatchers.Main) {
                loadNotes() // Reload the notes after deletion
            }
        }
    }
}
