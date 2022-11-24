package edu.northeastern.numad22fa_team15.activities.peakActivities.userRegister;

import static edu.northeastern.numad22fa_team15.utils.CommonUtils.closeKeyboard;
import static edu.northeastern.numad22fa_team15.utils.CommonUtils.displayMessageInSnackbar;
import static edu.northeastern.numad22fa_team15.utils.CommonUtils.fourDigitPasscodeChecker;
import static edu.northeastern.numad22fa_team15.utils.CommonUtils.nullOrEmptyInputChecker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import edu.northeastern.numad22fa_team15.R;
import edu.northeastern.numad22fa_team15.utils.DBHelper;
import edu.northeastern.numad22fa_team15.utils.IDBHelper;

public class PeakSignUp extends AppCompatActivity {


    private static final String TAG = "PeakSignUp___";
    private TextInputEditText usernameTextInputEditText;
    private TextInputEditText firstNameTextInputEditText;
    private TextInputEditText lastNameTextInputEditText;
    private TextInputEditText passcodeTextInputEditText;

    private IDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "OnCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peak_sign_up);

        usernameTextInputEditText = (TextInputEditText) findViewById(R.id.username_text_input_edit_text);
        firstNameTextInputEditText = (TextInputEditText) findViewById(R.id.first_name_text_input_edit_text);
        lastNameTextInputEditText = (TextInputEditText) findViewById(R.id.last_name_text_input_edit_text);
        passcodeTextInputEditText = (TextInputEditText) findViewById(R.id.passcode_text_input_edit_text);

        dbHelper = new DBHelper(this);
    }


    public void userSignUp(View view) {
        Log.v(TAG, "Trying to sign up user.");
        closeKeyboard(this.getApplicationContext(), view);
        String username = usernameTextInputEditText.getText().toString();
        String firstName = firstNameTextInputEditText.getText().toString();
        String lastName = lastNameTextInputEditText.getText().toString();
        String passcode = passcodeTextInputEditText.getText().toString();
        if (nullOrEmptyInputChecker(username, firstName, lastName, passcode)) {
            String message = "Username, first name, last name, and passcode inputs are all required.";
            displayMessageInSnackbar(view, message, Snackbar.LENGTH_SHORT);
            return; // Terminate the method
        }
        if (!fourDigitPasscodeChecker(passcode)) {
            Log.v(TAG, "Wrong passcode format");
            String message = "Passcode needs to have 4 digits.";
            displayMessageInSnackbar(view, message, Snackbar.LENGTH_SHORT);
            return;
        }
        boolean addResult = dbHelper.addUserTableUser(username, firstName, lastName, passcode);
        // String resultMessage = "Failed to add user"; // Default message
        if (addResult) {
            // ---> OPTION 1: JUMP TO LOGIN PAGE
            // Intent intent = new Intent(PeakSignUp.this, PeakLogin.class);
            // ---> OPTION 2: JUMP TO CONFIRMATION PAGE --> NO NEED TO ENTER USERNAME AND PASSCODE AGAIN
            Intent intent = new Intent(PeakSignUp.this, PeakSignUpConfirmation.class);
            startActivity(intent);
            // resultMessage = "User was added successfully.";
        }
        // displayMessageInSnackbar(view, resultMessage, Snackbar.LENGTH_SHORT);
    }
}