package com.example.nallanudi.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val term: String,
    val kannadaMeaning: String,
    val explanation: String,
    val exampleSentence: String,
    val subject: String
)

@Entity(tableName = "saved_words")
data class SavedWord(
    @PrimaryKey
    val wordId: Long,
    val savedAt: Long = System.currentTimeMillis()
)