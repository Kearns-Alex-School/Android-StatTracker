package cs246.groupApp.dndapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;


//To use: pass in an array of strings, with the first one being the filename to write and the second
//          being the JSON serialized object to write.


public class WriteFileTask extends AsyncTask<String, Integer, Void> {
    private WeakReference<Context> context;
    private WeakReference<ProgressBar> progressBar;
    private File dir;

    public WriteFileTask(Context context, ProgressBar progressBar, File dir) {
        if (context != null && progressBar != null) {
            this.context = new WeakReference<>(context);
            this.progressBar = new WeakReference<>(progressBar);
        }
        this.dir = dir;
    }

    @Override
    protected void onPreExecute() {
        if (progressBar != null) {
            progressBar.get().setProgress(0);

            Toast.makeText(context.get(), "Saving...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Void doInBackground(String... file) {
        File outputFile = new File(dir, file[0]);

        try {
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(file[1].getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(context.get(), "Saved!", Toast.LENGTH_SHORT).show();
    }
}
