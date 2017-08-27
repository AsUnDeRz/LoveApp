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
                val id = intent.getIntExtra(KEYPREFER.NOTIID,0)
                d{"check id [$id]"}
                if (ACTION_START == action) {
                    processStartNotification(id)
                }
            } finally {
                WakefulBroadcastReceiver.completeWakefulIntent(intent!!)
            }
        }

        private fun processDeleteNotification(intent: Intent) {
            // Log something?
        }

        private fun processStartNotification(id:Int) {
            d{"StartNotification"}
            val appDb = AppDatabase(this)
            val noti = appDb.getNotiWithState(id.toString())
            appDb.updateNotification(noti,KEYPREFER.MISSING)
            // Do something. For example, fetch fresh data from backend to create a rich notification?
            val builder = NotificationCompat.Builder(this)
            builder.setContentTitle(noti.title)
                    .setAutoCancel(true)
                    .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                    .setColor(resources.getColor(R.color.colorAccent))
                    .setContentText(noti.message)
                    .setSmallIcon(R.drawable.icon_app)

            val mainIntent = Intent(this, ReminderActivity::class.java)
            mainIntent.putExtra(KEYPREFER.NOTIID,id)
            val pendingIntent = PendingIntent.getActivity(this,
                    id,
                    mainIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentIntent(pendingIntent)
            builder.setDeleteIntent(EventReceiver.getDeleteIntent(this,id))

            val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(id, builder.build())
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

            private val ACTION_START = "ACTION_START"
            private val ACTION_DELETE = "ACTION_DELETE"

            fun createIntentStartNotificationService(context: Context,id: Int): Intent {
                val intent = Intent(context, Service::class.java)
                intent.action = ACTION_START
                intent.putExtra(KEYPREFER.NOTIID,id)
                return intent
            }

            fun createIntentDeleteNotification(context: Context,id: Int): Intent {
                val intent = Intent(context, Service::class.java)
                intent.action = ACTION_DELETE
                intent.putExtra(KEYPREFER.NOTIID,id)
                return intent
            }
        }
    }


    class ServiceStarterReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            d{"onReceive with ServiceStarterReceiver"}
            EventReceiver.setupAlarm(context, Calendar.getInstance(),intent.getIntExtra(KEYPREFER.NOTIID,0))
        }
    }

    class EventReceiver : WakefulBroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val id = intent.getIntExtra(KEYPREFER.NOTIID,0)
            var serviceIntent: Intent? = null
            if (ACTION_START_NOTIFICATION_SERVICE == action) {
                d{"onReceive from alarm, starting notification service with id [$id]"}
                serviceIntent = Service.createIntentStartNotificationService(context,id)
            } else if (ACTION_DELETE_NOTIFICATION == action) {
               d{"onReceive delete notification action, starting notification service to handle delete with id [$id]"}
                serviceIntent = Service.createIntentDeleteNotification(context,id)
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

            fun setupAlarm(context: Context,cal:Calendar,id:Int) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val alarmIntent = getStartPendingIntent(context,id)
                d{"on set Alarm notification with ID [$id]"}
                d{"on set Alarm ["+cal.time.toString()+"]"}
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis,alarmIntent)
            }


            fun cancelAlarm(context: Context,id: Int) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val alarmIntent = getStartPendingIntent(context,id)
                d{"on Cancel Alarm notification with ID [$id]"}
                alarmManager.cancel(alarmIntent)
            }


            private fun setNextTimeAlert() :Long{
                val sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
                val dateInString = "18-08-2017 16:50:00"
                val date = sdf.parse(dateInString)
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

            private fun getStartPendingIntent(context: Context,id:Int): PendingIntent {
                val intent = Intent(context, EventReceiver::class.java)
                intent.putExtra(KEYPREFER.NOTIID,id)
                intent.action = ACTION_START_NOTIFICATION_SERVICE
                d{"StartPendingIntent with ID [$id]"}
                return PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            fun getDeleteIntent(context: Context,id: Int): PendingIntent {
                val intent = Intent(context, EventReceiver::class.java)
                intent.putExtra(KEYPREFER.NOTIID,id)
                intent.action = ACTION_DELETE_NOTIFICATION
                d{"DeletePendingIntent with ID [$id]"}
                return PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }

}
