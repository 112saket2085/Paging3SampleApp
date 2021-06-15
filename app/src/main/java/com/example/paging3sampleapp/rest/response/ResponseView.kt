package com.example.paging3sampleapp.rest.response

object ResponseView {

    data class BaseResponse<T>(
        val Response: String,
        val Error: String?,
        val Search: T?,
        val totalResults: String
    )
}