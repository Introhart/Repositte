package the.hart.repositte.data.api.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val yourToken = "" // Cause I can
        val request = chain.request()
        val newRequest = request.newBuilder()
            // TODO :: Just imagine that the token is stored securely :)
            .addHeader("Authorization", "Bearer $yourToken")
            .build()
        return chain.proceed(newRequest)
    }
}