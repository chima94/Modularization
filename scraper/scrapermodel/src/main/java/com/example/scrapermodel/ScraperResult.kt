package com.example.scrapermodel

sealed class ScraperResult{

    data class Success(val link : String): ScraperResult()
    object Loading : ScraperResult()
    object UrlNotFound : ScraperResult()
    object TimeOut : ScraperResult()
    object Idle : ScraperResult()
    object Error : ScraperResult()
    object NoConnection : ScraperResult()
}