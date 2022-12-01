package edu.northeastern.numad22fa_team15.activities.peakActivities;

import androidx.appcompat.app.AppCompatActivity;

import static edu.northeastern.numad22fa_team15.utils.CommonUtils.displayMessageInSnackbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.google.android.material.snackbar.Snackbar;

import edu.northeastern.numad22fa_team15.R;
import edu.northeastern.numad22fa_team15.utils.CommonUtils;

public class PeakCreateBudget extends AppCompatActivity {

    SeekBar sb_dining, sb_groceries, sb_shopping, sb_living;
    SeekBar sb_entertainment, sb_education, sb_beauty, sb_transportation;
    SeekBar sb_health, sb_travel, sb_pet, sb_other;

    EditText et_dining, et_groceries, et_shopping, et_living;
    EditText et_entertainment, et_education, et_beauty, et_transportation;
    EditText et_health, et_travel, et_pet, et_other;

    Button confirm, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);

        // Display message if intent carries a message.
        String potentialMessage = getIntent().getStringExtra("message");
        if (potentialMessage != null) {
            displayMessageInSnackbar(findViewById(android.R.id.content).getRootView(),
                    potentialMessage, Snackbar.LENGTH_SHORT);
        }

        /**
         * TODO: get current budget amount for each category from the database and set
         *      seekbar progress to that amount. If it is the user's first time on
         *      this page (if the budget table is empty), set all progress to 0.
         */

        // Setting functions for buttons - Cancel Button
        cancel = (Button) findViewById(R.id.btn_cancel_new_budget);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelEditBudget(view);
            }
        });

        // TODO: Setting functions for buttons - Confirm Button
        confirm = (Button) findViewById(R.id.btn_confirm_new_budget);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: alter database with new budget amounts
            }
        });

        // Dining SeekBar
        sb_dining = (SeekBar) findViewById(R.id.seekbar_dining);
        et_dining = (EditText) findViewById(R.id.et_seekbar_dining);

        sb_dining.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar sb_dining, int progress,
                                          boolean b) {
                et_dining.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb_dining) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar sb_dining) {

            }
        });

        et_dining.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (et_dining.getText().toString().isEmpty()) {
                    et_dining.setText("0");
                    sb_dining.setProgress(0);
                } else if (Integer.parseInt(et_dining.getText().toString()) > 5000) {
                    et_dining.setText("5000");
                    sb_dining.setProgress(5000);
                } else {
                    setProgressValue(et_dining, sb_dining);
                }
                CommonUtils.closeKeyboard(view.getContext(), view);
            }
        });

        // Groceries SeekBar
        sb_groceries = (SeekBar) findViewById(R.id.seekbar_groceries);
        et_groceries = (EditText) findViewById(R.id.et_seekbar_groceries);

        sb_groceries.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar sb_groceries, int progress,
                                          boolean b) {
                et_groceries.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb_groceries) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar sb_groceries) {
                // TODO Auto-generated method stub
            }
        });

        et_groceries.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (et_groceries.getText().toString().isEmpty()) {
                    et_groceries.setText("0");
                    sb_groceries.setProgress(0);
                } else if (Integer.parseInt(et_groceries.getText().toString()) > 5000) {
                    et_groceries.setText("5000");
                    sb_groceries.setProgress(5000);
                } else {
                    setProgressValue(et_groceries, sb_groceries);
                }
                CommonUtils.closeKeyboard(view.getContext(), view);
            }
        });

        // Shopping SeekBar
        sb_shopping = (SeekBar) findViewById(R.id.seekbar_shopping);
        et_shopping = (EditText) findViewById(R.id.et_seekbar_shopping);

        sb_shopping.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar sb_shopping, int progress,
                                          boolean b) {
                et_shopping.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb_shopping) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar sb_shopping) {
                // TODO Auto-generated method stub
            }
        });

        et_shopping.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (et_shopping.getText().toString().isEmpty()) {
                    et_shopping.setText("0");
                    sb_shopping.setProgress(0);
                } else if (Integer.parseInt(et_shopping.getText().toString()) > 5000) {
                    et_shopping.setText("5000");
                    sb_shopping.setProgress(5000);
                } else {
                    setProgressValue(et_shopping, sb_shopping);
                }
                CommonUtils.closeKeyboard(view.getContext(), view);
            }
        });

        // Living SeekBar
        sb_living = (SeekBar) findViewById(R.id.seekbar_living);
        et_living = (EditText) findViewById(R.id.et_seekbar_living);

        sb_living.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar sb_living, int progress,
                                          boolean b) {
                et_living.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb_living) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar sb_living) {
                // TODO Auto-generated method stub
            }
        });

        et_living.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (et_living.getText().toString().isEmpty()) {
                    et_living.setText("0");
                    sb_living.setProgress(0);
                } else if (Integer.parseInt(et_living.getText().toString()) > 5000) {
                    et_living.setText("5000");
                    sb_living.setProgress(5000);
                } else {
                    setProgressValue(et_living, sb_living);
                }
                CommonUtils.closeKeyboard(view.getContext(), view);
            }
        });

        // Entertainment SeekBar
        sb_entertainment = (SeekBar) findViewById(R.id.seekbar_entertainment);
        et_entertainment = (EditText) findViewById(R.id.et_seekbar_entertainment);

        sb_entertainment.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar sb_entertainment, int progress,
                                          boolean b) {
                et_entertainment.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb_entertainment) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar sb_entertainment) {
                // TODO Auto-generated method stub
            }
        });

        et_entertainment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (et_entertainment.getText().toString().isEmpty()) {
                    et_entertainment.setText("0");
                    sb_entertainment.setProgress(0);
                } else if (Integer.parseInt(et_entertainment.getText().toString()) > 5000) {
                    et_entertainment.setText("5000");
                    sb_entertainment.setProgress(5000);
                } else {
                    setProgressValue(et_entertainment, sb_entertainment);
                }
                CommonUtils.closeKeyboard(view.getContext(), view);
            }
        });

        // Education SeekBar
        sb_education = (SeekBar) findViewById(R.id.seekbar_education);
        et_education = (EditText) findViewById(R.id.et_seekbar_education);

        sb_education.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar sb_education, int progress,
                                          boolean b) {
                et_education.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb_education) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar sb_education) {
                // TODO Auto-generated method stub
            }
        });

        et_education.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (et_education.getText().toString().isEmpty()) {
                    et_education.setText("0");
                    sb_education.setProgress(0);
                } else if (Integer.parseInt(et_education.getText().toString()) > 5000) {
                    et_education.setText("5000");
                    sb_education.setProgress(5000);
                } else {
                    setProgressValue(et_education, sb_education);
                }
                CommonUtils.closeKeyboard(view.getContext(), view);
            }
        });

        // Beauty SeekBar
        sb_beauty = (SeekBar) findViewById(R.id.seekbar_beauty);
        et_beauty = (EditText) findViewById(R.id.et_seekbar_beauty);

        sb_beauty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean b) {
                et_beauty.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        et_beauty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (et_beauty.getText().toString().isEmpty()) {
                    et_beauty.setText("0");
                    sb_beauty.setProgress(0);
                } else if (Integer.parseInt(et_beauty.getText().toString()) > 5000) {
                    et_beauty.setText("5000");
                    sb_beauty.setProgress(5000);
                } else {
                    setProgressValue(et_beauty, sb_beauty);
                }
                CommonUtils.closeKeyboard(view.getContext(), view);
            }
        });

        // Transportation SeekBar
        sb_transportation = (SeekBar) findViewById(R.id.seekbar_transportation);
        et_transportation = (EditText) findViewById(R.id.et_seekbar_transportation);

        sb_transportation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean b) {
                et_transportation.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        et_transportation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (et_transportation.getText().toString().isEmpty()) {
                    et_transportation.setText("0");
                    sb_transportation.setProgress(0);
                } else if (Integer.parseInt(et_transportation.getText().toString()) > 5000) {
                    et_transportation.setText("5000");
                    sb_transportation.setProgress(5000);
                } else {
                    setProgressValue(et_transportation, sb_transportation);
                }
                CommonUtils.closeKeyboard(view.getContext(), view);
            }
        });

        // Health SeekBar
        sb_health = (SeekBar) findViewById(R.id.seekbar_health);
        et_health = (EditText) findViewById(R.id.et_seekbar_health);

        sb_health.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean b) {
                et_health.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        et_health.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (et_health.getText().toString().isEmpty()) {
                    et_health.setText("0");
                    sb_health.setProgress(0);
                } else if (Integer.parseInt(et_health.getText().toString()) > 5000) {
                    et_health.setText("5000");
                    sb_health.setProgress(5000);
                } else {
                    setProgressValue(et_health, sb_health);
                }
                CommonUtils.closeKeyboard(view.getContext(), view);
            }
        });

        // Travel SeekBar
        sb_travel = (SeekBar) findViewById(R.id.seekbar_travel);
        et_travel = (EditText) findViewById(R.id.et_seekbar_travel);

        sb_travel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean b) {
                et_travel.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        et_travel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (et_travel.getText().toString().isEmpty()) {
                    et_travel.setText("0");
                    sb_travel.setProgress(0);
                } else if (Integer.parseInt(et_travel.getText().toString()) > 5000) {
                    et_travel.setText("5000");
                    sb_travel.setProgress(5000);
                } else {
                    setProgressValue(et_travel, sb_travel);
                }
                CommonUtils.closeKeyboard(view.getContext(), view);
            }
        });

        // Pet SeekBar
        sb_pet = (SeekBar) findViewById(R.id.seekbar_pet);
        et_pet = (EditText) findViewById(R.id.et_seekbar_pet);

        sb_pet.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean b) {
                et_pet.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        et_pet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (et_pet.getText().toString().isEmpty()) {
                    et_pet.setText("0");
                    sb_pet.setProgress(0);
                } else if (Integer.parseInt(et_pet.getText().toString()) > 5000) {
                    et_pet.setText("5000");
                    sb_pet.setProgress(5000);
                } else {
                    setProgressValue(et_pet, sb_pet);
                }
                CommonUtils.closeKeyboard(view.getContext(), view);
            }
        });

        // Other SeekBar
        sb_other = (SeekBar) findViewById(R.id.seekbar_other);
        et_other = (EditText) findViewById(R.id.et_seekbar_other);

        sb_other.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean b) {
                et_other.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        et_other.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (et_other.getText().toString().isEmpty()) {
                    et_other.setText("0");
                    sb_other.setProgress(0);
                } else if (Integer.parseInt(et_other.getText().toString()) > 5000) {
                    et_other.setText("5000");
                    sb_other.setProgress(5000);
                } else {
                    setProgressValue(et_other, sb_other);
                }
                CommonUtils.closeKeyboard(view.getContext(), view);
            }
        });
    }

    // Helper function to set progress of seekbar
    private void setProgressValue(EditText et, SeekBar seekBar) {
        seekBar.setProgress(Integer.parseInt(et.getText().toString()));
    }

    // Function to ensure the user creates a budget when the budget table is empty
    private void cancelEditBudget(View view) {

        // TODO: if statement to check if the summary table is empty. If it is, then:
        String message = "You must set a budget for at least one category.";
        displayMessageInSnackbar(view, message, Snackbar.LENGTH_SHORT);

        // TODO: else, bring the user back to the home screen.

    }

}