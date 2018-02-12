package uk.co.agfsoft.skyweather.volley

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Circle Internet Financial
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.HttpStack

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.ProtocolVersion
import org.apache.http.StatusLine
import org.apache.http.entity.BasicHttpEntity
import org.apache.http.message.BasicHeader
import org.apache.http.message.BasicHttpResponse
import org.apache.http.message.BasicStatusLine

import java.io.IOException
import java.util.concurrent.TimeUnit

import okhttp3.Call
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody

/**
 * OkHttp backed [HttpStack][com.android.volley.toolbox.HttpStack] that does not
 * use okhttp-urlconnection
 * and https://gist.github.com/alashow/c96c09320899e4caa06b
 * Modifications by Dmitriy Kazimirov, e-mail:dmitriy.kazimirov@viorsan.com 2017
 */
class OkHttp3Stack
/**
 * Construct class with list of network interceptors for usage with Stetho http://ligol.github.io/blog/2015/05/05/discovering-and-using-stetho-with-some-network-library/
 * use like this
 * ArrayList<Interceptor> interceptors=new ArrayList<>();
 * interceptors.add(new StethoInterceptor());
 * OkHttp3Stack okHttp3Stack=new OkHttp3Stack(interceptors);
 * @param interceptors
</Interceptor> */
(private val mInterceptors: List<Interceptor>) : HttpStack {

    @Throws(IOException::class, AuthFailureError::class)
    override fun performRequest(request: com.android.volley.Request<*>, additionalHeaders: Map<String, String>): HttpResponse {

        val clientBuilder = OkHttpClient.Builder()
        val timeoutMs = request.timeoutMs

        clientBuilder.connectTimeout(timeoutMs.toLong(), TimeUnit.MILLISECONDS)
        clientBuilder.readTimeout(timeoutMs.toLong(), TimeUnit.MILLISECONDS)
        clientBuilder.writeTimeout(timeoutMs.toLong(), TimeUnit.MILLISECONDS)

        val okHttpRequestBuilder = okhttp3.Request.Builder()
        okHttpRequestBuilder.url(request.url)

        val headers = request.headers
        for (name in headers.keys) {
            okHttpRequestBuilder.addHeader(name, headers[name])
        }

        for (name in additionalHeaders.keys) {
            okHttpRequestBuilder.addHeader(name, additionalHeaders[name])
        }

        setConnectionParametersForRequest(okHttpRequestBuilder, request)

        for (interceptor in mInterceptors) {
            clientBuilder.addNetworkInterceptor(interceptor)
        }

        val client = clientBuilder.build()

        val okHttpRequest = okHttpRequestBuilder.build()
        val okHttpCall = client.newCall(okHttpRequest)
        val okHttpResponse = okHttpCall.execute()

        val responseStatus = BasicStatusLine(parseProtocol(okHttpResponse.protocol()), okHttpResponse.code(), okHttpResponse.message())
        val response = BasicHttpResponse(responseStatus)
        response.entity = entityFromOkHttpResponse(okHttpResponse)

        val responseHeaders = okHttpResponse.headers()
        var i = 0
        val len = responseHeaders.size()
        while (i < len) {
            val name = responseHeaders.name(i)
            val value = responseHeaders.value(i)
            if (name != null) {
                response.addHeader(BasicHeader(name, value))
            }
            i++
        }

        return response
    }

    @Throws(IOException::class)
    private fun entityFromOkHttpResponse(r: Response): HttpEntity {
        val entity = BasicHttpEntity()
        val body = r.body()

        entity.content = body!!.byteStream()
        entity.contentLength = body.contentLength()
        entity.setContentEncoding(r.header("Content-Encoding"))

        if (body.contentType() != null) {
            entity.setContentType(body.contentType()!!.type())
        }
        return entity
    }

    @Throws(IOException::class, AuthFailureError::class)
    private fun setConnectionParametersForRequest(builder: okhttp3.Request.Builder, request: com.android.volley.Request<*>) {
        when (request.method) {
            Request.Method.DEPRECATED_GET_OR_POST -> {
                // Ensure backwards compatibility.  Volley assumes a request with a null body is a GET.
                val postBody = request.postBody
                if (postBody != null) {
                    builder.post(RequestBody.create(MediaType.parse(request.postBodyContentType), postBody))
                }
            }
            Request.Method.GET -> builder.get()
            Request.Method.DELETE -> builder.delete()
            Request.Method.POST -> builder.post(createRequestBody(request)!!)
            Request.Method.PUT -> builder.put(createRequestBody(request)!!)
            Request.Method.HEAD -> builder.head()
            Request.Method.OPTIONS -> builder.method("OPTIONS", null)
            Request.Method.TRACE -> builder.method("TRACE", null)
            Request.Method.PATCH -> builder.patch(createRequestBody(request)!!)
            else -> throw IllegalStateException("Unknown method type.")
        }
    }

    private fun parseProtocol(p: Protocol): ProtocolVersion {
        when (p) {
            Protocol.HTTP_1_0 -> return ProtocolVersion("HTTP", 1, 0)
            Protocol.HTTP_1_1 -> return ProtocolVersion("HTTP", 1, 1)
            Protocol.SPDY_3 -> return ProtocolVersion("SPDY", 3, 1)
            Protocol.HTTP_2 -> return ProtocolVersion("HTTP", 2, 0)
        }
    }

    @Throws(AuthFailureError::class)
    private fun createRequestBody(r: Request<*>): RequestBody? {
        val body = r.body ?: return null

        return RequestBody.create(MediaType.parse(r.bodyContentType), body)
    }
}