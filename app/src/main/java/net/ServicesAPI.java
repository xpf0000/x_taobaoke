package net;

import com.yibaitaoke.x.model.TKModel;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by X on 2016/10/1.
 */

public interface ServicesAPI {

 public  static  String APPUrl = "http://192.168.0.110/tbk/";

 //获取淘客商品链接url
 @POST("index.php")
 Observable<String> tk_list_str(
         @Query("name") String name,
         @Query("sort") String sort,
         @Query("page_no") int page_no,
         @Query("page_size") int page_size
 );

 //获取淘客商品列表
 @GET("rest?{query}")
 Observable<TKModel> tk_list(@Path("query") String query);


 //发送验证码
 @GET("?ctl=sms&act=send_sms_code&r_type=1&isapp=true&unique=0")
 Observable<HttpResult<Object>> sms_send_code(@Query("mobile") String mobile);


}


