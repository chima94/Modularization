package com.example.downloadsdestination

import android.net.Uri
import androidx.core.net.toUri

private const val URI_ID = "com.example.books"
private val baseUri = "app://$URI_ID".toUri()

const val BOOK_ID_PARAM = "book_id"
val DOWNLOADED_BOOK_NAME_URI_PATTERN = "$baseUri/$DOWNLOADS/${BOOK_ID_PARAM}={${BOOK_ID_PARAM}}"
fun makeBookIdUri(bookName: String): Uri = "${baseUri}/$DOWNLOADS/${BOOK_ID_PARAM}=$bookName".toUri()