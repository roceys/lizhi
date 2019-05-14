package ren.perry.mvplibrary.net;

import android.util.Log;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;


/**
 * ApiException
 *
 * @author perry
 */
public class ApiException {
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ResponseException handleException(Throwable e) {
        ResponseException ex;
        Log.e("handleException:", e.getClass().getSimpleName() + "," + e.toString());
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseException(e, ErrorCode.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex.message = "服务器竟然出错了";
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponseException(resultException, resultException.code);
            ex.message = resultException.msg;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof MalformedJsonException
            /*|| e instanceof ParseException*/) {
            ex = new ResponseException(e, ErrorCode.PARSE_ERROR);
            ex.message = "居然解析错误了";
            return ex;
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
            ex = new ResponseException(e, ErrorCode.NETWORK_ERROR);
            ex.message = "啊噢~连接失败了诶";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponseException(e, ErrorCode.SSL_ERROR);
            ex.message = "证书验证居然失败了";
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new ResponseException(e, ErrorCode.TIME_OUT);
            ex.message = "啊噢~连接超时了诶";
            return ex;
        } else if (e instanceof SocketException) {
            ex = new ResponseException(e, ErrorCode.NETWORK_ERROR);
            ex.message = "啊噢~连接失败了诶";
            return ex;
        } else {
            ex = new ResponseException(e, ErrorCode.UNKNOWN);
            ex.message = "我也不知道是什么错误";
            return ex;
        }
    }


    /**
     * 约定异常
     */
    @SuppressWarnings("WeakerAccess")
    public class ErrorCode {

        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORK_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 连接超时
         */
        public static final int TIME_OUT = 1004;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

    }

    public static class ResponseException extends Exception {
        public int code;
        public String message;

        public ResponseException(Throwable throwable, int code) {
            super(throwable);
            this.code = code;
        }
    }

    /**
     * ServerException发生后，将自动转换为ResponseThrowable返回
     */
    private class ServerException extends RuntimeException {
        int code;
        String msg;
    }

}