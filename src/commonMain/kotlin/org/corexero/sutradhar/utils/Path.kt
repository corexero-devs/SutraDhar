package org.corexero.sutradhar.utils

data class Path(
    val name: String,
    val extension: String
) {
    val nameWithExtension: String
        get() = if (extension.isNotEmpty()) "$name.$extension" else name

    val nameWithoutExtension: String
        get() = name

}