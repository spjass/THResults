package fi.tamk.tiko.th_results.dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;

import fi.tamk.tiko.th_results.tournament.Tournament;

/**
 * Class for uploading files to dropbox.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class UploadFileToDropbox extends AsyncTask<Void, Void, Boolean> {

    private DropboxAPI<?> dropbox;
    private String path;
    private Context context;
    private Tournament tournament;
    HTMLGenerator generator;
    String filename;

    public UploadFileToDropbox(Context context, DropboxAPI<?> dropbox,
                               String path, Tournament tournament) {
        this.context = context.getApplicationContext();
        this.dropbox = dropbox;
        this.path = path;
        this.tournament = tournament;
        filename = tournament.getName().replace(" ", "_");
        filename = filename.replace(".", "_");

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        final File tempDir = context.getCacheDir();
        File tempFile;
        FileWriter fr;
        generator = new HTMLGenerator(filename+".html", tournament);

        try {
            tempFile = generator.getFile();


            FileInputStream fileInputStream = new FileInputStream(tempFile);
            dropbox.putFile(path + filename + ".html", fileInputStream,
                    tempFile.length(), null, null);
            tempFile.delete();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DropboxException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Toast.makeText(context, "File Uploaded Sucesfully!",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Failed to upload file", Toast.LENGTH_LONG)
                    .show();
        }
    }
}
