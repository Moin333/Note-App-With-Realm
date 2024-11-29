package com.example.noteappwithrealm

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration


object RealmConfig {
    val realm: Realm by lazy {
        val config = RealmConfiguration.Builder(schema = setOf(Note::class))
            .name("notes.realm")
            .schemaVersion(1)
            .build()
        Realm.open(config)
    }
}