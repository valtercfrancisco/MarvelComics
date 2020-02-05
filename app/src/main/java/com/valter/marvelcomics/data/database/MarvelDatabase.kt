package com.valter.marvelcomics.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.valter.marvelcomics.data.converters.StringListTypeConverter
import com.valter.marvelcomics.data.database.entity.Comic

@Database(
        entities = [Comic::class],
        version = 1
)
@TypeConverters(
        StringListTypeConverter::class
)
abstract class MarvelDatabase : RoomDatabase() {
    abstract fun comicDao(): ComicDao
}