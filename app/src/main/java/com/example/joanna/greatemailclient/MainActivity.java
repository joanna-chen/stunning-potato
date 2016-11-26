package com.example.joanna.greatemailclient;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.app.ProgressDialog;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Button button;
    GMailSender sender;
    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Log.i("[BroadcastReceiver]", "Screen ON");
            }
            else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                Log.i("[BroadcastReceiver]", "Screen OFF");
            }
            else if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                Log.i("[BroadcastReceiver]", "User PRESENT");
                try {
                    sendEmail();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), 100).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerReceiver(br, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(br, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        registerReceiver(br, new IntentFilter(Intent.ACTION_USER_PRESENT));

        button = (Button) findViewById(R.id.send);
        // Add your mail Id and Password
        sender = new GMailSender("srdjanmk8@gmail.com", "atkjypvjaoaqhbsm");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
                Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                try {
                        sendEmail();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), 100).show();
                }

            }
        });

    }

    public void sendEmail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new EmailTask().execute();
            }
        });
    }

    /*public ArrayList<String> getContactEmails(){
        ArrayList<String> emails = new ArrayList<String>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null); //get all contacts table
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur2 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                while (cur2.moveToNext()) {
                    String email = cur2.getString(cur2.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    Log.e("Email", email);
                    if(email!=null){
                        emails.add(email);
                    }
                }
                cur2.close();
            }
        }
        return emails;
    }*/ //SECRET MEMES (delet this)

    public String getCancerQuote(){
        String[] CancerQuotes = {"Live.Laugh.Love.<33",
                "Legalize ranch",
                "Where are my testicles, Summer?",
                "doot doot, thank mr skeltal", "Dying Is MainStream #MONEY",
                "How Can Mirrors Be Real If Our Eyes Aren't Real",
                "\"It's Your Birthday\" Mateo Said. I Didn't Respond. \"Are You Not Excited To Be 15\" He Asked. Reading My Book I Uttered \"I Turned 15 Long Ago\"",
                "If A Book Store Never Runs Out Of A Certain Book, Dose That Mean That Nobody Reads It, Or Everybody Reads It",
                "People Use To Ask Me What Do You Wanna Be When You Get Older And I Would Say What A Stupid Question The Real Question Is What Am I Right Now"
        };
        return CancerQuotes[(int)(Math.random() * 6)];
    }

    class EmailTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {
                // Add subject, Body, your mail Id, and receiver mail Id.
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                //getContactEmails(); this is pretty spooky tbh
                sender.sendMail("You seem pretty chill. Can I have your credit card number?", getCancerQuote(), "srdjanmk8@gmail.com", "sirjelly88@gmail.com"/*, justin.the.c@gmail.com,jerryliu55@gmail.com,msjoannac@gmail.com"*/);
            }

            catch (Exception ex) {
                Log.e("SendMail", ex.getMessage(), ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            Toast.makeText(getApplicationContext(), "Email send", 100).show();
        }
    }
}
