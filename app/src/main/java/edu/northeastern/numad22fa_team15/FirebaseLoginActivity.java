package edu.northeastern.numad22fa_team15;

import static edu.northeastern.numad22fa_team15.utils.commonUtils.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.atomic.AtomicBoolean;

import edu.northeastern.numad22fa_team15.model.User;

public class FirebaseLoginActivity extends AppCompatActivity {

    private static final String TAG = "FirebaseLoginActivity___";

    private EditText usernameEditText;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "OnCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_login);

        usernameEditText = (EditText) findViewById(R.id.username_edit_text);

        // Get the instance of the Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();
        Log.v(TAG, "Firebase Database " + firebaseDatabase.toString());
        // Get the "Users" reference for our database.
        usersDatabaseReference = firebaseDatabase.getReference().child("users");
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
    }

    /**
     * This method gets called when the user clicks the LOGIN button. It will try to log in
     * with the username in the usernameEditText field.
     * @param view view
     */
    public void firebaseUsernameLogin(View view) {
        closeKeyboard(this.getApplicationContext(), view);
        String usernameInput = usernameEditText.getText().toString();
        // XH TO DO:
        // Need to add "username login" logic.
        if (!usernameInputChecker(usernameInput, view)) {
            return;
        }
        // Retrieve existing users from the firebase realtime database
        // Check if the given username matches with any user in the database
        // If yes, open the FirebaseFriendListActivity class
        // If no, generate a dialog that asks the user to sign up or check the validity of the username
        // XH Note: Seems like this need to be made to a thread.
        boolean userExistence = checkUserExistenceInFirebase(usernameInput);
        if (userExistence) {
            Intent intent = new Intent(getApplicationContext(), FirebaseFriendListActivity.class);
            startActivity(intent);
        } else { // User does not exist in the firebase realtime database
            Log.v(TAG, "SB");
        }
//        Log.v(TAG, String.format("Match found: %s", userExistence));
//        Intent intent = new Intent(getApplicationContext(), FirebaseFriendListActivity.class);
//        startActivity(intent);
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

    private boolean checkUserExistenceInFirebase(String usernameInput) {
        Log.v(TAG, "Trying to retrieve existing users' info from firsbase.");
        Task<DataSnapshot> t = usersDatabaseReference.get();
        AtomicBoolean existenceResult = new AtomicBoolean(false);

        t.addOnCompleteListener(task -> {
            if(!t.isSuccessful()){
                // A more specific message should be "Failed to retrieve users' info from firebase."
                String errorMessage = "Login feature is currently unavailable.";
                Log.v(TAG, errorMessage);
                displayMessageInSnackbar(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT);
            } else {
                for (DataSnapshot dataSnapshot : t.getResult().getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    String existingUsername = user.getUsername();
                    Log.v(TAG, String.format("Existing user: %s", existingUsername));
                    if (usernameInput.equals(existingUsername)) {
                        existenceResult.set(true);
                        break;
                    }
                }
            }
        });
        return existenceResult.get();
    }

    /**
     * This helper method will try to add a User object with the given username to the firebase
     * realtime database.
     * @param username a username
     */
    private void addUserToFirebase(@NonNull String username) {
        Log.v(TAG, "Trying to add user " + username);
        // Construct a User object using the given username
        User user = new User(username);
        DatabaseReference newUserDatabaseReference = usersDatabaseReference.child(user.getUsername());
        // Set this user info at the given location
        Task<Void> t = newUserDatabaseReference.setValue(user);

        t.addOnCompleteListener(task -> {
            if(!t.isSuccessful()){
                String errorMessage = String.format("Failed to add user %s.", user.getUsername());
                Log.v(TAG, errorMessage);
                displayMessageInSnackbar(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT);
            } else {
                String message = String.format("User %s added.", user.getUsername());
                Log.v(TAG, message);
                displayMessageInSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
            }
        });

        // XH Note: No need to set listener here. Kept for future use. Will Delete.
//        // Read from the database by listening for a change to that item.
//        newUserDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again whenever data
//                // at this location is updated.
//                User userValue = dataSnapshot.getValue(User.class);
//                Log.v(TAG, String.format("Updated username: %s", userValue.getUsername()));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Failed to add value
//                String errorMessage = String.format("Failed to add value. %s", error.toException());
//                Log.v(TAG, errorMessage);
//            }
//        });
    }

//    /**
//     * Display the given message in the given view for a certain duration.
//     * @param view the view
//     * @param message a message
//     * @param duration the duration of the snackbar
//     */
//    private void displayMessageInSnackbar(View view, @NonNull String message, int duration) {
//        Snackbar.make(view, message, duration).show();
//    }
//
//    /**
//     * This method close the keyboard on the given view.
//     * @param view the view
//     */
//    private void closeKeyboard(View view) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
//    }

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