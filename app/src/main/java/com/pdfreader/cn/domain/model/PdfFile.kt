package com.pdfreader.cn.domain.model

import android.net.Uri

data class PdfFile(
    val id: Long,
    val name: String,
    val path: String,
    val uri: Uri,
    val mimeType: String = "application/pdf",
    val size: Long,
    val dateModified: Long,
    val dateAdded: Long,
    val parentFolder: String,
    val pageCount: Int = 0,
    val isFavorite: Boolean = false
) {
    val displayName: String
        get() = name.substringBeforeLast(".", name)

    val folderName: String
        get() = parentFolder.substringAfterLast("/").ifEmpty { "Storage" }

    val isPdf: Boolean
        get() = mimeType == "application/pdf" || name.endsWith(".pdf", ignoreCase = true)

    val extensionLabel: String
        get() = name.substringAfterLast('.', "").ifBlank {
            mimeType.substringAfterLast('/').ifBlank { "FILE" }
        }.uppercase()
}
