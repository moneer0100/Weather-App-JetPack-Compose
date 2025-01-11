package com.example.weatherappjetpackconpose.model.dp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.weatherappjetpackconpose.model.pojo.Alert
import com.example.weatherappjetpackconpose.model.pojo.FavouriteWeather
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Database(entities = [FavouriteWeather::class,Alert::class], version = 2)
abstract class AppDataBase:RoomDatabase(){
abstract fun weatherApp():Dao

}
@Module
@InstallIn(SingletonComponent::class)
object DataBaseClient{
    @Volatile
 private var instance:AppDataBase?=null
@Singleton
@Provides
 fun getInstance(
    @ApplicationContext
    context: Context):AppDataBase{

    return instance?: synchronized(this){
        instance?:Room.databaseBuilder(context
            ,AppDataBase::class.java
            ,"favWeather").addMigrations(MIGRATION_1_2)
            .build().also { instance=it }

    }

}
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {

            database.execSQL("ALTER TABLE FavouriteWeather ADD COLUMN new_column_name TEXT DEFAULT '' NOT NULL")
        }
    }
    @Singleton
    @Provides
    fun provideDao(database: AppDataBase): Dao {
        return database.weatherApp()
    }
}