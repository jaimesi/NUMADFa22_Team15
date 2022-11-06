package edu.northeastern.numad22fa_team15.activities.firebaseActivities;

import static edu.northeastern.numad22fa_team15.utils.commonUtils.displayMessageInSnackbar;
import static edu.northeastern.numad22fa_team15.utils.firebaseUtils.checkUserExistenceInFirebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.northeastern.numad22fa_team15.R;
import edu.northeastern.numad22fa_team15.activities.MainActivity;
import edu.northeastern.numad22fa_team15.firebaseFriendTvRecyclerUtil.FriendTvAdapter;
import edu.northeastern.numad22fa_team15.model.Friend;
import edu.northeastern.numad22fa_team15.model.StickerRecord;

public class FirebaseFriendListActivity extends AppCompatActivity {

    private static final String TAG = "FirebaseFriendListActivity______";
    private static final String CHANNEL_ID = "channel_one";
    private static final int NOTIFICATION_UNIQUE_ID = 7;
    private static int notificationGeneration = 0;
    public static String currentUsername;

    private TextView currentUserTextView;
    private TextView numOfStickersSentTextView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersDatabaseReference;
    private DatabaseReference userDatabaseReference;
    private DatabaseReference numOfStickersDatabaseReference;
    private DatabaseReference friendsDatabaseReference;
    private DatabaseReference stickerRecordsDatabaseReference;

    private List<Friend> friendResults;
    private RecyclerView friendsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "OnCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_friend_list);

        // Call createNotificationChannel before a notification is sent.
        createNotificationChannel();

        // Retrieve current user's username from intent and set it to the currentUserTextView TextView.
        Intent intent = getIntent();
        String username = intent.getStringExtra("current_user");
        currentUserTextView = findViewById(R.id.current_userTV);
        currentUserTextView.setText(username);
        currentUsername = username;

        numOfStickersSentTextView = findViewById(R.id.sticker_numTV);

        // Get the instance of the Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();
        Log.v(TAG, "Firebase Database " + firebaseDatabase.toString());
        usersDatabaseReference = firebaseDatabase.getReference().child("users");
        // Get the "user" reference for our database.
        userDatabaseReference = usersDatabaseReference.child(username);

        friendResults = new ArrayList<Friend>();
        friendsRecyclerView = findViewById(R.id.friends_recycler_view);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendsRecyclerView.setAdapter(new FriendTvAdapter(friendResults, this));

        // Get the "numOfStickerSent" reference for our database and add ValueEventListener to it.
        numOfStickersDatabaseReference = userDatabaseReference.child("numOfStickersSent");
        addEventListenerToNumOfStickersDatabaseReference();

        // Get the "friends" reference for our database and add ValueEventListener to it.
        friendsDatabaseReference = userDatabaseReference.child("friends");
        addEventListenerToFriendsDatabaseReference();

        // Get the "stickerRecords" reference for our database and add ValueEventListener to it.
        stickerRecordsDatabaseReference = userDatabaseReference.child("stickerRecords");
        addChildEventListenerToStickerRecordsDatabaseReference();
    }

    /**
     * Listening for a change to the stickerRecords database reference.
     */
    private void addChildEventListenerToStickerRecordsDatabaseReference() {
        ChildEventListener childEventListener = new ChildEventListener(){
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Push a notification when detecting a new sticker record.
                Log.v(TAG, "New sticker record detected.");
                StickerRecord stickerRecord = snapshot.getValue(StickerRecord.class);
                if (!stickerRecord.getProcessedByServer() && currentUserTextView.getText().toString().equals(stickerRecord.getReceiver())) {
                    int stickerID = stickerRecord.getStickerID();
                    // Set processedByServer to true.
                    DatabaseReference processedStatusDatabaseReference = stickerRecordsDatabaseReference.child(String.valueOf(stickerID)).child("processedByServer");
                    Task<Void> t = processedStatusDatabaseReference.setValue(true);

                    t.addOnCompleteListener(task -> {
                        if (!t.isSuccessful()) {
                            String errorMessage = String.format("Fail to process sticker record %d", stickerID);
                            Log.v(TAG, errorMessage);
                        } else {
                            String message = String.format("Sticker record %d is processed by the server.", stickerID);
                            Log.v(TAG, message);
                            buildNotification(stickerRecord);
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // No need to implement
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // No need to implement
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // No need to implement
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v(TAG, "Failed to retrieve info from stickerRecords database reference.");
            }
        };
        stickerRecordsDatabaseReference.addChildEventListener(childEventListener);
    }

    // TODO: Back stack not handling properly.
    /**
     * Build notification based on sticker record.
     * @param stickerRecord sticker record
     */
    private void buildNotification(StickerRecord stickerRecord) {
//        Intent intent = new Intent(getApplicationContext(), FirebaseStickerHistoryActivity.class);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // Add current user's username to the intent.
        intent.putExtra("current_user", currentUserTextView.getText().toString());
        PendingIntent checkIntent = PendingIntent.getActivity(getApplicationContext(),
                (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_IMMUTABLE);

        int stickerResourceID = stickerRecord.getStickerResourceID();
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), stickerResourceID);
        String stickerName = stickerRecord.getStickerName();
        String friendUsername = stickerRecord.getSender();
        String contentText = String.format("Sticker %s sent by friend %s", stickerName, friendUsername);

        // Build notification
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle("New sticker received!")
                .setSmallIcon(R.drawable.notification_icon)
                .setLargeIcon(largeIcon)
                .setContentText(contentText)
                .setContentIntent(checkIntent)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(largeIcon).bigLargeIcon(null))
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Hide notification after it is selected
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        // Allow multiple notifications
        notificationGeneration++;
        notificationManager.notify(NOTIFICATION_UNIQUE_ID + notificationGeneration, notification);
    }

    /**
     * This helper method creates the notification channel.
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Listening for a change to the friends database reference.
     */
    private void addEventListenerToFriendsDatabaseReference() {
        // Listening for a change to the friends item
        friendsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Need to check for null because user may not have any friends. SAD =)
                Object friendsValue = dataSnapshot.getValue();
                if (friendsValue == null) {
                    Log.v(TAG, "No friends detected for the current user.");
                    return;
                }
                Map<String, String> friendsMap = (Map<String, String>) friendsValue;
                // Update friends RecyclerView
                friendResults.clear();
                for (Map.Entry<String, String> entry : friendsMap.entrySet()) {
                    String friendUsername = entry.getKey();
                    Log.v(TAG, String.format("FRIEND: %s.", friendUsername));
                    Friend friend = new Friend(friendUsername);
                    friendResults.add(friend);
                }
                friendsRecyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to update value
                String errorMessage = String.format("Failed to update value. %s", error.toException());
                Log.v(TAG, errorMessage);
            }
        });
    }

    /**
     * Listening for a change to the numOfStickersSent database reference.
     */
    private void addEventListenerToNumOfStickersDatabaseReference() {
        // Listening for a change to the numOfStickersSent item
        numOfStickersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again whenever data
                // at this location is updated.
                Integer numOfStickersValue = dataSnapshot.getValue(Integer.class);
                Log.v(TAG, String.format("Updated number of stickers sent: %d", numOfStickersValue));
                numOfStickersSentTextView.setText(String.valueOf(numOfStickersValue));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to update value
                String errorMessage = String.format("Failed to update value. %s", error.toException());
                Log.v(TAG, errorMessage);
            }
        });
    }

    /**
     * Clicking the STICKER HISTORY button will start the FirebaseStickerHistoryActivity activity.
     * @param view view
     */
    public void stickerHistoryActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), FirebaseStickerHistoryActivity.class);
        // Add current user's username to the intent.
        intent.putExtra("current_user", currentUserTextView.getText().toString());
        startActivity(intent);
    }

    /**
     * Clicking the ADD FRIEND button will open up the add friend dialog.
     * @param view view
     */
    public void addFriendDialog(View view) {
        // Pop up window for adding friend
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setCancelable(false);
        b.setTitle("Add a Friend");
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_add_friend, null);
        b.setView(dialogView);

        // Set up buttons
        b.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // (1) Check if the given username is the same as the current username
                // (1a) [YES] Display message in Snackbar "Cannot add yourself as a friend."
                // (1b) [NO] Go to step (2)
                EditText friendUsernameEditText = dialogView.findViewById(R.id.friend_username_edit_text);
                String friendUsername = friendUsernameEditText.getText().toString();
                Log.v(TAG, String.format("Trying to add user %s as friend.", friendUsername));
                Log.v(TAG, "(1) Check if friend's username is the same as the current username.");
                String currentUsername = currentUserTextView.getText().toString();
                if (friendUsername.equals(currentUsername)) {
                    String errorMessage = "Cannot add yourself as a friend.";
                    Log.v(TAG, errorMessage);
                    displayMessageInSnackbar(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT);
                    return;
                }
                // (2) Check if the current user already has this person as a friend
                // (2a) [YES] Display message in Snackbar "You already have this user as a friend."
                // (2b) [NO] Go to step (3)
                Log.v(TAG, "(2) Check if current user already has this user as a friend.");
                boolean existence = usernameExistenceInFriendsList(friendUsername);
                if (existence) {
                    String errorMessage = "You already have this user as a friend.";
                    Log.v(TAG, errorMessage);
                    displayMessageInSnackbar(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT);
                    return;
                }
                // (3) Check if the given username exists in the firebase realtime database
                // (3a) [YES] Go to step (4)
                // (3b) [NO] Display message in Snackbar "User does not exist."

                // (4) Try to add the given username in current user's friends list
                // (4a) [YES] Display message in Snackbar "Friend added successfully."
                // (4b) [NO] Display error message in Snackbar "Failed to add user as friend."
                Log.v(TAG, "(3) Check if friend's username exists in the database.");
                Task<DataSnapshot> task1 = usersDatabaseReference.get();

                task1.addOnCompleteListener(task -> {
                    if(!task1.isSuccessful()){
                        // A more specific message should be "Failed to retrieve users' info from firebase."
                        String errorMessage = "Add friend feature is currently unavailable.";
                        Log.v(TAG, errorMessage);
                        displayMessageInSnackbar(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT);
                    } else {
                        // Check if the given username exists in the firebase realtime database
                        boolean existenceResult = checkUserExistenceInFirebase(TAG, friendUsername, task1);
                        // If yes, step (4)
                        if (existenceResult) {
                            Task<Void> task2 = friendsDatabaseReference.child(friendUsername).setValue(friendUsername);

                            task2.addOnCompleteListener(anotherTask -> {
                                if(!task2.isSuccessful()){
                                    String errorMessage = String.format("Failed to add friend %s.", friendUsername);
                                    Log.v(TAG, errorMessage);
                                    displayMessageInSnackbar(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT);
                                } else {
                                    // Add yourself as a friend the that user's friends list
                                    Task<Void> task3 = usersDatabaseReference.child(friendUsername).child("friends").child(currentUsername).setValue(currentUsername);

                                    task3.addOnCompleteListener(finalTask -> {
                                        if(!task3.isSuccessful()){
                                            String errorMessage = String.format("Failed to add friend %s.", friendUsername);
                                            friendsDatabaseReference.child(friendUsername).removeValue();
                                            Log.v(TAG, errorMessage);
                                            displayMessageInSnackbar(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT);
                                        } else {
                                            String message = String.format("Friend %s added.", friendUsername);
                                            Log.v(TAG, message);
                                            displayMessageInSnackbar(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
                                        }
                                    });
                                }
                            });
                        } else { // If no, display message in a Snackbar.
                            String errorMessage = "User does not exist.";
                            Log.v(TAG, errorMessage);
                            displayMessageInSnackbar(findViewById(android.R.id.content), errorMessage, Snackbar.LENGTH_SHORT);
                        }
                    }
                });
            }
        });
        b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alert = b.create();
        alert.show();
    }

    /**
     * Return true if username exists in the friend list. Otherwise, return false.
     * @param friendUsername friend username
     * @return true if username exists in the friend list. Otherwise, return false
     */
    private boolean usernameExistenceInFriendsList(String friendUsername) {
        for (Friend existingFriend : this.friendResults) {
            String existingFriendUsername = existingFriend.getUsername();
            if (friendUsername.equals(existingFriendUsername)) {
                return true;
            }
        }
        return false;
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
        currentUserTextView.setText("");
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