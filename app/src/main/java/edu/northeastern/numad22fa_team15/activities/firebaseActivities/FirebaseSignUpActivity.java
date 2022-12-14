package edu.northeastern.numad22fa_team15.activities.firebaseActivities;

import static edu.northeastern.numad22fa_team15.utils.CommonUtils.closeKeyboard;
import static edu.northeastern.numad22fa_team15.utils.CommonUtils.displayMessageInSnackbar;
import static edu.northeastern.numad22fa_team15.utils.FirebaseUtils.checkUserExistenceInFirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.northeastern.numad22fa_team15.R;
import edu.northeastern.numad22fa_team15.models.firebaseModels.User;

public class FirebaseSignUpActivity extends AppCompatActivity {

    private static final String TAG = "FirebaseSignUpActivity___";

    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_signup);

        usernameEditText = findViewById(R.id.reg_username_input);
        passwordEditText = findViewById(R.id.reg_password_input);
        // Get the instance of the Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();
        // Get the "Users" reference for our database.
        usersDatabaseReference = firebaseDatabase.getReference().child("users");
    }

    public void openLogin(View view) {
        Intent intent = new Intent(FirebaseSignUpActivity.this, FirebaseLoginActivity.class);
        usernameEditText.setText("");
        passwordEditText.setText("");
        startActivity(intent);
    }

    /**
     * This method gets called when the user clicks the SIGN UP button. It will try to add
     * a new user to the firebase realtime database.
     * @param view view
     */
    public void firebaseUsernameSignUp(View view) {
        closeKeyboard(this.getApplicationContext(), view);
        String usernameInput = usernameEditText.getText().toString();
        if (!usernameInputChecker(usernameInput, view)) {
            return;
        }
        addUserToFirebase(usernameInput);
        usernameEditText.setText("");
        passwordEditText.setText("");
    }

    /**
     * This helper method returns true if the given username input is valid. Otherwise, it
     * returns false.
     * @param usernameInput a username input
     * @param view view
     * @return true if username input is valid. Otherwise, return false
     */
    private boolean usernameInputChecker(String usernameInput, View view) {
        if (usernameInput == null || usernameInput.isEmpty()) {
            String usernameErrorMessage = "Username cannot be null or empty.";
            displayMessageInSnackbar(view, usernameErrorMessage, Snackbar.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    /**
     * This helper method will try to add a User object with the given username to the firebase
     * realtime database if the given username does not exist in the database yet.
     * @param username a username
     */
    private void addUserToFirebase(@NonNull String username) {
        Log.v(TAG, String.format("Checking if user %s exists in the database", username));
        Task<DataSnapshot> task1 = usersDatabaseReference.get();

        task1.addOnCompleteListener(task -> {
            if (!task1.isSuccessful()) {
                // A more specific message should be "Failed to retrieve users' info from firebase."
                String errorMessage = "Sign up feature is currently unavailable.";
                Log.v(TAG, errorMessage);
                displayMessageInSnackbar(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT);
            } else {
                // Check if the given username exists in the firebase realtime database
                boolean existenceResult = checkUserExistenceInFirebase(TAG, username, task1);
                // If so, display "user already exists" message in a Snackbar.
                if (existenceResult) {
                    String errorMessage = String.format("User %s already exists in the database.", username);
                    Log.v(TAG, errorMessage);
                    displayMessageInSnackbar(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT);
                } else { // If not, try to add user to the database.
                    Log.v(TAG, "Trying to add user " + username);
                    // Construct a User object using the given username
                    User user = new User(username);
                    DatabaseReference newUserDatabaseReference = usersDatabaseReference.child(user.getUsername());
                    // Set this user info at the given location
                    Task<Void> task2 = newUserDatabaseReference.setValue(user);

                    task2.addOnCompleteListener(anotherTask -> {
                        if (!task2.isSuccessful()) {
                            String errorMessage = String.format("Failed to add user %s.", user.getUsername());
                            Log.v(TAG, errorMessage);
                            displayMessageInSnackbar(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT);
                        } else {
                            String message = String.format("User %s added.", user.getUsername());
                            Log.v(TAG, message);
                            displayMessageInSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
                        }
                    });
                }
            }
        });
    }

}
