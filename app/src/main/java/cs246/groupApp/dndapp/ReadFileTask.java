package cs246.groupApp.dndapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

// To use: pass in a string for the filename, and get a string of serialized JSON back.
//          return functionality is WIP

public class ReadFileTask extends AsyncTask<String, Integer, String> {
    private WeakReference<Context> contextReference;
    private WeakReference<ProgressBar> progressBar;
    private File dir;

    public ReadFileTask(Context context, ProgressBar progressBar, File dir) {
        if (context != null && progressBar != null) {

            this.contextReference = new WeakReference<>(context);
            this.progressBar = new WeakReference<>(progressBar);
        }
            this.dir = dir;
    }

    @Override
    protected void onPreExecute() {
        if (progressBar != null) {
            progressBar.get().setProgress(0);

            Toast.makeText(contextReference.get(), "Loading...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        String contentJson = "";
        File inputFile = new File(dir, strings[0]);

        Context context = contextReference.get();

        if (context != null) {

            try {
                FileInputStream inputStream = new FileInputStream(inputFile);
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    contentJson = reader.toString(); //TODO test this
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return contentJson;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String string) {
        //add a callback function here, see 2nd answer here: https://stackoverflow.com/questions/10972114/how-to-get-a-string-back-from-asynctask

        Toast.makeText(contextReference.get(), "Loaded!", Toast.LENGTH_SHORT).show();

        MainActivity.deserialize(dir, string, contextReference.get());
    }
}
