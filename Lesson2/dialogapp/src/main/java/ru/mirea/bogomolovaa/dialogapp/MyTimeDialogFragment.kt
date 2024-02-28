package ru.mirea.bogomolovaa.dialogapp

import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MyTimeDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireActivity(),
            { view, hourOfDay, minute ->
                // Time selection handle
                (requireActivity() as MainActivity).callbackTime(view, hourOfDay, minute)
            },
            hour,
            minute,
            true
        )
    }
}
