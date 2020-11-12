package com.internshala.echo.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.internshala.echo.Songs
import com.internshala.echo.databases.EchoDatabase.Staticated.TABLE_NAME

class EchoDatabase: SQLiteOpenHelper{
    var songList = ArrayList<Songs>()


    object Staticated{
        val DB_NAME = "FavoriteDatabase"
        var DB_VERSION = 1
        val TABLE_NAME ="FavoriteTable"
        val COLUMN_ID = "SongID"
        val COLUMN_SONG_TITLE ="SongTitle"
        val COLUMN_SONG_ARTIST = "SongArtist"
        val COLUMN_SONG_PATH = "Song Path"
    }

    override fun onCreate(sqliteDatabase: SQLiteDatabase?) {
        sqliteDatabase?.execSQL(" CREATE TABLE " + Staticated.TABLE_NAME + " ( " + Staticated.COLUMN_ID + " INTEGER, " + Staticated.COLUMN_SONG_ARTIST + " STRING, "
                + Staticated.COLUMN_SONG_TITLE + " STRING, " + Staticated.COLUMN_SONG_PATH + " STRING);"
        )

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("drop table if exists "+Staticated.TABLE_NAME)
        onCreate(p0)
    }

    constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : super(context, name, factory, version)
    constructor(context: Context?) : super(context, Staticated.DB_NAME, null, Staticated.DB_VERSION)
    fun storeAsFavorite(id: Int?,artist: String?, songTitle: String?, path: String?){

        val db = this.writableDatabase

        val contentValues = ContentValues()

        contentValues.put(Staticated.COLUMN_ID, id)
        contentValues.put(Staticated.COLUMN_SONG_ARTIST, artist)
        contentValues.put(Staticated.COLUMN_SONG_TITLE, songTitle)
        contentValues.put(Staticated.COLUMN_SONG_PATH, path)

        db.insert(Staticated.TABLE_NAME, null, contentValues)
        db.close()
    }

    fun queryDBList(): ArrayList<Songs>?{
        try {
            val db= this.readableDatabase
            val query_params = "SELECT * FROM " + Staticated.TABLE_NAME
            val cSor = db.rawQuery(query_params,null)
            if (cSor.moveToFirst()){
                do{
                    val _id = cSor.getInt(cSor.getColumnIndexOrThrow(Staticated.COLUMN_ID))
                    val _artist = cSor.getString(cSor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_ARTIST))
                    val _title = cSor.getString(cSor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_TITLE))
                    val _songPath = cSor.getString(cSor.getColumnIndexOrThrow(Staticated.COLUMN_SONG_PATH))
                    songList.add(Songs(_id.toLong() , _artist, _title, _songPath, 0))
                }while (cSor.moveToNext())
            }else
            {
                return null
            }

        }catch (e:Exception){
            e.printStackTrace()
        }

        return songList
    }

    fun checkIfIdExists(_id: Int): Boolean{
        var storeId = -1090
        val db = this.readableDatabase
        val queryParams = "SELECT * FROM " + Staticated.TABLE_NAME + " WHERE SongId = '$_id'"
        val cSor = db.rawQuery(queryParams,null)
        if (cSor.moveToFirst()) {
            do {
                storeId = cSor.getInt(cSor.getColumnIndexOrThrow(Staticated.COLUMN_ID))
            } while (cSor.moveToNext())
        }else {
            return false
        }
        return storeId != -1090
    }

    fun deleteFavorite(_id: Int){
        val db = this.readableDatabase
        db.delete(Staticated.TABLE_NAME, Staticated.COLUMN_ID + "=" + _id, null)
        db.close()
    }

    fun checkSize(): Int{
        var counter=0
        val db =this.readableDatabase
        val queryParams = "SELECT * FROM " + TABLE_NAME
        val cSor = db.rawQuery(queryParams, null)
        if(cSor.moveToFirst()){
            do{
                counter= counter + 1
            }while (cSor.moveToNext())
        }else{
            return 0
        }
        return counter
    }
}