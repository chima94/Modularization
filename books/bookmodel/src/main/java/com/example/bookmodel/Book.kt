package com.example.bookmodel

import com.example.generalbook.GeneralBook

data class Book(
    override val image: String?,
    override val title: String?,
    override val author: String?,
    override val id: String,
    override val extension: String?,
    override val pages: String?,
    override val size: String?,
    override val year: String?
): GeneralBook