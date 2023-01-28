package com.serapercel.trickle.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName="needs_table")
data class Need(
    @ColumnInfo("name")
    val name: String?,
    @ColumnInfo("count")
    val count: String
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}