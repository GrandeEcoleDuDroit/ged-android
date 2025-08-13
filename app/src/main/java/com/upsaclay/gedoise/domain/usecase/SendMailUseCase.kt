package com.upsaclay.gedoise.domain.usecase

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import com.upsaclay.gedoise.R
import android.widget.Toast

class SendMailUseCase(private val context: Context) {
    operator fun invoke(subject : String?, message : String?){
        val intent = Intent(Intent.ACTION_SEND)
        intent.setDataAndType("mailto:".toUri(),"text/plain")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("application.ged@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Important!
        val chooserIntent = Intent.createChooser(intent, context.getString(R.string.send_mail_via))
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Also important for the chooser
        try {
            startActivity(context, chooserIntent, null)
        } catch (e: Exception){
            Toast.makeText(context, context.getString(R.string.no_email_app_found), Toast.LENGTH_LONG).show()
        }

    }

}
