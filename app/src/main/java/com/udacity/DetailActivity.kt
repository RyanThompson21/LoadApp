package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val fileName = findViewById<TextView>(R.id.filename_text)
        val status = findViewById<TextView>(R.id.status_text)
        val notifId = intent.extras?.getInt(NOTIF_ID_KEY)

        val floatButton = findViewById<FloatingActionButton>(R.id.fab)
        floatButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        fileName.text = intent.extras?.getString(FILE_KEY)

        val statusText = intent.extras?.getString(STATUS_KEY)
        if (statusText != "Success") {
            status.setTextColor(Color.RED)
        }
        else {
            status.setTextColor(Color.GREEN)
        }
        status.text = statusText

        val notifManager = getSystemService(NotificationManager::class.java) as NotificationManager

        notifManager.cancel(notifId!!)

    }
    private fun back() {
        onSupportNavigateUp()
    }
    override fun onBackPressed() {
        onSupportNavigateUp()
    }

}
