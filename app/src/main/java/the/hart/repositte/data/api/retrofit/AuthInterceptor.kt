package the.hart.repositte.data.api.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer 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")
            .build()
        return chain.proceed(newRequest)
    }
}