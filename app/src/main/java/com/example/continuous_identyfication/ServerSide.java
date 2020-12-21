package com.example.continuous_identyfication;

import android.content.Context;
import android.os.FileObserver;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.security.Provider;
import java.util.List;

public class ServerSide {

    private final CommunicationModule communicationModule;
    private final Context context;
    public FileObserver fileObserver;
    private File file;
    private ModelData oldModel;
    private ModelData newModel;


    public ServerSide(Context context, CommunicationModule communicationModule) {
        file = new File(context.getExternalFilesDir(null), "toServer.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileObserver = new FileObserver(file.getPath(), FileObserver.CLOSE_WRITE) {
            @Override
            public void onEvent(int event, @Nullable String path) {
                handleCommunication();
            }
        };
        fileObserver.startWatching();
        this.communicationModule = communicationModule;
        this.context = context;
        this.oldModel = ModelData.readOldModel(context);
        newModel = new ModelData();
    }

    private void handleCommunication() {
        List<EventData> eventDataList = ReadFromFile.read(file.getPath());
        for (EventData eventData : eventDataList
        ) {
            this.newModel.addNewData(eventData);
        }
        if (this.oldModel.firstTime) {
            oldModel = newModel;
            return;
        }
        if (newModel.isLegible) {
            if (this.newModel.compareWithModel(oldModel) == 2) {
                communicationModule.sendDataToClient(1);
                oldModel = newModel;
                newModel = newModel.removeNumberOfRecords(50);
            } else {
                communicationModule.sendDataToClient(0);
            }
        }
    }
}
