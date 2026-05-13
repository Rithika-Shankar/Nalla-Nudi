package com.example.nallanudi.database

import androidx.room.*
import android.content.Context
import com.example.nallanudi.models.Word
import com.example.nallanudi.models.SavedWord
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [Word::class, SavedWord::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nalla_nudi_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Dao
interface WordDao {
    @Query("SELECT * FROM words WHERE term LIKE '%' || :query || '%' ORDER BY term")
    fun searchWords(query: String): Flow<List<Word>>

    @Query("SELECT * FROM words WHERE subject = :subject ORDER BY term")
    fun getWordsBySubject(subject: String): Flow<List<Word>>

    @Query("SELECT * FROM words WHERE term LIKE '%' || :query || '%' AND subject = :subject ORDER BY term")
    fun searchWordsBySubject(query: String, subject: String): Flow<List<Word>>

    @Query("SELECT * FROM words WHERE id IN (SELECT wordId FROM saved_words) ORDER BY term")
    fun getSavedWords(): Flow<List<Word>>

    @Insert
    suspend fun insertWord(word: Word)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWord(savedWord: SavedWord)

    @Delete
    suspend fun removeSavedWord(savedWord: SavedWord)

    @Query("SELECT * FROM words ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomWord(): Word?
}