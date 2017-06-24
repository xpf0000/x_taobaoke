package util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.ali.auth.third.core.MemberSDK;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.AlibcUrlCenter;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.alibaba.baichuan.trade.common.adapter.ut.AlibcUserTracker;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.robin.lazy.cache.CacheLoaderConfiguration;
import com.robin.lazy.cache.CacheLoaderManager;
import com.robin.lazy.cache.disk.naming.HashCodeFileNameGenerator;
import com.robin.lazy.cache.memory.MemoryCache;
import com.robin.lazy.cache.util.MemoryCacheUtils;
import com.ut.mini.internal.UTTeamWork;
import com.yibaitaoke.x.taobaoke.R;


import net.ServicesAPI;
import net.XNetUtil;

//import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import httpUtil.converter.CustomGsonConverterFactory;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApplicationClass extends MultiDexApplication {

	public static int SW = 0;
	public static int SH = 0;

	public static Context context;

	public static Retrofit retrofit;

	public static ServicesAPI APPService;

	private List<WeakReference<Activity>> vcArrs = new ArrayList<>();

	public static OkHttpClient okHttpClient;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();

		MemoryCache memoryCache= MemoryCacheUtils.createLruMemoryCache(1024*1024*256);
		CacheLoaderConfiguration config = new CacheLoaderConfiguration(this, new HashCodeFileNameGenerator(), 1024 * 1024 * 1024*64, 200000, memoryCache,60*24*30*365*20);
		CacheLoaderManager.getInstance().init(config);

		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
				XNetUtil.APPPrintln("onActivityCreated: "+activity);
				context = activity;
			}

			@Override
			public void onActivityStarted(Activity activity) {

			}

			@Override
			public void onActivityResumed(Activity activity) {
				context = activity;
				WeakReference<Activity> item = new WeakReference<Activity>(activity);
				vcArrs.add(item);
			}

			@Override
			public void onActivityPaused(Activity activity) {

			}

			@Override
			public void onActivityStopped(Activity activity) {

			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

			}

			@Override
			public void onActivityDestroyed(Activity activity) {

			}

		});

		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		SW = displayMetrics.widthPixels;
		SH = displayMetrics.heightPixels;

		okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {

				Request request = chain.request().newBuilder()
						.addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
						.addHeader("Accept","*/*")
						.build();

				XNetUtil.APPPrintln("URL: "+request.url().toString());
				if(request.body() != null)
				{
					XNetUtil.APPPrintln("Body: "+request.body().toString());
				}

				Response response = chain.proceed(request);

				return response;
			}
		}).cookieJar(new CookieJar() {

			private final List<Cookie> cookies = new ArrayList<Cookie>();

			@Override
			public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

			}

			@Override
			public List<Cookie> loadForRequest(HttpUrl url) {

//				if(cookies.size() == 0 && DataCache.getInstance().user != null)
//				{
//					Cookie.Builder builder = new Cookie.Builder();
//					builder.name("PHPSESSID");
//					builder.value(DataCache.getInstance().user.getSess_id());
//					builder.path("/");
//					builder.httpOnly();
//					builder.domain(url.host());
//					Cookie cookie = builder.build();
//
//					cookies.add(cookie);
//				}


				return cookies;
			}
		})
				.build();


//		retrofit = new Retrofit.Builder()
//				.baseUrl(ServicesAPI.APPUrl)
//				.addConverterFactory(CustomGsonConverterFactory.create())
//				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//				.callFactory(okHttpClient)
//				.build();
//
//		APPService = retrofit.create(ServicesAPI.class);
		initImageLoader();
		DataCache.getInstance().init();

		MemberSDK.turnOnDebug();
		//电商SDK初始化
		AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
			@Override
			public void onSuccess() {

				XNetUtil.APPPrintln("电商初始化成功 !!!!!!!!!!!!");

				Toast.makeText(context, "初始化成功", Toast.LENGTH_SHORT).show();

				Map utMap = new HashMap<>();
				utMap.put("debug_api_url","http://muvp.alibaba-inc.com/online/UploadRecords.do");
				utMap.put("debug_key","baichuan_sdk_utDetection");
				UTTeamWork.getInstance().turnOnRealTimeDebug(utMap);

				AlibcUserTracker.getInstance().sendInitHit4DAU("19","3.1.1.100");

			}

			@Override
			public void onFailure(int code, String msg) {
				XNetUtil.APPPrintln("初始化失败,错误码="+code+" / 错误消息="+msg);
				Toast.makeText(context, "初始化失败,错误码="+code+" / 错误消息="+msg, Toast.LENGTH_SHORT).show();
			}
		});



		System.out.println("================init============");
	}


	//初始化网络图片缓存库
	private void initImageLoader() {
		//网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
				showImageForEmptyUri(R.mipmap.default_pictures)
				.showImageOnFail(R.mipmap.default_pictures)
				.cacheInMemory(true).cacheOnDisk(true).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);

	}


}
