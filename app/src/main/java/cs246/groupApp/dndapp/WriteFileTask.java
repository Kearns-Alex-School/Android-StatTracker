package cs246.groupApp.dndapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;


//To use: pass in an array of strings, with the first one being the filename to write and the second
//          being the JSON serialized object to write.


public class WriteFileTask extends AsyncTask<String, Integer, Void> {
    private WeakReference<Context> context;
    private WeakReference<ProgressBar> progressBar;

    public WriteFileTask(Context context, ProgressBar progressBar) {
        this.context =  new WeakReference<>(context);
        this.progressBar = new WeakReference<>(progressBar);
    }

    @Override
    protected void onPreExecute() {
        progressBar.get().setProgress(0);

        Toast.makeText(context.get(), "Saving...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(String... file) {
        String filename = file[0];

        try (FileOutputStream outputStream = context.get().openFileOutput(filename, Context.MODE_PRIVATE)) {
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
