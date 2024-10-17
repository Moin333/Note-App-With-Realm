package com.example.noteappwithrealm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteViewModel(private val realm: Realm) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes = _notes.asStateFlow()

    init {
        getAllNotes()
    }

    private fun getAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            val results: RealmResults<Note> = realm.query<Note>().find()
            withContext(Dispatchers.Main) {
                _notes.value = results
            }
        }
    }

    fun addOrUpdateNote(id: String? = null, title: String, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            realm.write {
                if (id == null) {
                    copyToRealm(Note().apply {
                        this.title = title
                        this.content = content
                    })
                } else {
                    val note = query<Note>("id == $0", id).first().find()
                    note?.apply {
                        this.title = title
                        this.content = content
                    }
                }
            }
            // Directly update notes instead of calling getAllNotes()
            val updatedResults = realm.query<Note>().find()
            withContext(Dispatchers.Main) {
                _notes.value = updatedResults
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            realm.write {
                val liveNote = findLatest(note)
                liveNote?.let { delete(it) }
            }
            val updatedResults = realm.query<Note>().find()
            withContext(Dispatchers.Main) {
                _notes.value = updatedResults
            }
        }
    }

    fun searchNotes(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val results = realm.query<Note>("title CONTAINS[c] $0", query).find()
            withContext(Dispatchers.Main) {
                _notes.value = results
            }
        }
    }
}
