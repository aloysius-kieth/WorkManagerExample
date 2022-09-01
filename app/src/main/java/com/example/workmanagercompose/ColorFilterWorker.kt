package com.example.workmanagercompose

import android.content.Context
import android.graphics.*
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@Suppress("BlockingMethodInNonBlockingContext")
class ColorFilterWorker(
    private val context: Context,
    private val workParam: WorkerParameters
) : CoroutineWorker(context, workParam) {

    override suspend fun doWork(): Result {
        val imageFile = workParam.inputData.getString(WorkerKeys.IMAGE_URI)
            ?.toUri()
            ?.toFile()
        delay(5000L)
        return imageFile?.let { file ->
            val bmp = BitmapFactory.decodeFile(file.absolutePath)
            val resultBmp = bmp.copy(bmp.config, true)
            val paint = Paint()
            paint.colorFilter = LightingColorFilter(0x08FF04, 1)
            val canvas = Canvas(resultBmp)
            canvas.drawBitmap(resultBmp, 0f, 0f, paint)

            withContext(Dispatchers.IO) {
                val resultImagrFile = File(context.cacheDir, "color-filter-image.jpg")
                val outputStream = FileOutputStream(resultImagrFile)
                val success = resultBmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                if (success) {
                    Result.success(
                        workDataOf(
                            WorkerKeys.FILTER_URI to resultImagrFile.toUri().toString()
                        )
                    )
                } else {
                    Result.failure()
                }
            }
        } ?: Result.failure()
    }
}