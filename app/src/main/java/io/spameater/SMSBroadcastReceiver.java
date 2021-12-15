package io.spameater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;

import io.spameater.ui.home.HomeFragment;

public class SMSBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final PendingResult pendingResult = goAsync();
        Task asyncTask = new Task(pendingResult, intent,context);
        asyncTask.execute();
    }

    private static class Task extends AsyncTask<String, Integer, String> {

        private final PendingResult pendingResult;
        private final Intent intent;
        private final Context context;
        private Task(PendingResult pendingResult, Intent intent,Context context) {
            this.pendingResult = pendingResult;
            this.intent = intent;
            this.context=context;
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();//用于存储短信内容
            String sender = null;//存储短信发送方手机号
            Bundle bundle = intent.getExtras();//通过getExtras()方法获取短信内容
            String format = intent.getStringExtra("format");
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");//根据pdus关键字获取短信字节数组，数组内的每个元素都是一条短信
                for (Object object : pdus) {
                    SmsMessage message=SmsMessage.createFromPdu((byte[])object,format);//将字节数组转化为Message对象
                    sender = message.getOriginatingAddress();//获取短信手机号
                    content.append(message.getMessageBody());//获取短信内容
                }
            }
            boolean isSpam=false;

            try {
                File directory = context.getFilesDir();
                File file = new File(directory+"/spam/");
                String[] files=file.list();
                WriteAndRead.save(sender,content.toString(),files.length+1,context);


            } catch (FileNotFoundException | JSONException e) {
                e.printStackTrace();
            }
            if (isSpam==true){

            }

            HomeFragment.addr.add(sender);
            HomeFragment.body.add(content.toString());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Must call finish() so the BroadcastReceiver can be recycled.
            pendingResult.finish();
        }
    }
}
