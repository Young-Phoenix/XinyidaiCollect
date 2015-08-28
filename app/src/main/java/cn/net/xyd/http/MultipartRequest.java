package cn.net.xyd.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import collect.xyd.net.cn.xinyidaicollect.utils.L;


/**
 * Created by Administrator on 2015/7/21 0021.
 */
public class MultipartRequest extends Request{

    private Response.Listener mListener;
    private Response.ErrorListener mErrorListener;
    private HttpEntity mEntity;

    public MultipartRequest(String url,Map<String, String> textBodyParamMap, Map<String, File> fileBodyParamMap, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, null);
        mListener = listener;
        mErrorListener = errorListener;
        Charset charset = Charset.forName("UTF-8");
        ContentType contentTypeText = ContentType.create("text/plain", charset);
        ContentType contentTypeFile = ContentType.create("multipart/form-data", charset);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE).setCharset(charset);
        for (Map.Entry<String, String> entry : textBodyParamMap.entrySet()) {
            builder.addTextBody(entry.getKey(), entry.getValue(),contentTypeText);
        }
        for (Map.Entry<String, File> entry : fileBodyParamMap.entrySet()) {
            builder.addBinaryBody(entry.getKey(), entry.getValue(),contentTypeFile,entry.getValue().getName());
        }
        mEntity = builder.build();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(HTTP.CHARSET_PARAM, HTTP.UTF_8);
        return headers;
    }

    @Override
    public String getBodyContentType() {
        return mEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            mEntity.writeTo(baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        JSONObject json = null;
        try {
            L.d(new String(response.data));
            json = new JSONObject(new String(response.data, HttpHeaderParser.parseCharset(response.headers)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(response));
        } catch (JSONException e) {
            e.printStackTrace();
            return Response.error(new ParseError(response));
        }
        return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(Object response)  {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {

        mErrorListener.onErrorResponse(error);
    }
}
