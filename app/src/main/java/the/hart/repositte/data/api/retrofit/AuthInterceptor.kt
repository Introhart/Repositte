package the.hart.repositte.data.api.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            // TODO :: Just imagine that the token is stored securely :)
            .addHeader("Authorization", "Bearer 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")
            .build()
        return chain.proceed(newRequest)
    }
}