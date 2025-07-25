package com.ljyVoca.vocabularyapp.di

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ljyVoca.vocabularyapp.db.VocabularyDatabase
import com.ljyVoca.vocabularyapp.db.WeeklyGoalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideVocaDatabase(app: Application) =
        Room.databaseBuilder(app, VocabularyDatabase::class.java, "voca_db")
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
            .build()

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE voca ADD COLUMN phonetic TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE voca ADD COLUMN description TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE voca ADD COLUMN wrongCount INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE voca ADD COLUMN totalAttempts INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE voca ADD COLUMN lastStudiedDate INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE voca ADD COLUMN category TEXT NOT NULL DEFAULT '영어'")
                database.execSQL("ALTER TABLE voca ADD COLUMN isBookmarked INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE voca ADD COLUMN createdDate INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")
            }
        }
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 기존 한글 카테고리를 언어 코드로 변경
                database.execSQL("UPDATE voca SET category = 'en' WHERE category = '영어'")
            }
        }
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 1. Vocabulary 테이블 생성
                database.execSQL("""
                    CREATE TABLE vocabulary (
                        id TEXT PRIMARY KEY NOT NULL,
                        title TEXT NOT NULL,
                        category TEXT NOT NULL,
                        description TEXT NOT NULL DEFAULT ''
                    )
                """)

                // 2. VocaWord 테이블에 vocabularyId 컬럼 추가
                database.execSQL("ALTER TABLE voca ADD COLUMN vocabularyId TEXT NOT NULL DEFAULT ''")

                // 3. 기존 단어가 있으면 기본 단어장 생성
                val cursor = database.query("SELECT COUNT(*) FROM voca")
                cursor.moveToFirst()
                val wordCount = cursor.getInt(0)
                cursor.close()

                if (wordCount > 0) {
                    val defaultVocabId = "default-vocab"
                    database.execSQL("""
                        INSERT INTO vocabulary VALUES (
                            '$defaultVocabId',
                            '기존 단어장',
                            '영어',
                            '기존에 저장된 단어들'
                        )
                    """)

                    // 4. 기존 모든 단어들을 기본 단어장에 할당
                    database.execSQL("""
                        UPDATE voca SET vocabularyId = '$defaultVocabId'
                    """)
                }
            }
        }
    }

    @Singleton
    @Provides
    fun provideWeeklyGoalDatabase(app: Application) =
        Room.databaseBuilder(app, WeeklyGoalDatabase::class.java, "weekly_goal_db")
            .build()
}