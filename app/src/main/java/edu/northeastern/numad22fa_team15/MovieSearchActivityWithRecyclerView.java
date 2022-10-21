package edu.northeastern.numad22fa_team15;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad22fa_team15.model.MovieTv;
import edu.northeastern.numad22fa_team15.model.Result;
import edu.northeastern.numad22fa_team15.network.MovieSearchService;
import edu.northeastern.numad22fa_team15.network.RetrofitMovieClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieSearchActivityWithRecyclerView extends AppCompatActivity {

    private static final String TAG = "MovieSearchActivity___";
    private static final String API_KEY = "ecafb541";

    private MovieSearchThread movieSearchThread;
    private Thread movieThread;

    private ProgressDialog progressDialog;

    EditText titleInput;
    TextView yearInput;
    CheckBox yearCheckBox;
    RadioGroup typeRadioGroup;
    RadioButton typeRadioButton;

    TextView type;
    TextView title;
    TextView year;
    TextView id;
    ImageView poster;

    private List<MovieTv> matchResults;
    RecyclerView movieTvRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search_w_recycler_view);

        if (savedInstanceState != null) {
            // When screen is rotated, the list of match results is restored
//            matchResults = (ArrayList<MovieTv>) savedInstanceState.getSerializable(STATE_LINKS);
        } else {
            // List is empty at first
            matchResults = new ArrayList<>();
        }

        movieTvRecyclerView = findViewById(R.id.movie_tv_recycler_view);

        // Define the way in which the RecyclerView is oriented
        movieTvRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Associates the adapter with the RecyclerView
        movieTvRecyclerView.setAdapter(new MovieTvAdapter(matchResults, this));

        // Input: title, year, type
        titleInput = (EditText) findViewById(R.id.movie_tv_title_edit_text);
        yearInput = (TextView) findViewById(R.id.customized_year_text_view);
        yearCheckBox = (CheckBox) findViewById(R.id.customized_year_check_box);
        yearCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    yearInput.setText(R.string.customized_year);
                } else {
                    yearPickerDialog();
                }
            }
        });
        typeRadioGroup = (RadioGroup) findViewById(R.id.type_radio_group);
        typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                typeRadioButton = findViewById(checkedID);
            }
        });
        typeRadioButton = findViewById(R.id.no_preference_radio_button);

        progressDialog = new ProgressDialog(MovieSearchActivityWithRecyclerView.this);
        progressDialog.setMessage("Loading...");

        movieSearchThread = new MovieSearchThread();
    }

    /**
     * Generate a year picker dialog that allows users to specify which year they want to search the
     * movie/tv in.
     */
    private void yearPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MovieSearchActivityWithRecyclerView.this);
        builder.setTitle("Pick a specific year");

        // Set view to be a number picker (1888 - 2022)
        NumberPicker yearPicker = new NumberPicker(MovieSearchActivityWithRecyclerView.this);
        yearPicker.setMinValue(1888);
        yearPicker.setMaxValue(2022);
        yearPicker.setValue(2008);
        builder.setView(yearPicker);

        // Choosing "Confirm" will close the MovieSearchActivity
        builder.setPositiveButton("Confirm", (DialogInterface.OnClickListener) (dialog, which) -> {
            String yearString = String.valueOf(yearPicker.getValue());
            yearInput.setText(yearString);
            dialog.cancel();
        });
        // Choosing "Cancel" will let the user remain in the MovieSearchActivity
        builder.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
            yearCheckBox.setChecked(false);
            dialog.cancel();
        });
        // Nothing happens if the user clicks anywhere outside the dialog
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        // Display the Alert Dialog to the user
        alertDialog.show();
    }

    public void searchMovieUsingApi(View view) {
        progressDialog.show();

        if (movieThread != null && movieThread.isAlive()) {
            String runningSearchMessage = "A search is currently running";
            Snackbar.make(view, runningSearchMessage, Snackbar.LENGTH_SHORT).show();
            return;
        }
        movieThread = new Thread(movieSearchThread);
        movieThread.start();
    }

    class MovieSearchThread implements Runnable {

        /**
         * Use the Picasso package to load the poster image from the given url and assign it to the
         * poster image view.
         * @param url poster URL
         */
        private void loadImageInImageView(String url) {
            Picasso.get().load(url).resize(400,600).centerCrop().into(poster);
        }

        @Override
        public void run() {
            String titleInputString = titleInput.getText().toString();
            String yearInputString = yearInput.getText().toString();
            if (yearInputString.equals(getString(R.string.customized_year))) {
                yearInputString = "";
            }
            String typeInputString = typeRadioButton.getText().toString();
            if (typeInputString.equals(getString(R.string.no_preference))) {
                typeInputString = "";
            }

            StringBuilder searchMessageStringBuilder = new StringBuilder();
            searchMessageStringBuilder.append("Searching for title: ");
            searchMessageStringBuilder.append(titleInputString);
            searchMessageStringBuilder.append(". Year: ");
            searchMessageStringBuilder.append(yearInputString);
            searchMessageStringBuilder.append(". Type: ");
            searchMessageStringBuilder.append(typeInputString);
            Log.v(TAG, searchMessageStringBuilder.toString());

            MovieSearchService service = RetrofitMovieClientInstance.getRetrofitInstance().create(MovieSearchService.class);

//            callServiceGetMovieTvByTitle(service, titleInputString, API_KEY);
            callServiceGetMovieTvByCriteria(service, titleInputString, yearInputString, typeInputString, API_KEY);
        }

        private void callServiceGetMovieTvByCriteria(MovieSearchService service, String titleInputString,
                                                     String yearInputString, String typeInputString,
                                                     String apiKey) {
            Call<Result> call = service.getMovieTvByCriteria(titleInputString, yearInputString, typeInputString, apiKey);
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    Log.v(TAG, "onResponse() " + titleInputString);
                    progressDialog.dismiss();

                    Result movieTvResponses = response.body();
                    Log.v(TAG, "Total results: " + movieTvResponses.getTotalResults() +
                            ". Response: " + movieTvResponses.getResponse());
                    for (MovieTv movieTvResponse : movieTvResponses.getMovieTvList()) {
                        Log.v(TAG, "Response: " + movieTvResponse.getMovieTvTitle());
                        addResultToList(movieTvResponse);
                    }
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Log.v(TAG, "onFailure() " + titleInputString);
                    progressDialog.dismiss();

                    String searchFailureMessage= "Something went wrong. Please try again later!";
                    Snackbar.make(findViewById(android.R.id.content), searchFailureMessage, Snackbar.LENGTH_SHORT).show();
                }
            });
        }

        private void addResultToList(MovieTv movieTvResponse) {
            matchResults.add(movieTvResponse);
            movieTvRecyclerView.getAdapter().notifyDataSetChanged();
        }

        private void callServiceGetMovieTvByTitle(MovieSearchService service, String titleInputString,
                                                  String apiKey) {
            Call<MovieTv> call = service.getMovieTvByTitle(titleInputString, API_KEY);
            call.enqueue(new Callback<MovieTv>() {
                @Override
                public void onResponse(Call<MovieTv> call, Response<MovieTv> response) {
                    Log.v(TAG, "onResponse() " + titleInputString);
                    progressDialog.dismiss();

                    MovieTv movieResponse = response.body();

//                    boolean searchResult = Boolean.parseBoolean(movieResponse.getResponse());
                    boolean searchResult = true;
                    Log.v(TAG, "Search result: " + searchResult);
                    if (searchResult) {
                        loadImageInImageView(movieResponse.getPosterURL());
                        type.setText(movieResponse.getType());
                        title.setText(movieResponse.getMovieTvTitle());
                        year.setText(movieResponse.getMovieTvYear());
                        id.setText(movieResponse.getMovieTvImdbID());
                    } else {
                        type.setText("not found");
                        title.setText(R.string.movie_tv_title);
                        year.setText(R.string.movie_tv_year);
                        id.setText(R.string.imdb_id);
                    }
                }

                @Override
                public void onFailure(Call<MovieTv> call, Throwable t) {
                    Log.v(TAG, "onFailure() " + titleInputString);
                    progressDialog.dismiss();

                    String searchFailureMessage= "Something went wrong. Please try again later!";
                    Snackbar.make(findViewById(android.R.id.content), searchFailureMessage, Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.v(TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Log.v(TAG, "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.v(TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.v(TAG, "onStop()");
        super.onStop();
    }

}
