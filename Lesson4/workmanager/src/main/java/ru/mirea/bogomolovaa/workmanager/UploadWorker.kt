package ru.mirea.bogomolovaa.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class UploadWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    private val TAG = UploadWorker::class.java.simpleName

    override fun doWork(): Result {
        Log.d(TAG, "doWork: Started")
        try {
            TimeUnit.SECONDS.sleep(10)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        Log.d(TAG, "doWork: Finished")
        return Result.success()
    }

}