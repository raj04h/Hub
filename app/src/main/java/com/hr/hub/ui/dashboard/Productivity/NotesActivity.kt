package com.hr.hub.ui.dashboard.Productivity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hr.hub.R
import com.hr.hub.ui.dashboard.Productivity.dataProductivity.NotesDAO
import com.hr.hub.ui.dashboard.Productivity.dataProductivity.NotesDB
import com.hr.hub.ui.dashboard.Productivity.dataProductivity.NotesEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesActivity : AppCompatActivity() {

    private lateinit var btncreateNote:Button
    private lateinit var floatbtnNote:FloatingActionButton
    private lateinit var recycleviewNote:RecyclerView
    private lateinit var linearempty: LinearLayout

    private lateinit var notesDB: NotesDB
    private lateinit var notesDAO: NotesDAO
    private lateinit var notesAdapter:NotesrecyleAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        callId()
        setupRecycleview()
        setupDB()

        floatbtnNote.setOnClickListener {
            Toast.makeText(this, "Start Creating", Toast.LENGTH_SHORT).show()
            showNotesDialogBox()
        }
        btncreateNote.setOnClickListener {
            floatbtnNote.performClick()
            }
    }

    private fun callId() {
        btncreateNote = findViewById(R.id.btn_notecreate)
        floatbtnNote = findViewById(R.id.fabadd)
        recycleviewNote = findViewById(R.id.recycleview_notes)
        linearempty = findViewById(R.id.linear_empty)
    }

    private fun setupRecycleview(){
        recycleviewNote.layoutManager=GridLayoutManager(this,2)
        notesAdapter= NotesrecyleAdapter(this)
        recycleviewNote.adapter=notesAdapter
    }

    private fun setupDB(){
        notesDB=NotesDB.getData(applicationContext)
        notesDAO=notesDB.notesdao()

        notesAdapter=NotesrecyleAdapter(this)
        recycleviewNote.adapter=notesAdapter
        loadNotes()
    }

    private fun loadNotes(){
        CoroutineScope(Dispatchers.IO).launch {
            val getNotes = notesDAO.getAllNotes()

            withContext(Dispatchers.Main) {
                if (getNotes.isEmpty()) {
                    recycleviewNote.visibility = RecyclerView.GONE
                    linearempty.visibility = LinearLayout.VISIBLE
                } else {
                    recycleviewNote.visibility = RecyclerView.VISIBLE
                    linearempty.visibility = LinearLayout.GONE
                }
                notesAdapter.submitList(getNotes)

            }
        }
    }

    private fun saveNoteToDB(tittle:String,content: String){
        CoroutineScope(Dispatchers.IO).launch {
            val note = NotesEntity(tittle = tittle, content = content)
            notesDAO.insertNotes(note)
            withContext(Dispatchers.Main){
                loadNotes()
                Toast.makeText(this@NotesActivity, "Data Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showNotesDialogBox(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.notes_content_box)

        val ettittle: EditText = dialog.findViewById(R.id.et_notetittle)
        val ettcontent: EditText = dialog.findViewById(R.id.et_notescontent)
        val btnsave: Button = dialog.findViewById(R.id.btn_notesave)

        btnsave.setOnClickListener {
            val tittle = ettittle.text.toString().trim()
            val content = ettcontent.text.toString().trim()

            if (tittle.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Please fill it", Toast.LENGTH_SHORT).show()

            }
            else {
                saveNoteToDB(tittle, content)
                Toast.makeText(this, "Notes Saved", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}