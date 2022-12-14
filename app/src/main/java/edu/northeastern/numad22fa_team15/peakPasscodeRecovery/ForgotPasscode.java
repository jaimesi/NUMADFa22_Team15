package edu.northeastern.numad22fa_team15.peakPasscodeRecovery;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import edu.northeastern.numad22fa_team15.R;

public class ForgotPasscode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Back button on ToolBar to go back one page
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.forgot_passcode);
    }
}