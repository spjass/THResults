package fi.tamk.tiko.th_results;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.dropbox.client2.session.TokenPair;

import java.util.ArrayList;

import fi.tamk.tiko.th_results.database.Loader;
import fi.tamk.tiko.th_results.dropbox.UploadFileToDropbox;
import fi.tamk.tiko.th_results.tournament.Tournament;

/**
 * Activity class for Dropbox login and file uploading.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class DropboxActivity extends Activity implements View.OnClickListener {

    private DropboxAPI<AndroidAuthSession> dropbox;

    private final static String FILE_DIR = "/Tournaments/";
    private final static String DROPBOX_NAME = "dropbox_prefs";
    private final static String ACCESS_KEY = "sxr07nv0mdoyf1k";
    private final static String ACCESS_SECRET = "0xb7q7fz9g28xp1";
    private boolean isLoggedIn;
    private Button logIn;
    private Button uploadFile;
    private Button listFiles;
    private LinearLayout container;
    ArrayList<Tournament> tournamentsList;
    TournamentAdapter adapterTournaments2;
    ListView tournamentsListView;
    Loader loader;
    Tournament tournament;
    /**
     * Initializes variables and Dropbox preferences when activity is created
     *
     * @param savedInstanceState Saved bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropbox);

        logIn = (Button) findViewById(R.id.dropbox_login);
        logIn.setOnClickListener(this);
        uploadFile = (Button) findViewById(R.id.upload_file);
        uploadFile.setOnClickListener(this);
        container = (LinearLayout) findViewById(R.id.container_files);

        loggedIn(false);

        AndroidAuthSession session;
        AppKeyPair pair = new AppKeyPair(ACCESS_KEY, ACCESS_SECRET);

        SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, 0);
        String key = prefs.getString(ACCESS_KEY, null);
        String secret = prefs.getString(ACCESS_SECRET, null);

        if (key != null && secret != null) {
            AccessTokenPair token = new AccessTokenPair(key, secret);
            session = new AndroidAuthSession(pair, Session.AccessType.APP_FOLDER, token);
        } else {
            session = new AndroidAuthSession(pair, Session.AccessType.APP_FOLDER);
        }

        dropbox = new DropboxAPI<AndroidAuthSession>(session);
        loader = new Loader(getApplicationContext());


        tournamentsList = loader.loadTournaments();
        tournamentsListView = new ListView(getApplicationContext());
        adapterTournaments2 = new TournamentAdapter(getApplicationContext(), R.layout.tournamentlist_item, tournamentsList);
        tournamentsListView.setAdapter(adapterTournaments2);
        tournamentsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        tournamentsListView.setBackgroundColor(getApplicationContext()
                .getResources().getColor(R.color.darkgray));
    }
    /**
     * onResume() method of the activity.
     */
    @Override
    protected void onResume() {
        super.onResume();

        AndroidAuthSession session = dropbox.getSession();
        if (session.authenticationSuccessful()) {
            try {
                session.finishAuthentication();

                TokenPair tokens = session.getAccessTokenPair();
                SharedPreferences prefs = getSharedPreferences(DROPBOX_NAME, 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(ACCESS_KEY, tokens.key);
                editor.putString(ACCESS_SECRET, tokens.secret);
                editor.commit();

                loggedIn(true);
            } catch (IllegalStateException e) {
                Toast.makeText(this, "Error during Dropbox authentication",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * Sets users login status
     *
     * @param isLogged True if logged in, false if not
     */
    public void loggedIn(boolean isLogged) {
        isLoggedIn = isLogged;
        uploadFile.setEnabled(isLogged);

        logIn.setText(isLogged ? "Log out" : "Log in");
    }


    /**
     * Handles button clicks inside the activity.
     *
     * @param v Clicked View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dropbox_login:

                if (isLoggedIn) {
                    dropbox.getSession().unlink();
                    loggedIn(false);
                } else {
                    dropbox.getSession().startAuthentication(DropboxActivity.this);
                }

                break;

            case R.id.upload_file:
                exportTournament(v);

                break;

            default:
                break;
        }
    }

    /**
     * Exports a html-file to Dropbox folder
     *
     * @param v Clicked View
     */
    public void exportTournament(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);



        builder.setTitle("Select tournament to upload");
        builder.setView(tournamentsListView);
        builder.setCancelable(true);
        refreshUI();

        builder.setPositiveButton("Upload HTML",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        ArrayList<Integer> selectedId = checkSelected(tournamentsListView);
                        Tournament tempTournament = (Tournament) tournamentsListView.getAdapter().getItem(selectedId.get(0));


                        tournament = loader.getTournamentAndRounds(tempTournament.getId());
                        UploadFileToDropbox upload = new UploadFileToDropbox(DropboxActivity.this, dropbox, FILE_DIR, tournament);
                        upload.execute();
                        refreshUI();
                        ((ViewGroup) tournamentsListView.getParent()).removeView(tournamentsListView);
                        selectedId.clear();
                        dialog.dismiss();
                        dialog.cancel();


                    }
                }
        );



        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((ViewGroup) tournamentsListView.getParent()).removeView(tournamentsListView);

                        dialog.dismiss();
                        dialog.cancel();

                    }
                }
        );


        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Checks for checked items inside a ListView
     *
     * @param listView ListView to check
     * @return ArrayList of clicked ids.
     */
    public ArrayList<Integer> checkSelected(ListView listView) {

        // Get all of the items that have been clicked - either on or off
        final SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
        Log.d("DropboxActivity", checkedItems.toString());
        ArrayList<Integer> checkedIDs= new ArrayList<Integer>();
        for (int i = 0; i < checkedItems.size(); i++){
            // And this tells us the item status at the above position
            final boolean isChecked = checkedItems.valueAt(i);
            if (isChecked){
                // This tells us the item position we are looking at
                final int position = checkedItems.keyAt(i);
                // Put the value of the id in our list
                checkedIDs.add(0, position);

            }
        }

        return checkedIDs;
    }
    /**
     * Refreshes the ListView
     */
    public void refreshUI() {

        adapterTournaments2.clear();
        adapterTournaments2.addAll(loader.loadTournaments());
        adapterTournaments2.notifyDataSetChanged();

    }

}
