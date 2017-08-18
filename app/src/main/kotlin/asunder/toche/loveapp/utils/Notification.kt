package asunder.toche.loveapp

import android.content.Intent
import android.app.PendingIntent
import android.app.AlarmManager
import android.content.Context
import android.support.v4.content.WakefulBroadcastReceiver
import android.util.Log
import java.util.*
import android.content.BroadcastReceiver
import android.media.RingtoneManager
import android.media.Ringtone
import android.app.NotificationManager
import android.app.IntentService
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import com.github.ajalt.timberkt.Timber.d
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


/**
 * Created by admin on 8/18/2017 AD.
 */
object  Notification {


    class Service : IntentService(Service::class.java.simpleName) {

        override fun onHandleIntent(intent: Intent?) {
            Log.d(javaClass.simpleName, "onHandleIntent, started handling a notification event")
            try {
                val action = intent!!.action
                if (ACTION_START == action) {
                    processStartNotification()
                }
            } finally {
                WakefulBroadcastReceiver.completeWakefulIntent(intent!!)
            }
        }

        private fun processDeleteNotification(intent: Intent) {
            // Log something?
        }

        private fun processStartNotification() {
            // Do something. For example, fetch fresh data from backend to create a rich notification?

            val builder = NotificationCompat.Builder(this)
            builder.setContentTitle("Scheduled Notification")
                    .setAutoCancel(true)
                    .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                    .setColor(resources.getColor(R.color.colorAccent))
                    .setContentText("This notification has been triggered by Notification Service")
                    .setSmallIcon(R.drawable.icon_app)

            val mainIntent = Intent(this, NotificationActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this,
                    NOTIFICATION_ID,
                    mainIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentIntent(pendingIntent)
            builder.setDeleteIntent(EventReceiver.getDeleteIntent(this))

            val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(NOTIFICATION_ID, builder.build())
            playNotificationSound()

        }

        private fun playNotificationSound() {
            try {
                val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val r = RingtoneManager.getRingtone(applicationContext, notification)
                r.play()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        companion object {

            private val NOTIFICATION_ID = 1
            private val ACTION_START = "ACTION_START"
            private val ACTION_DELETE = "ACTION_DELETE"

            fun createIntentStartNotificationService(context: Context): Intent {
                val intent = Intent(context, Service::class.java)
                intent.action = ACTION_START
                return intent
            }

            fun createIntentDeleteNotification(context: Context): Intent {
                val intent = Intent(context, Service::class.java)
                intent.action = ACTION_DELETE
                return intent
            }
        }
    }


    class ServiceStarterReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            EventReceiver.setupAlarm(context, Calendar.getInstance())
        }
    }

    class EventReceiver : WakefulBroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            var serviceIntent: Intent? = null
            if (ACTION_START_NOTIFICATION_SERVICE == action) {
                Log.i(javaClass.simpleName, "onReceive from alarm, starting notification service")
                serviceIntent = Service.createIntentStartNotificationService(context)
            } else if (ACTION_DELETE_NOTIFICATION == action) {
                Log.i(javaClass.simpleName, "onReceive delete notification action, starting notification service to handle delete")
                serviceIntent = Service.createIntentDeleteNotification(context)
            }

            if (serviceIntent != null) {
                // Start the service, keeping the device awake while it is launching.
                startWakefulService(context, serviceIntent)
            }
        }

        companion object {

            private val ACTION_START_NOTIFICATION_SERVICE = "ACTION_START_NOTIFICATION_SERVICE"
            private val ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION"

            private val NOTIFICATIONS_INTERVAL_IN_HOURS = 2

            fun setupAlarm(context: Context,cal:Calendar) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val alarmIntent = getStartPendingIntent(context)
                /*
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        getTriggerAt(Date()),
                        setNextTimeAlert(),
                        alarmIntent)
                        */
                d{"setDate ["+ setNextTimeAlert()+"]"}
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis,alarmIntent)
            }

            fun cancelAlarm(context: Context) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val alarmIntent = getStartPendingIntent(context)
                alarmManager.cancel(alarmIntent)
            }

            private fun setNextTimeAlert() :Long{
                var sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
                var dateInString = "18-08-2017 16:50:00"
                var date = sdf.parse(dateInString)
                var calendar = Calendar.getInstance()
                calendar.time = date
                return calendar.timeInMillis
            }

            private fun getTriggerAt(now: Date): Long {
                val calendar = Calendar.getInstance()
                calendar.time = now
                //calendar.add(Calendar.HOUR, NOTIFICATIONS_INTERVAL_IN_HOURS);
                d{"Current long ["+calendar.timeInMillis+"]"}
                return calendar.timeInMillis
            }

            private fun getStartPendingIntent(context: Context): PendingIntent {
                val intent = Intent(context, EventReceiver::class.java)
                intent.action = ACTION_START_NOTIFICATION_SERVICE
                return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            fun getDeleteIntent(context: Context): PendingIntent {
                val intent = Intent(context, EventReceiver::class.java)
                intent.action = ACTION_DELETE_NOTIFICATION
                return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }

}
