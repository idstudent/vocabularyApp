package com.ljyVoca.vocabularyapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ljyVoca.vocabularyapp.view.MainActivity

class StudyReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        sendLocalNotification("테스트 알림!", "FCM 테스트 중입니다!")

        val sharedPrefs = applicationContext.getSharedPreferences("study_prefs", Context.MODE_PRIVATE)
        val lastStudyDate = sharedPrefs.getLong("last_study_date", 0L)
        val currentTime = System.currentTimeMillis()

        val daysSinceLastStudy = (currentTime - lastStudyDate) / (24 * 60 * 60 * 1000)

        when {
            daysSinceLastStudy >= 7 -> {
                sendLocalNotification("7일간 복습하지 않았어요!", "단어 복습하러 가볼까요?")
            }
            daysSinceLastStudy >= 3 -> {
                sendLocalNotification("3일간 복습하지 않았어요!", "잠깐 복습하고 가세요!")
            }
        }

        return Result.success()
    }

    private fun sendLocalNotification(title: String, body: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager // 수정

        // 알림 채널 생성
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "quiz_reminder",
                "퀴즈 복습 알림",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java) // 수정
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE // 수정
        )

        val notification = NotificationCompat.Builder(applicationContext, "quiz_reminder") // 수정
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}