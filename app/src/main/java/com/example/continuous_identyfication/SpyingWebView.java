package com.example.continuous_identyfication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.FileObserver;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;


public class SpyingWebView extends WebView {

    private final Context context;
    private final ServerSide server;
    private final CommunicationModule communicationModule;
    public FileObserver fileObserver;
    private int idCounter;
    private EventData dataRecord;
    private File file;
    private Toast toastYes;
    private Toast toastNo;
    private SharedPreferences.Editor preferencesEditor;


    public SpyingWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.communicationModule = new CommunicationModule(1, context);
        this.server = new ServerSide(context, communicationModule);
    }

    public SpyingWebView(MainActivity mainActivity) {
        super(mainActivity);
        this.context = mainActivity;
        this.communicationModule = new CommunicationModule(1, context);
        this.server = new ServerSide(context, communicationModule);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferencesEditor = preferences.edit();
        idCounter = preferences.getInt("ID",0);

        file = new File(context.getExternalFilesDir(null), "toClient.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileObserver = new FileObserver(file.getPath(), FileObserver.CLOSE_WRITE) {
            @Override
            public void onEvent(int event, @Nullable String path) {
                handleCommunication(path);
            }
        };
        fileObserver.startWatching();
        toastYes = Toast.makeText(this.context, "Everything is alright", Toast.LENGTH_LONG);
        toastNo = Toast.makeText(this.context, "Hmmmmmmmmmmmm, something's off", Toast.LENGTH_LONG);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        analizeTouchEvent(event);
        super.onTouchEvent(event);
        return true;
    }


    private void analizeTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0: {
                this.dataRecord = new EventData(++idCounter, event.getX(), event.getY(), event.getPressure(), event.getTouchMajor(), event.getDownTime());
                preferencesEditor.putInt("ID",idCounter);
                preferencesEditor.commit();
                return;
            }
            case 2: {
                //Dodanie wartości do bufora
                this.dataRecord.appendData(event.getX(), event.getY(), event.getPressure(), event.getTouchMajor(), event.getDownTime());
                return;
            }
            case 1: {
                //Dodanie wartości i wysłanie
                this.dataRecord.appendData(event.getX(), event.getY(), event.getPressure(), event.getTouchMajor(), event.getDownTime());
                this.dataRecord.time = SystemClock.uptimeMillis() - this.dataRecord.time;
                communicationModule.sendDataToServer(this.dataRecord);
                return;
            }
        }
    }

    private void handleCommunication(String path) {
        System.out.println("Client received a message");
        try (Scanner scanner = new Scanner(new File(path));) {
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().trim().equals("0")) {
                    toastNo.show();
                } else {
                    toastYes.show();
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Empty file");
            e.printStackTrace();
        }
    }
}