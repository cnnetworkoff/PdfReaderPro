package com.pdfreader.cn.util

import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.pdfreader.cn.domain.model.PdfFile
import java.io.File
import timber.log.Timber

object DocumentIntents {

    fun openDocument(context: Context, file: PdfFile): Boolean {
        return try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                File(file.path)
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, resolveMimeType(file))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(Intent.createChooser(intent, "Open with"))
            true
        } catch (error: Exception) {
            Timber.e(error, "Failed to open document: ${file.path}")
            false
        }
    }

    fun resolveMimeType(file: PdfFile): String {
        if (file.mimeType.isNotBlank() && file.mimeType != "application/octet-stream") {
            return file.mimeType
        }

        val extension = file.name.substringAfterLast('.', "").lowercase()
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "*/*"
    }
}
