package ru.mirea.bogomolovaa.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class AlertDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        android.app.AlertDialog.Builder(activity)
            .setTitle("Hello MIREA!")
            .setMessage("Is success close?")
            .setIcon(R.mipmap.ic_launcher_round)
            .setPositiveButton("Going further") { dialog, _ ->
                (requireActivity() as MainActivity).onOkClicked()
                dialog.cancel()
            }
            .setNeutralButton("On pause") { dialog, _ ->
                (requireActivity() as MainActivity).onNeutralClicked()
                dialog.cancel()
            }
            .setNegativeButton("No") { dialog, _ ->
                (requireActivity() as MainActivity).onCancelClicked()
                dialog.cancel()
            }
            .create()
}