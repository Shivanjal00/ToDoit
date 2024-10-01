package com.example.todoit.Model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize // Add this annotation
@Entity(tableName = "Notes")
data class Notes( // Change `class` to `data class`
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var title: String,
    var subTitle: String,
    var notes: String,
    var date: String,
    var priority: String
) : Parcelable // Implement Parcelable
