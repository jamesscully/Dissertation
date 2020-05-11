package com.scully.server;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class SendActionTask extends AsyncTask<String, Void, Boolean> {

    private ObjectOutputStream outputStream;

    public SendActionTask(ObjectOutputStream o) {
        outputStream = o;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            outputStream.writeUTF(strings[0]);
            outputStream.flush();
        } catch (IOException e) {
            System.err.println("SendActionTask: IO Exception occured");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }
}
