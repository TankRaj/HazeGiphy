package com.hazetest.hazegiphy.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteGif(
    var gifId : String,
    var url : String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
