package edu.northeastern.numad22fa_team15.activities.peakActivities.lockScreen;

import static edu.northeastern.numad22fa_team15.utils.CommonUtils.displayMessageInSnackbar;
import static edu.northeastern.numad22fa_team15.utils.CommonUtils.nullOrEmptyInputChecker;
import static edu.northeastern.numad22fa_team15.utils.CommonUtils.updateSummaryTable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.numad22fa_team15.R;
import edu.northeastern.numad22fa_team15.activities.MainActivity;
import edu.northeastern.numad22fa_team15.activities.peakActivities.homePage.PeakHomePage;
import edu.northeastern.numad22fa_team15.models.databaseModels.SummaryModel;
import edu.northeastern.numad22fa_team15.models.databaseModels.UserModel;
import edu.northeastern.numad22fa_team15.utils.Category;
import edu.northeastern.numad22fa_team15.utils.DBHelper;
import edu.northeastern.numad22fa_team15.utils.IDBHelper;

public class PeakLockScreen extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private static final String TAG = "PeakLockScreen___";
    private View view_01, view_02, view_03, view_04;
    private Button btn_01, btn_02, btn_03, btn_04, btn_05, btn_06, btn_07, btn_08, btn_09, btn_00, btn_clear;

    private List<String> numbers_list;
    private String passcodeInput;
    private String num_01, num_02, num_03, num_04;

    private String selectedCategoryChoice;

    private IDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peak_lock_screen);

        dbHelper = new DBHelper(PeakLockScreen.this);

        numbers_list = new ArrayList<>();
        passcodeInput = "";
        selectedCategoryChoice = Category.OTHER.toString();

        initializeComponents();

        LocalDateTime now = LocalDateTime.now();
        String currentDate = String.valueOf(now);
        Integer currentYear = Integer.parseInt(currentDate.substring(0,4));
        Integer currentMonth = Integer.parseInt(currentDate.substring(5,7));

        SummaryModel lastSummary = dbHelper.retrieveLatestSummaryInfoTableSummary();
        Log.d(TAG, "Last Summary Year: " + lastSummary.getYear() + ". Current Year: " + currentYear);
        Log.d(TAG, "Last Summary Month: " + lastSummary.getMonth() + ". Current Month: " + currentMonth);
        if (lastSummary.getYear() != currentYear && lastSummary.getMonth() != currentMonth) {
            Log.d(TAG, "No summary found for this current month. Adding summary to database...");
            float budget = lastSummary.getTotalBudget();
            float diningBudget = lastSummary.getDiningBudget();
            float groceriesBudget = lastSummary.getGroceriesBudget();
            float shoppingBudget = lastSummary.getShoppingBudget();
            float livingBudget = lastSummary.getLivingBudget();
            float entertainmentBudget = lastSummary.getEntertainmentBudget();
            float educationBudget = lastSummary.getEducationBudget();
            float beautyBudget = lastSummary.getBeautyBudget();
            float transportationBudget = lastSummary.getTransportationBudget();
            float healthBudget = lastSummary.getHealthBudget();
            float travelBudget = lastSummary.getTravelBudget();
            float petBudget = lastSummary.getPetBudget();
            float otherBudget = lastSummary.getOtherBudget();

            boolean addSummary = dbHelper.addSummaryTableSummary(currentYear, currentMonth, budget,
                    diningBudget, groceriesBudget, shoppingBudget, livingBudget, entertainmentBudget,
                    educationBudget, beautyBudget, transportationBudget, healthBudget, travelBudget,
                    petBudget, otherBudget);

            String budgetMessage = "Fail to add Summary";
            if (addSummary) {
                budgetMessage = "Successfully added summary";
            }
            Log.d(TAG, budgetMessage);

            float remainingBudget = lastSummary.getTotalBudget() - lastSummary.getCurrentExpense();
            boolean updateSaving = dbHelper.updateSavingSoFarTableSaving(remainingBudget);

            String savingMessage = "Fail to update Saving";
            if (updateSaving) {
                savingMessage = "Successfully updated saving";
            }
            Log.d(TAG, savingMessage);
        }
    }

    private void initializeComponents() {
        view_01 = findViewById(R.id.view_01);
        view_02 = findViewById(R.id.view_02);
        view_03 = findViewById(R.id.view_03);
        view_04 = findViewById(R.id.view_04);

        btn_01 = findViewById(R.id.btn_01);
        btn_02 = findViewById(R.id.btn_02);
        btn_03 = findViewById(R.id.btn_03);
        btn_04 = findViewById(R.id.btn_04);
        btn_05 = findViewById(R.id.btn_05);
        btn_06 = findViewById(R.id.btn_06);
        btn_07 = findViewById(R.id.btn_07);
        btn_08 = findViewById(R.id.btn_08);
        btn_09 = findViewById(R.id.btn_09);
        btn_00 = findViewById(R.id.btn_00);
        btn_clear = findViewById(R.id.btn_clear);

        btn_01.setOnClickListener(this);
        btn_02.setOnClickListener(this);
        btn_03.setOnClickListener(this);
        btn_04.setOnClickListener(this);
        btn_05.setOnClickListener(this);
        btn_06.setOnClickListener(this);
        btn_07.setOnClickListener(this);
        btn_08.setOnClickListener(this);
        btn_09.setOnClickListener(this);
        btn_00.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_01:
                numbers_list.add("1");
                passNumber(numbers_list);
                break;
            case R.id.btn_02:
                numbers_list.add("2");
                passNumber(numbers_list);
                break;
            case R.id.btn_03:
                numbers_list.add("3");
                passNumber(numbers_list);
                break;
            case R.id.btn_04:
                numbers_list.add("4");
                passNumber(numbers_list);
                break;
            case R.id.btn_05:
                numbers_list.add("5");
                passNumber(numbers_list);
                break;
            case R.id.btn_06:
                numbers_list.add("6");
                passNumber(numbers_list);
                break;
            case R.id.btn_07:
                numbers_list.add("7");
                passNumber(numbers_list);
                break;
            case R.id.btn_08:
                numbers_list.add("8");
                passNumber(numbers_list);
                break;
            case R.id.btn_09:
                numbers_list.add("9");
                passNumber(numbers_list);
                break;
            case R.id.btn_00:
                numbers_list.add("0");
                passNumber(numbers_list);
                break;
            case R.id.btn_clear:
                numbers_list.clear();
                passNumber(numbers_list);
                break;
        }

    }

    private void passNumber(List<String> numbers_list) {

        if (numbers_list.size() == 0) {
            view_01.setBackgroundResource(R.drawable.bg_view_grey_oval);
            view_02.setBackgroundResource(R.drawable.bg_view_grey_oval);
            view_03.setBackgroundResource(R.drawable.bg_view_grey_oval);
            view_04.setBackgroundResource(R.drawable.bg_view_grey_oval);
        } else {
            switch (numbers_list.size()) {
                case 1:
                    num_01 = numbers_list.get(0);
                    view_01.setBackgroundResource(R.drawable.bg_view_dark_oval);
                    break;
                case 2:
                    num_02 = numbers_list.get(1);
                    view_02.setBackgroundResource(R.drawable.bg_view_dark_oval);
                    break;
                case 3:
                    num_03 = numbers_list.get(2);
                    view_03.setBackgroundResource(R.drawable.bg_view_dark_oval);
                    break;
                case 4:
                    num_04 = numbers_list.get(3);
                    view_04.setBackgroundResource(R.drawable.bg_view_dark_oval);
                    passcodeInput = num_01 + num_02 + num_03 + num_04;
                    matchPasscode(passcodeInput);
                    break;
            }
        }
    }

    private void matchPasscode(String passcodeInput) {
        if (getPasscode().equals(passcodeInput)) {
            startActivity(new Intent(this, PeakHomePage.class));
        } else {

            String message = "Wrong passcode. Please try again.";
            displayMessageInSnackbar(view_01.getRootView(), message, Snackbar.LENGTH_SHORT);

            // clear the current password input
            numbers_list.clear();
            passNumber(numbers_list);
        }
    }

    private String getPasscode() {
        UserModel user = dbHelper.retrieveUserInfoTableUser();
        String passcode = user.getPasscode();

        return passcode;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * This method gets called when the QUICK ADD button was clicked. It brings a dialog that allows
     * the user to quickly add a transaction to the database.
     * @param view view
     */
    public void quickAddDialog(View view) {
        Log.d(TAG, "QUICK ADD button was clicked.");

        // Create and display the BottomSheetDialog
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this,
                R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.layout_bottom_sheet_quick_add_transaction, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        bottomSheetDialog.setOnShowListener(dialogInterface -> {
            mBehavior.setPeekHeight(800);
        });

        // Add tip spinner
        Spinner categorySpinner = (Spinner) bottomSheetView.findViewById(R.id.spinner_category);
        categorySpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        categorySpinner.setAdapter(adapter);
        bottomSheetDialog.show();

        // Add setOnClickListener to DONE button
        Button confirmTransactionDoneButton = (Button) bottomSheetView.findViewById(R.id.btn_done_add_transaction_quick_add);
        confirmTransactionDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmAddTransaction(bottomSheetView, bottomSheetDialog, selectedCategoryChoice);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] categoryChoices = getResources().getStringArray(R.array.category_choices);
        selectedCategoryChoice = categoryChoices[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void confirmAddTransaction(View bottomSheetView, BottomSheetDialog bottomSheetDialog, String category) {
        Log.d(TAG, "Add transaction DONE button was clicked for quick add transaction.");

        EditText expenseEditText = (EditText) bottomSheetView.findViewById(R.id.et_transaction_amount_quick_add);
        EditText descriptionEditText = (EditText) bottomSheetView.findViewById(R.id.et_description_quick_add);

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
        // Try to add transaction to the transactionEntry Table
        // Set null as receiptPhoto's value
        boolean addTransaction = dbHelper.addTranTableTransaction(expense, description, category, transactionDate, null);
        boolean updateSummary = updateSummaryTable(dbHelper, expense, category, transactionDate);
        if (!updateSummary) {
            Log.d(TAG, "Failed to update the summary table after adding a transaction.");
        }
        String transactionMessage = "Failed to add transaction.";
        if (addTransaction) {
            transactionMessage = "Transaction added successfully.";
        }
        Toast.makeText(getApplicationContext(), transactionMessage, Toast.LENGTH_SHORT).show();
        bottomSheetDialog.dismiss();
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
