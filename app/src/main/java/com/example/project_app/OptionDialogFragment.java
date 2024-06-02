package com.example.project_app;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class OptionDialogFragment extends DialogFragment {

    // Constants for SharedPreferences
    private static final String PREF_NAME = "MyPrefs";
    private static final String PREF_OPTION_SELECTED = "OptionSelected";

    private String[] mainFilters;
    private String[][] subFilters;
    private boolean[] checkedOptions;
    private boolean[] mainFiltersClicked;
    private Context context; // Store context reference

    public OptionDialogFragment(String[] mainFilters, String[][] subFilters, boolean[] checkedOptions) {
        this.mainFilters = mainFilters;
        this.subFilters = subFilters;
        this.checkedOptions = checkedOptions;
        this.mainFiltersClicked = new boolean[mainFilters.length];
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use context variable instead of requireContext() throughout the fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Options");

        // Set up single-choice items for main filters
        builder.setSingleChoiceItems(mainFilters, -1, (dialog, which) -> {
            if (mainFiltersClicked[which]) {
                // If the main filter is already clicked, uncheck and save options
                checkedOptions[which] = false;
                saveSelectedOptions();
            } else {
                // If the main filter is not clicked, show its sub-filters
                showSubFiltersDialog(which);
            }
            mainFiltersClicked[which] = !mainFiltersClicked[which];
            dialog.dismiss(); // Dismiss dialog after selection
        });

        builder.setNegativeButton("Cancel", null);
        return builder.create();
    }

    // Method to show sub-filters dialog for a specific main filter
    private void showSubFiltersDialog(int mainFilterIndex) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(mainFilters[mainFilterIndex]);

            // Initialize checked options for sub-filters dialog and set to main filter's options
            boolean[] subCheckedOptions = new boolean[subFilters[mainFilterIndex].length];
            for (int i = 0; i < subFilters[mainFilterIndex].length; i++) {
                subCheckedOptions[i] = checkedOptions[calculateIndex(mainFilterIndex, i)];
            }

            // Set up multi-choice items for the sub-filters of the selected main filter
            builder.setMultiChoiceItems(subFilters[mainFilterIndex], subCheckedOptions, (dialog, which, isChecked) -> {
                // Update the checked state in the subCheckedOptions array
                subCheckedOptions[which] = isChecked;
            });

            builder.setPositiveButton("OK", (dialog, which) -> {
                // Save selected options when OK is clicked
                for (int i = 0; i < subCheckedOptions.length; i++) {
                    checkedOptions[calculateIndex(mainFilterIndex, i)] = subCheckedOptions[i];
                }
                saveSelectedOptions(); // Save options without dismissing the dialog
            });

            builder.setNegativeButton("Back", null);

            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
        }
    }

    // Store context reference when fragment is attached
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void saveSelectedOptions() {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for (int i = 0; i < checkedOptions.length; i++) {
            editor.putBoolean(PREF_OPTION_SELECTED + i, checkedOptions[i]);
        }
        editor.apply();
    }

    private static int calculateIndex(int mainFilterIndex, int subFilterIndex) {
        return mainFilterIndex * 3 + subFilterIndex; // Assuming there are always 3 sub-filters for each main filter
    }

    public static boolean[] getSavedOptions(Context context, int optionCount) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean[] savedOptions = new boolean[optionCount];
        for (int i = 0; i < optionCount; i++) {
            savedOptions[i] = preferences.getBoolean(PREF_OPTION_SELECTED + i, false);
        }
        return savedOptions;
    }
}

