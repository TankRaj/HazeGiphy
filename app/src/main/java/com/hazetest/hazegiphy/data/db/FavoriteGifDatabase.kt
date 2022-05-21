package com.hazetest.hazegiphy.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hazetest.hazegiphy.data.db.dao.FavoriteGifDao
import com.hazetest.hazegiphy.data.db.entity.FavoriteGif

@Database(entities = [FavoriteGif::class], version = 1)
abstract class FavoriteGifDatabase : RoomDatabase() {
    abstract fun favoriteGifDao() : FavoriteGifDao

    companion object {
        private var instance: FavoriteGifDatabase? = null

        @Synchronized
        fun getInstance(context: Context): FavoriteGifDatabase? {
            if (instance == null) {
                synchronized(FavoriteGifDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteGifDatabase::class.java,
                        "favorite-gif-database"
                    ).build()
                }
            }
            return instance
        }
    }
}