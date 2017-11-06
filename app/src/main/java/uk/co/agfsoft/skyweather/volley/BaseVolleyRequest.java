package uk.co.agfsoft.skyweather.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import uk.co.agfsoft.skyweather.BuildConfig;
import uk.co.agfsoft.skyweather.R;

/**
 * Base class for all volley requests
 * <p/>
 * Provides an interface to the volley library functions to simplify and abstract
 * clients from the underlying complexities.
 * <p/>
 * This implementation creates by default a request with the following characteristics:
 * - HTTP GET method
 * - Does NOT cache responses (call setShouldCache(true) in the subclass constructor to enable caching
 * <p/>
 * Created by drew on 22/01/2016.
 */
public abstract class BaseVolleyRequest<T> {

    protected static final String DEFAULT_CHARSET = "utf-8";
    protected boolean isUIBlocking;
    protected String url;
    private long startTs;

    @Inject
    protected RequestQueue requestQueue;
    @Inject
    Gson gson;


    public BaseVolleyRequest() {
    }

    protected abstract String getUrl();

    protected abstract int getMethod();

    protected abstract Class getResponseClass();

    /**
     * Call this method to actually submit the request once it has been properly initialised
     *
     * @param context  This is used as a tag.  All requests with a given tag can be cancelled
     *                 by calling RequestQueue.cancelAll(Object tag)
     * @param listener - the listener for notifying the outcome to
     */
    public void submit(Context context, final boolean isUIBlocking, final Listener<T> listener) {

        if (BuildConfig.DEBUG) {
            StringBuilder sb = new StringBuilder("Network request: ");
            sb.append(methodToString(getMethod()) + ": " + buildUrl());
            String tag = this.getClass().getSimpleName();
            Log.i(tag, sb.toString());
            startTs = System.currentTimeMillis();
        }

        Request<T> request = new Request<T>(getMethod(), buildUrl(), new ErrorListener<>(isUIBlocking)) {
            @Override
            protected Response<T> parseNetworkResponse(NetworkResponse response) {
                try {
                    if (BuildConfig.DEBUG) {

                        Log.i(BaseVolleyRequest.this.getClass().getSimpleName(),
                                "Network success: " +
                                        String.valueOf(response.data.length) + " bytes, " +
                                        "elapsed: " + String.valueOf(System.currentTimeMillis() - startTs) + "mS, " +
                                        "  data: " + new String(response.data, 0, Math.min(40, response.data.length)) +
                                        ((response.data.length > 40) ? "..." : ""));
                    }
                    return parseResponse(response);
                } catch (JsonSyntaxException e) {
                    Response.error(new VolleyError("Unable to parse " + getResponseClass().getSimpleName() + " when processing network response"));
                    return null;
                } catch (UnsupportedEncodingException e) {
                    Response.error(new VolleyError("Unsupported character set when processing network response for " + getResponseClass().getSimpleName()));
                    return null;
                }
            }

            /**
             * Called by Volley to deliver the response.
             * <p/>
             * Simply check whether a listener has been set for
             * the class and if so notify by calling the onSuccess method.
             *
             * @param response
             */
            @Override
            protected void deliverResponse(T response) {
                if (listener != null) {
                    listener.onSuccess(response);
                }
            }

            /**
             * Called by Volley to deliver an error response
             * <p/>
             * Simply check whether a listener has been set for
             * the class and if so notify by calling the onError method.
             *
             * @param error
             */
            @Override
            public void deliverError(VolleyError error) {
                if (BuildConfig.DEBUG) {
                    String detail = (error instanceof ServerError) ? new String(error.networkResponse.data) : "Networking error";
                    Log.e(BaseVolleyRequest.this.getClass().getSimpleName(),
                            "Network ERROR: " + error.toString() +
                                    "  detail: " + detail +
                                    "  elapsed: " + String.valueOf(System.currentTimeMillis() - startTs) + "mS");
                }

                if (listener != null) {
                    listener.onError(error);
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return BaseVolleyRequest.this.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return BaseVolleyRequest.this.getParams();
            }

            @Override
            public String getBodyContentType() {
                return BaseVolleyRequest.this.getBodyContentType();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return BaseVolleyRequest.this.getBody();
            }
        };


        ((ErrorListener<T>) request.getErrorListener()).setListener(listener);
        request.setTag(context);
        requestQueue.add(request);
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
    protected Response<T> parseResponse(NetworkResponse response) throws JsonSyntaxException, UnsupportedEncodingException {
        String jsonString = new String(response.data,
                HttpHeaderParser.parseCharset(response.headers, DEFAULT_CHARSET));

        Object result = gson.fromJson(jsonString, getResponseClass());
        return (Response<T>) Response.success(result, null);
    }


    /**
     * Provide headers for the request.
     * <p/>
     * Subclass requests that require additional custom headers should override this method and add
     * their own headers to the returned Map.
     * Each map entry consists of the header name (the key) and the header value.
     * Volley uses the map contents to build the request headers.
     * Note that the super class headers must also be included
     *
     * @return Map containing headers for request
     * @throws AuthFailureError
     */
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        return headers;
    }

    /**
     * Provide parameters for the request
     * <p/>
     * Subclass requests that require additional parameters should override this method and add
     * their own parameters to the returned Map.
     * Each map entry consists of the parameter name (the key) and the parameter value.
     * Volley uses the map contents to build the query string for the request.
     * Note that the super class parameters must also be included
     *
     * @return Map containing parameters for the query string
     * @throws AuthFailureError
     */
    protected Map<String, String> getQueryParams() {
        return null;
    }

    /**
     * Provide post/put parameters for the request
     * <p/>
     * Subclass requests that require additional parameters should override this method and add
     * their own parameters to the returned Map.
     * Each map entry consists of the parameter name (the key) and the parameter value.
     * Volley uses the map contents to build the a form data body.  Typically we use json bodies so
     * this is unlikely to be used
     *
     * @return Map containing parameters for the query string
     * @throws AuthFailureError
     */
    protected Map<String, String> getParams() throws AuthFailureError {
        return null;
    }

    /**
     * Provide body content type for the request
     * <p/>
     * Override this method if you wish to send another type of content in the body.
     * Volley uses this to set the content type header in the request.
     *
     * @return
     */
    public String getBodyContentType() {
        return "application/text";
    }

    /**
     * Provide the boidy for the request
     * </p>
     * Override this method to create the body content as a byte array.  The format will be dependent
     * on the body content type for the request.
     *
     * @return
     */
    public byte[] getBody() throws AuthFailureError {
        return null;
    }

    /**
     * Listener interface for notifying the outcome of the volley request.
     *
     * @param <T> - the type of the response returned by the class.
     */
    public interface Listener<T> {
        void onSuccess(T response);

        void onError(VolleyError error);
    }

    /**
     * Provide an appropriate string resource ID for the error and log the error if not a connection
     * issue.
     *
     * @param error - the VolleyError
     * @return
     */
    public int handleError(VolleyError error) {
        if (error instanceof NoConnectionError || error instanceof TimeoutError) {
            return R.string.error_generic_connection;
        } else {
            logError(this.getClass(), error);
            return R.string.error_generic_unknown;
        }
    }

    /**
     * Handles logging the error, both in logcat and by sending to Analytics
     *
     * @param request The class that forms the request
     * @param error   The error returned by the {@link com.android.volley.Response.ErrorListener}
     */
    private static void logError(Class request, VolleyError error) {
        StringBuilder sb = new StringBuilder(request.getSimpleName() + " -- ");
        if (error == null) {
            sb.append("Null response error");
        } else if (error.networkResponse == null) {
            sb.append(error.getLocalizedMessage());
        } else if (error.networkResponse.data == null || error.networkResponse.data.length == 0) {
            sb.append(error.getLocalizedMessage());
        } else {
            for (byte b : error.networkResponse.data) {
                sb.append(((char) b));
            }
            Log.w("VOLLEY ERROR: ", sb.toString());
        }
    }


    /**
     * Volley error listener
     * <p/>
     * This class is passed by Volley and the onErrorResponse method is called by Volley in the
     * event of an error occurring.
     * <p/>
     * Simply check whether to hide the progress dialog and if a listener has been set then
     * call the onError method
     *
     * @param <T>
     */
    public static class ErrorListener<T> implements Response.ErrorListener {

        private boolean isUIBlocking;
        private Listener<T> listener;

        protected ErrorListener(boolean isUIBlocking) {
            this.isUIBlocking = isUIBlocking;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            if (listener != null) {
                listener.onError(error);
            }

        }

        public void setListener(Listener<T> listener) {
            this.listener = listener;
        }
    }

    //</editor-fold>

    private String methodToString(int method) {
        switch (method) {
            case -1:
                return "DEPRECATED_GET_OR_POST";
            case 0:
                return "GET";
            case 1:
                return "POST";
            case 2:
                return "PUT";
            case 3:
                return "DELETE";
            case 4:
                return "HEAD";
            case 5:
                return "OPTIONS";
            case 6:
                return "TRACE";
            case 7:
                return "PATCH";
            default:
                return "UNKNOWN";
        }
    }

    private String buildUrl() {
        StringBuilder sb = new StringBuilder(getUrl());
        if (getQueryParams() != null) {
            sb.append("?");
            sb.append(encodeParameters(getQueryParams(), "UTF-8"));
        }
        return sb.toString();
    }

    private String encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (encodedParams.length() != 0) {
                    encodedParams.append('&');
                }
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
            }
            return encodedParams.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }


}
