package httpUtil;


import android.util.Log;

import httpUtil.converter.CustomGsonConverterFactory;
import httpUtil.interceptor.TokenInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

import static util.ApplicationClass.okHttpClient;


/**
 * 服务端接口工厂类.
 */
public class ServiceFactoty {
    public static final String Tag = "retrofitLog";
    public static <T> T getService(Class<T> clazz,String baseurl){
        Retrofit retrofit= createRetrofitService(baseurl);
        return retrofit.create(clazz);
    }
    /**
     * Creates a retrofit service from an arbitrary class (clazz)
     *
     */
    public static Retrofit createRetrofitService(String baseurl) {
        /**
         * 拦截一个请求使用OkHttp里面的Interceptor
         */

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(CustomGsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }
}
