package ru.mirea.bogomolovaa.mireaproject.ui.backgroundtask

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import ru.mirea.bogomolovaa.mireaproject.ui.backgroundtask.BackgroundTaskFragment.Companion.NUMBER_OF_POINTS_KEY
import ru.mirea.bogomolovaa.mireaproject.ui.backgroundtask.BackgroundTaskFragment.Companion.PI_APPROXIMATION_KEY
import java.lang.Math.random

class PiCalculationWorker (
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    private val TAG = PiCalculationWorker::class.java.simpleName

    override fun doWork(): Result {
        val numberOfPoints = inputData.getLong(NUMBER_OF_POINTS_KEY, 0)
        Log.d(TAG, "doWork: Started with $numberOfPoints points")

        try {
            var insideCircleCount = 0
            for (i in 0 until numberOfPoints) {
                val x = random()
                val y = random()

                if (x * x + y * y <= 1.0) {
                    insideCircleCount++
                }

                // Вычисляем число π в реальном времени
                val piApproximation = 4.0 * insideCircleCount / (i + 1)
                Log.d(TAG, "Current approximation of Pi: $piApproximation")
            }
            val pi = 4.0 * insideCircleCount / numberOfPoints

            val outputData = Data
                .Builder()
                .putDouble(PI_APPROXIMATION_KEY, pi)
                .build()

            Log.d(TAG, "doWork: Finished")
            return Result.success(outputData)
        } catch (e: Exception) {
            Log.e(TAG, "Error calculating Pi", e)
            return Result.failure()
        }
    }
}