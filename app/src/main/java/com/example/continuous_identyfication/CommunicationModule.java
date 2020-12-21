package com.example.continuous_identyfication;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CommunicationModule {
    private int userId;
    private Context context;

    public CommunicationModule(int userId, Context context) {
        this.userId = userId;
        this.context = context;
    }

    public void sendDataToServer(EventData eventData) {
        String dataBuffer = eventData.eventId + "; " + eventData.positionsX.toString().substring(1, +eventData.positionsX.toString().length() - 1) + "; " + eventData.positionsY.toString().substring(1, +eventData.positionsY.toString().length() - 1) + "; " + eventData.pressure.toString().substring(1, +eventData.pressure.toString().length() - 1) + "; " + eventData.fingerSize.toString().substring(1, +eventData.fingerSize.toString().length() - 1) + "; " + String.valueOf((Long) eventData.time) + "\n";
        sendToServer(dataBuffer, this.context);
        writeToFile(dataBuffer, this.context);
    }

    public void sendDataToClient(int message) {
        sendToClient(String.valueOf(message), this.context);
    }

    private void writeToFile(String data, Context context) {
        try {
            File file = new File(context.getExternalFilesDir(null), "rawData.txt");
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            System.out.println(context.getFilesDir().toString());
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void sendToServer(String data, Context context) {
        try {
            File file = new File(context.getExternalFilesDir(null), "toServer.txt");
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            System.out.println(context.getExternalFilesDir(null));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void sendToClient(String data, Context context) {
        try {
            File file = new File(context.getExternalFilesDir(null), "toClient.txt");
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            System.out.println(context.getExternalFilesDir(null));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


}
