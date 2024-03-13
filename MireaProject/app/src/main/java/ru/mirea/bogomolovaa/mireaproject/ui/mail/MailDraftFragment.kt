package ru.mirea.bogomolovaa.mireaproject.ui.mail

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import ru.mirea.bogomolovaa.mireaproject.R
import ru.mirea.bogomolovaa.mireaproject.databinding.FragmentMailBinding

class MailDraftFragment : DialogFragment() {

    companion object {
        val TAG: String = MailDraftFragment::class.java.simpleName
    }

    private val viewModel: MailViewModel by activityViewModels()

    private lateinit var _binding: FragmentMailBinding
    private val binding get() = _binding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            _binding = FragmentMailBinding.inflate(
                requireActivity().layoutInflater,
                null,
                false
            )
            val view = binding.root

            val builder = AlertDialog.Builder(activity)

            val emailToEditText = binding.emailToEditText
            val messageEditText = binding.messageEditText

            viewModel.emailTo.observeForever {
                if (it.isNotEmpty()) {
                    emailToEditText.setText(it)
                }
            }
            viewModel.emailMessage.observeForever {
                if (it.isNotEmpty()) {
                    messageEditText.setText(it)
                }
            }

            builder.setView(view)
                .setPositiveButton(R.string.save_draft_button) { dialog, _ ->
                    saveDraft(
                        emailToEditText.text.toString(),
                        messageEditText.text.toString()
                    )
                    viewModel.onFileSaved.value = true
                    viewModel.emailTo.value = ""
                    viewModel.emailMessage.value = ""
                    dialog.cancel()
                }
                .setNeutralButton(R.string.cancel_draft_button) { dialog, _ ->
                    emailToEditText.text.toString().let {
                        if (it.isNotEmpty()) {
                            viewModel.emailTo.value = it
                        }
                    }
                    messageEditText.text.toString().let {
                        if (it.isNotEmpty()) {
                            viewModel.emailMessage.value = it
                        }
                    }
                    dialog.cancel()
                }
                .setNegativeButton(R.string.discard_draft_button) { dialog, _ ->
                    viewModel.onFileSaved.value = false
                    viewModel.emailTo.value = ""
                    viewModel.emailMessage.value = ""
                    dialog.cancel()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun saveDraft(email: String, emailMessage: String) {
        Thread {
            try {
                val draftFileName = "$email-draft.txt"
                val draft = requireContext().openFileOutput(draftFileName, MODE_PRIVATE)
                draft.write(emailMessage.toByteArray())
                draft.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}