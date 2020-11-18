package com.newsapp.utils

import androidx.room.TypeConverter
import com.newsapp.data.entities.Source
import java.text.SimpleDateFormat

class Converters {
    // Room Source Converters
    @TypeConverter
    fun fromSource(source: Source) : String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String) : Source {
        return Source(name, name)
    }
}