package com.udacity

import android.app.DownloadManager
import android.app.DownloadManager.COLUMN_STATUS
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

var radioSelected = false
var downloadCompleted = false

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var loadingButton: LoadingButton
    private lateinit var notificationManager: NotificationManager
    private var fileName = ""
    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        radioSelected = false

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(CHANNEL_ID, getString(R.string.notif_channel_name))

        loadingButton = findViewById(R.id.custom_button)
        loadingButton.setOnClickListener {
            if(radioSelected){
            download()
            }
            else {
                Toast.makeText(
                    this,
                    getString(R.string.no_radio),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun onRadioClick(view: View) {
        // make sure a radio button is selected
        if (view is RadioButton) {
            if (view.isChecked){
                radioSelected = true
            }
                when (view.id) {
                R.id.glide_button -> {
                    url = glide_url
                    fileName = getString(R.string.radioOption1)
                }
                R.id.loadapp_button -> {
                    url = loadApp_URL
                    fileName = getString(R.string.radioOption2)
                }
                else -> {
                    url = retrofit_url
                    fileName = getString(R.string.radioOption3)
                }
            }
        }
    }



    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            var notifText = getString(R.string.download_failed)

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent?.action) {
                downloadCompleted = true
            }

            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val cursor = dm.query(DownloadManager.Query().setFilterById(downloadID))

            if (cursor.moveToFirst()){
                val status = cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS))
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    notifText = getString(R.string.download_success)
                }
            }
            cursor.close()

            notificationManager.sendNotification(this@MainActivity, id!!, notifText, fileName)
        }
    }

    private fun download() {
        // make sure completed flag always starts at false
        downloadCompleted = false

        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val loadApp_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val glide_url = "https://github.com/bumptech/glide"
        private const val retrofit_url = "https://github.com/square/retrofit"
    }

}
