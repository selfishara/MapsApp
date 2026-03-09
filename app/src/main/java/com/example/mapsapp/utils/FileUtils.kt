package com.example.mapsapp.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object FileUtils {

    fun createImageUri(context: Context): Uri? {
        val file = File.createTempFile(
            "temp_image_",
            ".jpg",
            context.cacheDir
        )

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
}