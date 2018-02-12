package uk.co.agfsoft.skyweather.volley

import android.content.Context
import android.util.Log

import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.HttpHeaderParser.parseCharset
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.HashMap

import javax.inject.Inject

import uk.co.agfsoft.skyweather.BuildConfig
import uk.co.agfsoft.skyweather.R
import java.nio.charset.Charset


/**
 * Base class for all volley requests
 *
 *
 * Provides an interface to the volley library functions to simplify and abstract
 * clients from the underlying complexities.
 *
 *
 * This implementation creates by default a request with the following characteristics:
 * - HTTP GET method
 * - Does NOT cache responses (call setShouldCache(true) in the subclass constructor to enable caching
 *
 *
 * Created by drew on 22/01/2016.
 */
abstract class BaseVolleyRequest<T> {
    protected var isUIBlocking: Boolean = false
    protected var requestUrl: String? = null
    private var startTs: Long = 0

    @Inject
    lateinit var requestQueue: RequestQueue
    @Inject
    lateinit var gson: Gson

    protected abstract val method: Int

    protected abstract val responseClass: Class<*>


    /**
     * Provide headers for the request.
     *
     *
     * Subclass requests that require additional custom headers should override this method and add
     * their own headers to the returned Map.
     * Each map entry consists of the header name (the key) and the header value.
     * Volley uses the map contents to build the request headers.
     * Note that the super class headers must also be included
     *
     * @return Map containing headers for request
     * @throws AuthFailureError
     */
    @Throws(AuthFailureError::class)
    protected fun headers() : Map<String, String> = mapOf()

    /**
     * Provide parameters for the request
     *
     *
     * Subclass requests that require additional parameters should override this method and add
     * their own parameters to the returned Map.
     * Each map entry consists of the parameter name (the key) and the parameter value.
     * Volley uses the map contents to build the query string for the request.
     * Note that the super class parameters must also be included
     *
     * @return Map containing parameters for the query string
     * @throws AuthFailureError
     */
    open fun getQueryParams(): Map<String, String>? = null

    /**
     * Provide post/put parameters for the request
     *
     *
     * Subclass requests that require additional parameters should override this method and add
     * their own parameters to the returned Map.
     * Each map entry consists of the parameter name (the key) and the parameter value.
     * Volley uses the map contents to build the a form data body.  Typically we use json bodies so
     * this is unlikely to be used
     *
     * @return Map containing parameters for the query string
     * @throws AuthFailureError
     */
    protected val params: Map<String, String>?
        @Throws(AuthFailureError::class)
        get() = null

    /**
     * Provide body content type for the request
     *
     *
     * Override this method if you wish to send another type of content in the body.
     * Volley uses this to set the content type header in the request.
     *
     * @return
     */
    val bodyContentType: String
        get() = "application/text"

    /**
     * Provide the body for the request
     *
     * Override this method to create the body content as a byte array.  The format will be dependent
     * on the body content type for the request.
     *
     * @return
     */
    val body: ByteArray?
        @Throws(AuthFailureError::class)
        get() = null

    open fun getUrl(): String? = null

    /**
     * Call this method to actually submit the request once it has been properly initialised
     *
     * @param context  This is used as a tag.  All requests with a given tag can be cancelled
     * by calling RequestQueue.cancelAll(Object tag)
     * @param listener - the listener for notifying the outcome to
     */
    fun submit(context: Context, isUIBlocking: Boolean, listener: Listener<T>?) {

        if (BuildConfig.DEBUG) {
            val sb = StringBuilder("Network request: ")
            sb.append(methodToString(method) + ": " + buildUrl())
            val tag = this.javaClass.simpleName
            Log.i(tag, sb.toString())
            startTs = System.currentTimeMillis()
        }

        val request = object : Request<T>(method, buildUrl(), ErrorListener<Any>(isUIBlocking)) {
            override fun parseNetworkResponse(response: NetworkResponse): Response<T>? {
                try {
                    if (BuildConfig.DEBUG) {

                        Log.i(this@BaseVolleyRequest.javaClass.simpleName,
                                "Network success: " +
                                        response.data.size.toString() + " bytes, " +
                                        "elapsed: " + (System.currentTimeMillis() - startTs).toString() + "mS, " +
                                        "  data: " + String(response.data, 0, Math.min(40, response.data.size)) +
                                        if (response.data.size > 40) "..." else "")
                    }
                    return parseResponse(response)
                } catch (e: JsonSyntaxException) {
                    Response.error<Any>(VolleyError("Unable to parse " + responseClass.simpleName + " when processing network response"))
                    return null
                } catch (e: UnsupportedEncodingException) {
                    Response.error<Any>(VolleyError("Unsupported character set when processing network response for " + responseClass.simpleName))
                    return null
                }

            }

            /**
             * Called by Volley to deliver the response.
             *
             *
             * Simply check whether a listener has been set for
             * the class and if so notify by calling the onSuccess method.
             *
             * @param response
             */
            override fun deliverResponse(response: T) {
                listener?.onSuccess(response)
            }

            /**
             * Called by Volley to deliver an error response
             *
             *
             * Simply check whether a listener has been set for
             * the class and if so notify by calling the onError method.
             *
             * @param error
             */
            override fun deliverError(error: VolleyError) {
                if (BuildConfig.DEBUG) {
                    val detail = if (error is ServerError) String(error.networkResponse.data) else "Networking error"
                    Log.e(this@BaseVolleyRequest.javaClass.simpleName,
                            "Network ERROR: " + error.toString() +
                                    "  detail: " + detail +
                                    "  elapsed: " + (System.currentTimeMillis() - startTs).toString() + "mS")
                }

                listener?.onError(error)
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                return this@BaseVolleyRequest.headers()
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                return this@BaseVolleyRequest.params
            }

            override fun getBodyContentType(): String {
                return this@BaseVolleyRequest.bodyContentType
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray? {
                return this@BaseVolleyRequest.body
            }
        }


        (request.errorListener as ErrorListener<T>).setListener(listener)
        request.tag = context
        requestQueue!!.add(request)
    }

    /**
     * Parse the network response data.  This method should be overridden by subclasses to perform
     * custom parsing.  The approach ensures all exception handling is handled within this class.
     *
     * @param response
     * @return The parsed response
     * @throws JsonSyntaxException
     * @throws UnsupportedEncodingException
     */
    @Throws(JsonSyntaxException::class, UnsupportedEncodingException::class)
    protected fun parseResponse(response: NetworkResponse): Response<T> {
        val jsonString = String(response.data,
                Charset.forName(parseCharset(response.headers, DEFAULT_CHARSET)))

        val result = gson!!.fromJson(jsonString, responseClass)
        return Response.success(result, null) as Response<T>
    }

    /**
     * Listener interface for notifying the outcome of the volley request.
     *
     * @param <T> - the type of the response returned by the class.
    </T> */
    interface Listener<T> {
        fun onSuccess(response: T)

        fun onError(error: VolleyError)
    }

    /**
     * Provide an appropriate string resource ID for the error and log the error if not a connection
     * issue.
     *
     * @param error - the VolleyError
     * @return
     */
    fun handleError(error: VolleyError): Int {
        if (error is NoConnectionError || error is TimeoutError) {
            return R.string.error_generic_connection
        } else {
            logError(this.javaClass, error)
            return R.string.error_generic_unknown
        }
    }


    /**
     * Volley error listener
     *
     *
     * This class is passed by Volley and the onErrorResponse method is called by Volley in the
     * event of an error occurring.
     *
     *
     * Simply check whether to hide the progress dialog and if a listener has been set then
     * call the onError method
     *
     * @param <T>
    </T> */
    class ErrorListener<T>(private val isUIBlocking: Boolean) : Response.ErrorListener {
        private var listener: Listener<T>? = null

        override fun onErrorResponse(error: VolleyError) {
            if (listener != null) {
                listener!!.onError(error)
            }

        }

        fun setListener(listener: Listener<T>?) {
            this.listener = listener
        }
    }

    //</editor-fold>

    private fun methodToString(method: Int): String {
        when (method) {
            -1 -> return "DEPRECATED_GET_OR_POST"
            0 -> return "GET"
            1 -> return "POST"
            2 -> return "PUT"
            3 -> return "DELETE"
            4 -> return "HEAD"
            5 -> return "OPTIONS"
            6 -> return "TRACE"
            7 -> return "PATCH"
            else -> return "UNKNOWN"
        }
    }

    private fun buildUrl(): String {
        val sb = StringBuilder(getUrl())
        val queryParams = getQueryParams()
        if (queryParams != null) {
            sb.append("?")
            sb.append(encodeParameters(queryParams, "UTF-8"))
        }
        return sb.toString()
    }

    private fun encodeParameters(params: Map<String, String>?, paramsEncoding: String): String {
        val encodedParams = StringBuilder()
        try {
            for ((key, value) in params!!) {
                if (encodedParams.length != 0) {
                    encodedParams.append('&')
                }
                encodedParams.append(URLEncoder.encode(key, paramsEncoding))
                encodedParams.append('=')
                encodedParams.append(URLEncoder.encode(value, paramsEncoding))
            }
            return encodedParams.toString()
        } catch (uee: UnsupportedEncodingException) {
            throw RuntimeException("Encoding not supported: " + paramsEncoding, uee)
        }

    }

    companion object {

        protected val DEFAULT_CHARSET = "utf-8"

        /**
         * Handles logging the error, both in logcat and by sending to Analytics
         *
         * @param request The class that forms the request
         * @param error   The error returned by the [com.android.volley.Response.ErrorListener]
         */
        private fun logError(request: Class<*>, error: VolleyError?) {
            val sb = StringBuilder(request.simpleName + " -- ")
            if (error == null) {
                sb.append("Null response error")
            } else if (error.networkResponse == null) {
                sb.append(error.localizedMessage)
            } else if (error.networkResponse.data == null || error.networkResponse.data.size == 0) {
                sb.append(error.localizedMessage)
            } else {
                for (b in error.networkResponse.data) {
                    sb.append(b.toChar())
                }
                Log.w("VOLLEY ERROR: ", sb.toString())
            }
        }
    }


}
