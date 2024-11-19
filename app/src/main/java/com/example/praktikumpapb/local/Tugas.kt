package com.example.praktikumpapb.local

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.parcelize.Parcelize

class UriConverter {
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        return uriString?.let { Uri.parse(it) }
    }
}

@Entity
@Parcelize
class Tugas (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "matkul")
    var matkul: String,
    @ColumnInfo(name = "detail_tugas")
    var detailTugas: String,
    @ColumnInfo(name = "selesai")
    var selesai: Boolean,
    @ColumnInfo(name = "image_uri")
    var imageUri: Uri? = null
) : Parcelable