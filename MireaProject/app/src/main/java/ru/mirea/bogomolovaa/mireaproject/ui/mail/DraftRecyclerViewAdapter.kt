package ru.mirea.bogomolovaa.mireaproject.ui.mail

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import ru.mirea.bogomolovaa.mireaproject.ui.mail.placeholder.PlaceholderContent.PlaceholderItem
import ru.mirea.bogomolovaa.mireaproject.databinding.FragmentDraftBinding
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class DraftRecyclerViewAdapter(
    private val files: List<String>
) : RecyclerView.Adapter<DraftRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentDraftBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = files[position]

        holder.fileNumberView.text = position.toString()
        holder.fileNameView.text = file.take(file.lastIndexOf('-'))
        holder.itemView.setOnClickListener {
            holder.fileContentView.text =
                readFileContent(holder.itemView.context, file)
        }
    }

    override fun getItemCount(): Int = files.size

    inner class ViewHolder(binding: FragmentDraftBinding) : RecyclerView.ViewHolder(binding.root) {
        val fileNumberView = binding.fileNumber
        val fileNameView = binding.fileName
        val fileContentView = binding.fileContent
    }

    private fun readFileContent(context: Context, fileName: String): String? {
        var inputStream: FileInputStream? = null
        try {
            inputStream = context.openFileInput(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var text: String?

            while (bufferedReader.readLine().also { text = it } != null) {
                stringBuilder.append("$text\n")
            }

            inputStream.close()
            return stringBuilder.toString()
        } catch (e: IOException) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        return null
    }
}