package fi.tamk.tiko.th_results;


import java.sql.SQLException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import fi.tamk.tiko.th_results.database.DataSource;
import fi.tamk.tiko.th_results.database.Loader;
import fi.tamk.tiko.th_results.tournament.Player;
import fi.tamk.tiko.th_results.tournament.Tournament;

/**
 * TournamentFragment inflates layout defined in fragment_layout.xml
 *
 * This fragment holds the first fragment layout in the view pager. Tournaments
 * can be loaded and created from this fragment.
 *
 * @author Juho Rautio
 * @version 1.01
 * @since 1.0
 */
public class TournamentFragment extends Fragment implements OnClickListener {
	private Tournament tournament;
	Button addPlayerButton;
	Button removePlayerButton;
    Button createTournamentButton;
    Button loadTournamentButton;
    Button clearPlayersButton;
	ListView listView;
	ArrayAdapter<String> adapter;
	ArrayList<String> playersList;
	TextView participantsTextView;
    Communicator comm;
    //ArrayAdapter<String> adapterTournaments;
    TournamentAdapter adapterTournaments2;
    ArrayList<Tournament> tournamentsList;
    ListView tournamentsListView;
    Loader loader;
    View rootView;
    public TournamentFragment(){
	      //Empty constructor
	   }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        rootView = inflater.inflate(R.layout.fragment_tournament, container, false);
        addPlayerButton = (Button) rootView.findViewById(R.id.addPlayerButton);
        removePlayerButton = (Button) rootView.findViewById(R.id.removePlayerButton);
        createTournamentButton = (Button) rootView.findViewById(R.id.createTournamentButton);
        loadTournamentButton = (Button) rootView.findViewById(R.id.loadTournamentButton);
        clearPlayersButton = (Button) rootView.findViewById(R.id.clearPlayersButton);
        listView = (ListView) rootView.findViewById(R.id.list);
        participantsTextView = (TextView) rootView.findViewById(R.id.participantsTextView);
        addPlayerButton.setOnClickListener(this);
        removePlayerButton.setOnClickListener(this);
        createTournamentButton.setOnClickListener(this);
        loadTournamentButton.setOnClickListener(this);
        clearPlayersButton.setOnClickListener(this);
        tournament = new Tournament();
        tournamentsListView = new ListView(getActivity().getApplicationContext());
        tournamentsList = new ArrayList<Tournament>();

         
        participantsTextView.setText("Participants: " + tournament.getPlayerCount());
        playersList = tournament.getPlayersToStringArray();

        
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_multiple_choice, 
                playersList);
        
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        loader = new Loader(getActivity().getApplicationContext());


        tournamentsList = loader.loadTournaments();
        ArrayList<String> tournamentsStringArray = loader.tournamentsToStringArray(tournamentsList);

        adapterTournaments2 = new TournamentAdapter(getActivity().getApplicationContext(), R.layout.tournamentlist_item, tournamentsList);
        tournamentsListView.setAdapter(adapterTournaments2);
        tournamentsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        tournamentsListView.setBackgroundColor(getActivity().getApplicationContext()
                .getResources().getColor(R.color.darkgray));


        return rootView;
    }
	
	@Override
	public void onResume() {
		refreshUI();
		super.onResume();
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            playersList = savedInstanceState.getStringArrayList("fi/tamk/tiko/th_results/tournament");

            tournament.setPlayers(playersList);
            getActivity().setTitle(tournament.getName());
            refreshUI();
        }
	}


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);



        comm = (Communicator) activity;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("fi/tamk/tiko/th_results/tournament", this.tournament.getPlayersToStringArray());
    }

    /**
     * Adds a new player to a tournament and the UI.
     *
     * @param v Clicked view
     */
    protected void addPlayer(View v) {
    	AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
    	final EditText input = new EditText(getActivity());
    	builder1.setView(input);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Add player",
                new DialogInterface.OnClickListener() {
	            
        	public void onClick(DialogInterface dialog, int id) {
                if (isAlphaNumeric(input.getText().toString())) {
                    if (!tournament.checkForDuplicatePlayer(input.getText().toString())) {


                            addPlayerToUI(input.getText().toString());


                            dialog.cancel();

                        } else {
                            Toast.makeText(getActivity().getBaseContext(), "Player " + input.getText().toString() + " already added", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity().getBaseContext(), "Only letters a-Z, 0-9 and space allowed, 2-25 characters.", Toast.LENGTH_SHORT).show();

                    }

	            }
        });
        builder1.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    /**
     * Removes selected players from the tournament.
     *
     * @param v Clicked view
     */
    public void removePlayers(View v) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    	ArrayList<Integer> checkedIDs = checkSelected(listView);
    	builder.setTitle("Delete " + checkedIDs.size() + " items?");
        builder.setCancelable(true);
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
	            
        	public void onClick(DialogInterface dialog, int id) {
        			ArrayList<Integer> checkedIDs = checkSelected(listView);
        			removeIDs(checkedIDs);
        	        adapter.notifyDataSetChanged();
        	        listView.clearChoices();
        	        
	                dialog.cancel();
	            }
        });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Adds a new tournament to the UI and database, generates match tables and standings.
     *
     * @param v Clicked view
     */
    public void createTournament(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Create a new tournament with " + tournament.getSize() + " players");

        final EditText input = new EditText(getActivity());
        input.setText("New Tournament");
        builder.setView(input);
        builder.setCancelable(true);
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        tournament.setName(input.getText().toString());
                        if (isAlphaNumeric(tournament.getName())) {
                            DataSource ds = new DataSource(getActivity());
                            try {
                                ds.open();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            tournament.setDate();
                            if (tournament.getPlayerCount() % 2 == 0) {
                                Player ghost = new Player("X");
                                ghost.setGhostPlayer(true);
                                tournament.addPlayer(ghost);
                            }
                            tournament.setId(ds.createTournament(tournament));
                            Log.d("TournamentFragment", "TOURNAMENT ID " + tournament.getId());

                            tournament.generateRounds(getActivity().getApplicationContext(), tournament.getId());
                            getActivity().setTitle(tournament.getName());
                            tournament.setMaxRounds();
                            comm.respond(tournament, 1, true);
                            comm.respond(tournament, 2, false);


                            dialog.cancel();
                        } else {
                            Toast.makeText(getActivity().getBaseContext(), "Only letters a-Z, 0-9 and space allowed, 2-25 characters.", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
        );
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }
        );
        if (tournament.getPlayerCount() > 0) {
            AlertDialog alert = builder.create();
            alert.show();

        } else {
            Toast.makeText(getActivity().getBaseContext(), "Empty tournament", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Opens a dialog for loading tournaments.
     *
     * @param v Clicked view
     */

    public void loadTournament(View v) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());



        builder.setTitle("Load a tournament");
        builder.setView(tournamentsListView);
        builder.setCancelable(true);
        refreshUI();

        builder.setPositiveButton("Load",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        ArrayList<Integer> selectedId = checkSelected(tournamentsListView);
                        Tournament tempTournament = (Tournament) tournamentsListView.getAdapter().getItem(selectedId.get(0));



                        TournamentLoader loader = new TournamentLoader(getActivity().getApplicationContext(), rootView);
                        loader.execute(tempTournament.getId());

                        ((ViewGroup) tournamentsListView.getParent()).removeView(tournamentsListView);;
                        selectedId.clear();
                        dialog.dismiss();
                        dialog.cancel();


                    }
                }
        );

        builder.setNeutralButton("Delete tournament",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Delete tournament")
                                .setMessage("Are you sure you want to delete tournament?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        ArrayList<Integer> selectedId = checkSelected(tournamentsListView);

                                        if (selectedId.size() > 0) {
                                            Tournament tempTournament = (Tournament) tournamentsListView.getAdapter().getItem(selectedId.get(0));
                                            DataSource ds = new DataSource(getActivity().getApplicationContext());
                                            try {
                                                ds.open();
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }

                                            ds.deleteTournamentandMatches(tempTournament.getId());
                                            Toast.makeText(getActivity().getBaseContext(), "Tournament deleted", Toast.LENGTH_SHORT).show();
                                            refreshUI();
                                            ((ViewGroup) tournamentsListView.getParent()).removeView(tournamentsListView);
                                        } else {
                                            Toast.makeText(getActivity().getBaseContext(), "Please select a tournament to delete.", Toast.LENGTH_SHORT).show();
                                            ((ViewGroup) tournamentsListView.getParent()).removeView(tournamentsListView);
                                        }

                                    }})
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        ((ViewGroup) tournamentsListView.getParent()).removeView(tournamentsListView);
                                        dialog.cancel();
                                        dialog.dismiss();
                                    }
                                }).show();


                    }
                });

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
        try {
            alert.show();
        } catch(IllegalStateException e) {

            alert.dismiss();
            alert.cancel();
        }
    }

    /**
     * AsyncTask for loading tournaments.
     */
    public class TournamentLoader extends AsyncTask<Long, Void, Tournament> {
        Context mContext;
        View rootView;

        public TournamentLoader(Context context, View rootView){
            this.mContext=context;
            this.rootView=rootView;
        }

        @Override
        protected void onPreExecute(){
            ProgressBar bar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            bar.setVisibility(View.VISIBLE);

        }
        @Override
        protected Tournament doInBackground(Long... tournamentID) {
            tournament = loader.getTournamentAndRounds(tournamentID[0]);



            return tournament;
        }

        @Override
        protected void onPostExecute(Tournament tournament) {
            TournamentFragment.this.tournament = tournament;
            comm.respond(tournament, 1, false);
            comm.respond(tournament, 2, false);
            ProgressBar bar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            refreshUI();
            getActivity().setTitle(tournament.getName());
            bar.setVisibility(View.GONE);
            //ImageView icon = (ImageView) findViewById(R.id.imageView1);
            //icon.setImageBitmap(bitmap);
            //bar.setVisibility(View.GONE);
        }
    }

    /**
     * Adds a player to the UI
     *
     * @param playerName Name of the player to be added
     */
    public void addPlayerToUI(String playerName) {
    	Player player = new Player(playerName);
    	tournament.addPlayer(player);
    	refreshUI();
    	Log.d("TournamentFragment", "adding " + player.getName());
    	
    }

    /**
     * Refreshes lists and UI
     */
    public void refreshUI() {
    	participantsTextView.setText("Participants: " + tournament.getPlayerCount());
        adapter.clear();


        tournament.removeGhosts();

        adapter.addAll(tournament.getPlayersToStringArray());
        adapter.notifyDataSetChanged();
        adapterTournaments2.clear();
        adapterTournaments2.addAll(loader.loadTournaments());
        adapterTournaments2.notifyDataSetChanged();

    }
    /**
     * Checks selected ListView items
     *
     * @param listView ListView to be checked.
     * @return Integer ArrayList of selected IDs.
     */
    public ArrayList<Integer> checkSelected(ListView listView) {
  	                           
    	// Get all of the items that have been clicked - either on or off
    	final SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
        Log.d("TournamentFragment", checkedItems.toString());
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
     * Removes selected players from the tournament.
     *
     * @param checkedIDs ArrayList of checked ListView IDs.
     */
    public void removeIDs(ArrayList<Integer> checkedIDs) {

    	for (int i = 0; i < checkedIDs.size(); i++) {
    		tournament.removePlayer(checkedIDs.get(i));
    	}
    	refreshUI();
    }
    /**
     * Clears all players from the ListView.
     *
     * @param v Clicked view
     */
    public void clearPlayers(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Clear playerlist?");

        builder.setCancelable(true);
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        tournament.clearPlayers();
                        refreshUI();
                        dialog.cancel();
                    }
                }
        );
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }
        );

            AlertDialog alert = builder.create();
            alert.show();

    }

    /**
     * Checks if a string is alphanumeric containing spaces.
     *
     * @param str String to check
     * @return True if is alphanumeric.
     */
    public boolean isAlphaNumeric(String str){
        String pattern= "^[a-zA-Z0-9\\s]{2,25}$";
        if(str.matches(pattern)){
            return true;
        }
        return false;
    }
    /**
     * Handles button clicks.
     *
     * @param v Clicked view
     */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.addPlayerButton: addPlayer(v);
				break;
		case R.id.removePlayerButton: removePlayers(v);
			break;
        case R.id.createTournamentButton:  createTournament(v);
            break;
        case R.id.loadTournamentButton:  loadTournament(v);
            break;
        case R.id.clearPlayersButton: if (tournament.getPlayers().size() > 0) { clearPlayers(v);}
            break;
        }
	}

}
