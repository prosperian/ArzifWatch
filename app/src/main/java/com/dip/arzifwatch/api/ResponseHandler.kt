package com.dip.arzifwatch.api

import retrofit2.Response
import java.util.regex.Pattern

sealed class ResponseHandler<T> {
    companion object {

        fun <T> handleResponse(response: Response<T>): Resource<T> {
            return when (create(response)) {
                is ApiSuccessResponse -> {
                    Resource.Success(response.body()!!)
                }
                is ApiEmptyResponse -> {
                    Resource.Error("Empty Response")
                }
                is ApiErrorResponse -> {
                    Resource.Error(response.code().toString())
                }
            }
        }

        private fun <T> create(response: Response<T>): ResponseHandler<T> {
            try {
                return if (response.isSuccessful) {
                    val body = response.body()
                    if (body == null || response.code() == 204) {
                        ApiEmptyResponse()
                    } else {
                        ApiSuccessResponse(
                            body = body,
                            linkHeader = response.headers().get("link")
                        )
                    }
                } else {
                    val msg = response.errorBody()?.string()
                    val errorMsg = if (msg.isNullOrEmpty()) {
                        response.message()
                    } else {
                        msg
                    }
                    ApiErrorResponse(errorMsg ?: "unknown error")
                }
            } catch (e: Exception) {
                return ApiErrorResponse("time out error")
            }

        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ResponseHandler<T>()

data class ApiSuccessResponse<T>(
    val body: T,
    val links: Map<String, String>
) : ResponseHandler<T>() {
    constructor(body: T, linkHeader: String?) : this(
        body = body,
        links = linkHeader?.extractLinks() ?: emptyMap()
    )

    companion object {
        private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")

        private fun String.extractLinks(): Map<String, String> {
            val links = mutableMapOf<String, String>()
            val matcher = LINK_PATTERN.matcher(this)

            while (matcher.find()) {
                val count = matcher.groupCount()
                if (count == 2) {
                    links[matcher.group(2)!!] = matcher.group(1)!!
                }
            }
            return links
        }

    }
}

data class ApiErrorResponse<T>(val errorMessage: String) : ResponseHandler<T>()