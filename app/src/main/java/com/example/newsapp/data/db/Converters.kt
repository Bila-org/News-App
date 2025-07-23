package com.example.newsapp.data.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.newsapp.data.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source?): String?{
        return source?.name
    }

    @TypeConverter
    fun toSource(name: String?): Source?{
        return name?.let{
            Source(name, name)
        }
    }
}