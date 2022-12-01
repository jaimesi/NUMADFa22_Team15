package edu.northeastern.numad22fa_team15.activities.peakActivities.profilePage;

import static edu.northeastern.numad22fa_team15.utils.CommonUtils.closeKeyboard;
import static edu.northeastern.numad22fa_team15.utils.CommonUtils.displayMessageInSnackbar;
import static edu.northeastern.numad22fa_team15.utils.CommonUtils.nullOrEmptyInputChecker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import edu.northeastern.numad22fa_team15.R;
import edu.northeastern.numad22fa_team15.activities.peakActivities.PeakAddTransaction;
import edu.northeastern.numad22fa_team15.utils.DBHelper;
import edu.northeastern.numad22fa_team15.utils.IDBHelper;

public class TestBudgetActivity extends AppCompatActivity {

    private static final String TAG = "TestBudget___";
    private TextInputEditText budgetTextInputEditText;
    private IDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_budget);

        dbHelper = new DBHelper(TestBudgetActivity.this);
        budgetTextInputEditText = findViewById(R.id.budget_textinput_edittext);
    }

    public void addBudget(View view) {
        closeKeyboard(this.getApplicationContext(), view);

        String budgetString = budgetTextInputEditText.getText().toString();

        if (nullOrEmptyInputChecker(budgetString)) {
            String message = "You need to enter a budget.";
            displayMessageInSnackbar(view, message, Snackbar.LENGTH_SHORT);
            return;
        }

        float totalBudget = Float.parseFloat(budgetString);
        float category_budget = totalBudget / 12;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentDate = String.valueOf(now);

        Integer currentYear = Integer.parseInt(currentDate.substring(0,4));
        Integer currentMonth = Integer.parseInt(currentDate.substring(5,7));

        boolean addSummary = dbHelper.addSummaryTableSummary(currentYear, currentMonth, totalBudget,
                category_budget, category_budget, category_budget, category_budget, category_budget,
                category_budget, category_budget, category_budget, category_budget, category_budget,
                category_budget, category_budget);

        String budgetMessage = "Fail to add Summary";
        if (addSummary) {
            budgetMessage = "Successfully added summary";
        }
        displayMessageInSnackbar(view, budgetMessage, Snackbar.LENGTH_SHORT);
    }

    public void updateBudget(View view) {
        closeKeyboard(this.getApplicationContext(), view);

        String budgetString = budgetTextInputEditText.getText().toString();

        float totalBudget = Float.parseFloat(budgetString);
        float category_budget = totalBudget / 12;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentDate = String.valueOf(now);

        Integer currentYear = Integer.parseInt(currentDate.substring(0,4));
        Integer currentMonth = Integer.parseInt(currentDate.substring(5,7));

        boolean updateSummary = dbHelper.updateSummaryTableSummary(currentYear, currentMonth, totalBudget,
                category_budget, category_budget, category_budget, category_budget, category_budget,
                category_budget, category_budget, category_budget, category_budget, category_budget,
                category_budget, category_budget);

        String budgetMessage = "Fail to add Summary";
        if (updateSummary) {
            budgetMessage = "Successfully added summary";
        }
        displayMessageInSnackbar(view, budgetMessage, Snackbar.LENGTH_SHORT);
    }
}