package com.GoMobeil.H2H.Services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.GoMobeil.H2H.Home
import com.GoMobeil.H2H.R
import com.GoMobeil.H2H.StaticRefs
import org.json.JSONObject


/**
 * Created by harshagulati on 25/07/17.
 */




class PushReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        var notificationTitle = "Pushy"
        var notificationText = "Test notification"

        // Attempt to extract the "message" property from the payload: {"message":"Hello World!"}
        if (intent.getStringExtra(StaticRefs.NOTIFICATIONTYPE) != null) {
                val notifciationtype=intent.getStringExtra(StaticRefs.NOTIFICATIONTYPE)
            if(notifciationtype.equals(StaticRefs.NOTIFICATIONMESSAGE)){
                if(intent.getStringExtra(StaticRefs.NOTIFICATIONDATA)!=null){
                    val notificationdata= JSONObject(intent.getStringExtra(StaticRefs.NOTIFICATIONDATA))
                    if(notificationdata.length()>0){
                        val sendertype=notificationdata.getString(StaticRefs.CHATSENDER)
                        val chatid=notificationdata.getString(StaticRefs.CHAT_ID)
                        val chatmessage=notificationdata.getString(StaticRefs.CHATMESSAGE)
                        val chattxnid=notificationdata.getString(StaticRefs.CHATTXNID)
                        val chatcustomerid=notificationdata.getString(StaticRefs.CHATCUSTOMERID)
                        val chatvendorid=notificationdata.getString(StaticRefs.CHATVENDORID)
                        if(sendertype.equals("V")){
                            notificationTitle=notificationdata.getString(StaticRefs.VENDORNAME).toString()
                            notificationText=chatmessage
                        }
                        else{
                            notificationTitle="Message from Customer"
                        }
                        // Prepare a notification with vibration, sound and lights
                   /*    val intent = Intent(context,Message::class.java)
                        intent.putExtra(StaticRefs.KEY,StaticRefs.NOTIFICATIONTYPE)
                        intent.putExtra(StaticRefs.CHATTXNID,chattxnid)
                        intent.putExtra(StaticRefs.CHATCUSTOMERID,chatcustomerid)
                        intent.putExtra(StaticRefs.CHATVENDORID,chatvendorid)*/

                        val builder = NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.userid_icon)
                                .setContentTitle(notificationTitle)
                                .setContentText(notificationText)
                                .setLights(Color.RED, 1000, 1000)
                                .setVibrate(longArrayOf(0, 400, 250, 400))
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setContentIntent(PendingIntent.getActivity(context,1, Intent(context,Home::class.java),PendingIntent.FLAG_ONE_SHOT))
                        // .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, Schedule.class), PendingIntent.FLAG_UPDATE_CURRENT));

                        // Get an instance of the NotificationManager service
                        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                        // Build the notification and display it
                        notificationManager.notify(0, builder.build())
                    }
                    else{
                        Log.d("Messaging","DATA IS NULL")
                    }


                }else{
                    Log.d("Messaging","No Data For Message")
                }
            }

         //   notificationText = intent.getStringExtra("message")


        }



    }
}

