package com.example.noteappwithrealm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.realm.kotlin.Realm

class NoteViewModelFactory(private val realm: Realm) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(realm) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}