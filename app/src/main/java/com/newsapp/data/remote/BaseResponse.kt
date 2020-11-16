package com.newsapp.data.remote

import com.newsapp.models.NewsResponse
import com.newsapp.utils.ApiConstants
import retrofit2.Response

abstract class BaseResponse {

    protected suspend fun getApiResponse(call: suspend () -> Response<NewsResponse>): Resource<NewsResponse> {
        try {
            val response = call.invoke()

            // Codes based on https://newsapi.org/docs/errors
            when (response.code()) {
                400 -> return error(ApiConstants.ERROR_400)
                401 -> return error(ApiConstants.ERROR_401)
                429 -> return error(ApiConstants.ERROR_429)
                500 -> return error(ApiConstants.ERROR_500)
            }

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        return Resource.error("Network Error: $message")
    }
}