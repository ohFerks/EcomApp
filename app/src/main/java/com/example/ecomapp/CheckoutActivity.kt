package com.example.ecomapp

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class CheckoutActivity : AppCompatActivity() {

    private lateinit var countdownMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        countdownMessage = findViewById(R.id.countdownMessage)

        // Настраиваем таймер на 5 секунд
        val countdownTime = 5000L // 5 секунд в миллисекундах
        val timer = object : CountDownTimer(countdownTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Обновляем текст с оставшимся временем
                val secondsLeft = millisUntilFinished / 1000
                countdownMessage.text = "Вы будете возвращены на страницу продуктов через: $secondsLeft секунд"
            }

            override fun onFinish() {
                // По завершении отсчёта переходим на страницу GalleryOfProduct
                val intent = Intent(this@CheckoutActivity, GalleryOfProductsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }

        // Запускаем таймер
        timer.start()
    }
}
