package com.example.noteappwithrealm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.noteappwithrealm.ui.theme.NoteAppWithRealmTheme

class MainActivity : ComponentActivity() {

    private val viewModel: NoteViewModel by viewModels {
        NoteViewModelFactory(RealmConfig.realm)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppWithRealmTheme {
                NotesApp(viewModel)
            }
        }
    }
}
