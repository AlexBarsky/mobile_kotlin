package ru.mirea.bogomolovaa.dialogapp

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MyDateDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireActivity(),
            { view, year, month, dayOfMonth ->
                // Date selection handle
                (requireActivity() as MainActivity).callbackDate(view, year, month, dayOfMonth)
            },
            year,
            month,
            day
        )
    }
}