package edu.northeastern.numad22fa_team15.activities.peakActivities.addTransaction;

import static edu.northeastern.numad22fa_team15.utils.CommonUtils.getByteArrayFromInputStream;
import static edu.northeastern.numad22fa_team15.utils.CommonUtils.nullOrEmptyInputChecker;
import static edu.northeastern.numad22fa_team15.utils.CommonUtils.setPictureToGivenImageView;
import static edu.northeastern.numad22fa_team15.utils.CommonUtils.updateSummaryTable;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Arrays;

import edu.northeastern.numad22fa_team15.R;
import edu.northeastern.numad22fa_team15.activities.peakActivities.homePage.PeakHomePage;
import edu.northeastern.numad22fa_team15.utils.Category;
import edu.northeastern.numad22fa_team15.utils.DBHelper;
import edu.northeastern.numad22fa_team15.utils.IDBHelper;

public class AddTransactionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "AddTransactionActivity______";

    private static final int CAMERA_PERMISSION_CODE = 1;
    private byte[] receiptPictureByteArray;

    private IDBHelper dbHelper;
    private View bottomSheetView;
    private Spinner addTipSpinner;
    private EditText expenseEditText;
    private EditText descriptionEditText;
    private TextView categoryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        dbHelper = new DBHelper(AddTransactionActivity.this);
    }

    public void addTransactionCategoryDining(View view) {
        Log.d(TAG, "Category Dining was clicked.");
        addTransactionWithTipHelper(Category.DINING);
    }

    public void addTransactionCategoryGroceries(View view) {
        Log.d(TAG, "Category Groceries was clicked.");
        addTransactionWithoutTipHelper(Category.GROCERIES);
    }

    public void addTransactionCategoryShopping(View view) {
        Log.d(TAG, "Category Shopping was clicked.");
        addTransactionWithTipHelper(Category.SHOPPING);
    }

    public void addTransactionCategoryLiving(View view) {
        Log.d(TAG, "Category Living was clicked.");
        addTransactionWithoutTipHelper(Category.LIVING);
    }

    public void addTransactionCategoryEntertainment(View view) {
        Log.d(TAG, "Category Entertainment was clicked.");
        addTransactionWithTipHelper(Category.ENTERTAINMENT);
    }

    public void addTransactionCategoryEducation(View view) {
        Log.d(TAG, "Category Education was clicked.");
        addTransactionWithoutTipHelper(Category.EDUCATION);
    }

    public void addTransactionCategoryBeauty(View view) {
        Log.d(TAG, "Category Beauty was clicked.");
        addTransactionWithTipHelper(Category.BEAUTY);
    }

    public void addTransactionCategoryTransportation(View view) {
        Log.d(TAG, "Category Transportation was clicked.");
        addTransactionWithoutTipHelper(Category.TRANSPORTATION);
    }

    public void addTransactionCategoryHealth(View view) {
        Log.d(TAG, "Category Health was clicked.");
        addTransactionWithoutTipHelper(Category.HEALTH);
    }

    public void addTransactionCategoryTravel(View view) {
        Log.d(TAG, "Category Travel was clicked.");
        addTransactionWithTipHelper(Category.TRAVEL);
    }

    public void addTransactionCategoryPet(View view) {
        Log.d(TAG, "Category Pet was clicked.");
        addTransactionWithoutTipHelper(Category.PET);
    }

    public void addTransactionCategoryOther(View view) {
        Log.d(TAG, "Category Other was clicked.");
        addTransactionWithTipHelper(Category.OTHER);
    }

    /**
     * Helper method for categories when adding transaction that requires tip calculation.
     * @param category category that requires tip calculation
     */
    private void addTransactionWithTipHelper(Category category) {
        // Create and display the BottomSheetDialog
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this,
                R.style.BottomSheetDialogTheme);
        bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.layout_bottom_sheet_dialog_with_tip_backup, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());

        // REF: https://stackoverflow.com/questions/41591733/bottom-sheet-landscape-issue
        bottomSheetDialog.setOnShowListener(dialogInterface -> {
            mBehavior.setPeekHeight(1000);
            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        categoryText = (TextView) bottomSheetView.findViewById(R.id.tv_category_with_tip);
        String categoryString = category.toString();
        categoryText.setText(categoryString);
        bottomSheetDialog.show();

        // Add setOnClickListener to VIEW button
        Button previewReceiptPhotoViewButton = (Button) bottomSheetView.findViewById(R.id.btn_view_receipt);
        previewReceiptPhotoViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReceiptPicturePreviewDialog(v);
            }
        });

        // Add tip spinner
        addTipSpinner = (Spinner) bottomSheetView.findViewById(R.id.spinner_add_tip);
        addTipSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.tip_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        addTipSpinner.setAdapter(adapter);

        // Add setOnClickListener to ADD RECEIPT button
        Button addReceiptButton = (Button) bottomSheetView.findViewById(R.id.btn_add_receipt);
        addReceiptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(v);
            }
        });

        // Add setOnClickListener to DONE button
        Button confirmTransactionDoneButton = (Button) bottomSheetView.findViewById(R.id.btn_done_add_transaction);
        confirmTransactionDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmAddTransaction(bottomSheetDialog, categoryString);
            }
        });
    }

    private void showReceiptPicturePreviewDialog(View v) {
        // Check if receiptPictureByteArray contains value
        // If no, make a toast
        if (receiptPictureByteArray == null || receiptPictureByteArray.length == 0) {
            Toast.makeText(getApplicationContext(), "No receipt photo added.", Toast.LENGTH_SHORT).show();
            return;
        }
        // If yes, create a dialog
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Receipt Picture Preview");
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_receipt_picture_preview, null);
        ImageView imageView = (ImageView) dialogView.findViewById(R.id.receipt_photo_preview_image_view);
        Log.d(TAG, "showReceiptPicturePreviewDialog() method: " + receiptPictureByteArray);
        if (imageView == null) {
            Log.d(TAG, "Something wrong");
        }
        // Set receipt picture to given image view
        setPictureToGivenImageView(receiptPictureByteArray, imageView);
        Button okButton = (Button) dialogView.findViewById(R.id.ok_button_drpp);
        b.setView(dialogView);
        AlertDialog alert = b.create();
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] tipChoices = getResources().getStringArray(R.array.tip_choices);
        String selectedTipChoice = tipChoices[position];
        Log.d(TAG, String.format("Selected tip choice: %s", selectedTipChoice));
        // If other tip choices are selected, check if amount has value in it.
        if (!selectedTipChoice.equals(tipChoices[0])) {
            expenseEditText = (EditText) bottomSheetView.findViewById(R.id.et_transaction_amount_tip);
            String expenseString = expenseEditText.getText().toString();
            // If not, ask user to put a value in the amount field.
            if (nullOrEmptyInputChecker(expenseString)) {
                addTipSpinner.setSelection(0);
                String addTipErrorMessage = "Amount is required if you want to calculate the tip.";
                Toast.makeText(getApplicationContext(), addTipErrorMessage, Toast.LENGTH_SHORT).show();
            } else { // If yes, calculate tip and update amount.
                calculateTipAndUpdateAmount(expenseEditText, selectedTipChoice);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void showBottomSheetDialog(View view) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddTransactionActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            ImagePicker.with(this)
                    .crop(9f, 16f)
                    .compress(1024)
                    .maxResultSize(480, 480)
                    .start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImagePicker.with(this)
                        .crop(9f, 16f)
                        .compress(1024)
                        .maxResultSize(480, 480)
                        .start();
            } else {
                String msg = "Camera permission denied, please allow permission first before accessing the camera features";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Uri uri = intent.getData();
        if (uri != null) {
            InputStream inputStream;
            try {
                inputStream = getContentResolver().openInputStream(uri);
                receiptPictureByteArray =  getByteArrayFromInputStream(inputStream);
            } catch (FileNotFoundException e) {
                Log.v(TAG, "File not found");
            } catch (IOException e) {
                Log.v(TAG, "Failed to get byte array from input stream");
            }
        }
    }

    /**
     * This method should not be called if user selects 0% tip.
     * @param expenseEditText expense EditText
     * @param selectedTipChoice selected tip choice
     */
    private void calculateTipAndUpdateAmount(EditText expenseEditText, String selectedTipChoice) {
        float expense = Float.parseFloat(expenseEditText.getText().toString());
        // Retrieve tip percentage from selectedTipChoice String
        float selectedTip = Float.parseFloat(selectedTipChoice.substring(0, 2)) / 100;
        DecimalFormat df = new DecimalFormat("0.00");
        float updatedAmount = expense * (1 + selectedTip);
        expenseEditText.setText(df.format(updatedAmount));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * Linked with DONE button on bottomSheetDialog for transaction with tip.
     * @param bottomSheetDialog bottom sheet dialog
     * @param category expense category
     */
    private void confirmAddTransaction(BottomSheetDialog bottomSheetDialog, String category) {
        Log.d(TAG, "Add transaction DONE button was clicked");

        expenseEditText = (EditText) bottomSheetView.findViewById(R.id.et_transaction_amount_tip);
        descriptionEditText = (EditText) bottomSheetView.findViewById(R.id.et_description_tip);

        String expenseString = expenseEditText.getText().toString();
        Log.d(TAG, "Expense before constraining decimal places " + expenseString);
        String description = descriptionEditText.getText().toString();

        Log.d(TAG, String.format("Category: %s. Expense: %s. Description: %s",
                category, expenseString, description));

        if (nullOrEmptyInputChecker(expenseString, description)) {
            String message = "All fields are required.";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            return;
        }

        float expense = Float.parseFloat(expenseString);
        // 2 Decimal Places constraint
        String expenseTwoDecimal = String.format("%.2f", expense);
        expense = Float.parseFloat(expenseTwoDecimal);
        Log.d(TAG, "Expense after constraining decimal places " + String.valueOf(expense));
        LocalDateTime now = LocalDateTime.now();
        String transactionDate = String.valueOf(now);
        Log.d(TAG, "Receipt Photo to be added: " + Arrays.toString(receiptPictureByteArray));
        // Try to add transaction to the transactionEntry Table
        boolean addTransaction = dbHelper.addTranTableTransaction(expense, description, category, transactionDate, receiptPictureByteArray);
        boolean updateSummary = updateSummaryTable(dbHelper, expense, category, transactionDate);
        if (!updateSummary) {
            Log.d(TAG, "Failed to update the summary table after adding a transaction.");
        }
        String transactionMessage = "Failed to add Transaction.";
        if (addTransaction) {
            transactionMessage = "Transaction added successfully.";
        }
        Toast.makeText(getApplicationContext(), transactionMessage, Toast.LENGTH_SHORT).show();

        receiptPictureByteArray = null;
        bottomSheetDialog.dismiss();
    }

    /**
     * Helper method for adding transaction without tip categories.
     * @param category the category chosen that does not require tip calculation
     */
    private void addTransactionWithoutTipHelper(Category category) {
        // Create and display the BottomSheetDialog
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this,
                R.style.BottomSheetDialogTheme);
        bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.layout_bottom_sheet_dialog_without_tip_backup, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        bottomSheetDialog.setOnShowListener(dialogInterface -> {
            mBehavior.setPeekHeight(1000);
            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        // set category text
        categoryText = (TextView) bottomSheetView.findViewById(R.id.tv_category_without_tip);
        String categoryString = category.toString();
        categoryText.setText(categoryString);
        bottomSheetDialog.show();

        // Add setOnClickListener to VIEW button
        Button previewReceiptPhotoViewButton = (Button) bottomSheetView.findViewById(R.id.btn_view_receipt_no_tip);
        previewReceiptPhotoViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReceiptPicturePreviewDialog(v);
            }
        });

        // Add setOnClickListener to ADD RECEIPT button
        Button addReceiptButton = (Button) bottomSheetView.findViewById(R.id.btn_add_receipt_no_tip);
        addReceiptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog(v);
            }
        });

        // Add setOnClickListener to DONE button
        Button confirmTransactionDoneButton = (Button) bottomSheetView.findViewById(R.id.btn_done_add_transaction_no_tip);
        confirmTransactionDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmAddTransactionWithoutTip(bottomSheetDialog, categoryString);
            }
        });
    }

    /**
     * Linked with DONE button for bottomSheetDialog without tip
     */
    private void confirmAddTransactionWithoutTip(BottomSheetDialog bottomSheetDialog, String category) {
        Log.d(TAG, "Add transaction DONE button was clicked for transaction without tip ");

        expenseEditText = (EditText) bottomSheetView.findViewById(R.id.et_transaction_amount_no_tip);
        descriptionEditText = (EditText) bottomSheetView.findViewById(R.id.et_description_no_tip);

        String expenseString = expenseEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        Log.d(TAG, String.format("Category: %s. Expense: %s. Description: %s",
                category, expenseString, description));

        if (nullOrEmptyInputChecker(expenseString, description)) {
            String message = "All fields are required.";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            return;
        }

        float expense = Float.parseFloat(expenseString);
        // 2 Decimal Places constraint
        String expenseTwoDecimal = String.format("%.2f", expense);
        expense = Float.parseFloat(expenseTwoDecimal);
        LocalDateTime now = LocalDateTime.now();
        String transactionDate = String.valueOf(now);
        Log.d(TAG, "Receipt Photo to be added: " + Arrays.toString(receiptPictureByteArray));
        // Try to add transaction to the transactionEntry Table
        boolean addTransaction = dbHelper.addTranTableTransaction(expense, description, category, transactionDate, receiptPictureByteArray);
        boolean updateSummary = updateSummaryTable(dbHelper, expense, category, transactionDate);
        if (!updateSummary) {
            Log.d(TAG, "Failed to update the summary table after adding a transaction.");
        }
        String transactionMessage = "Failed to add Transaction.";
        if (addTransaction) {
            transactionMessage = "Transaction added successfully.";
        }
        Toast.makeText(getApplicationContext(), transactionMessage, Toast.LENGTH_SHORT).show();

        receiptPictureByteArray = null;
        bottomSheetDialog.dismiss();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), PeakHomePage.class);
        startActivity(intent);
        finish();
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
