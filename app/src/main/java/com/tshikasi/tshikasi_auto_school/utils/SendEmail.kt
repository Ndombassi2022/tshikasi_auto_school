package com.tshikasi.tshikasi_auto_school.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.tshikasi.tshikasi_auto_school.R
import com.tshikasi.tshikasi_auto_school.domain.model.SendEmailsModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

fun SendEmail(sendEmailsModel : SendEmailsModel) {

    CoroutineScope(Dispatchers.IO).launch {
        // SMTP server details
        val host = "smtp.gmail.com"
        val port = "465"
        val username = "tshikasiplatform@gmail.com"
        val password =
            "ccvfmqqyaiwrxgov " // user the apps password not your real gmail password

        // Email recipient
        val to = "ndombassi9@hotmail.com"

        // Configure SMTP properties
        val props = Properties()
        props["mail.smtp.auth"] = "true"
        // props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.host"] = host
        props["mail.smtp.port"] = port

        // Create a session with authentication
        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        try {
            // Create a new MimeMessage
            val message = MimeMessage(session)
            message.setFrom(InternetAddress("tshikasi"))
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(sendEmailsModel.from)
            )
            message.subject = sendEmailsModel.subject
            message.setText("\n" + sendEmailsModel.content)

            // Send the message using the Transport class
            Transport.send(message)

            // Perform UI operations on the Main dispatcher
            CoroutineScope(Dispatchers.Main).launch {
                // Display a success toast message
                Toast.makeText(sendEmailsModel.context, "Sent Successfully.", Toast.LENGTH_LONG)
                    .show()

                // Invoke the onSuccess callback function
                //  onSuccess.invoke()
            }
        } catch (e: MessagingException) {
            e.printStackTrace()

            // Perform UI operations on the Main dispatcher
            CoroutineScope(Dispatchers.Main).launch {
                // Display an error toast message
                Toast.makeText(sendEmailsModel.context, "Problem exists: $e", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}


fun SendNotificationMyself(titulo: String, mensagem: String,context: Context) {
    //val context = getApplication<Application>().applicationContext
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val channelId = "meu_canal_id"

    // Cria canal para Android 8+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val canal = NotificationChannel(
            channelId,
            "Canal de Notificação",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Canal usado para notificações da aplicação"
        }
        notificationManager.createNotificationChannel(canal)
    }

    val notificacao = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.message) // substitua por um ícone válido
        .setContentTitle(titulo)
        .setStyle(NotificationCompat.BigTextStyle().bigText(mensagem))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    notificationManager.notify(System.currentTimeMillis().toInt(), notificacao)
}


