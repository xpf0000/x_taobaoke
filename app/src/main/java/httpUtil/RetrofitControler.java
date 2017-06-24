package httpUtil;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @anthor: gaotengfei
 * @time: 2016/12/28
 * @tel: 18511913443
 * @desc:
 */

public interface RetrofitControler {
    /**
     * 获取验证码.
     *
     * @param mobile
     * @return
     */
    @POST("v1/mobiles/{mobile}/captcha")
    Observable<String> getCaptcha(@Path("mobile") String mobile);

    //商户提现提交
    @POST("index.php")
    Observable<String> tk_list_str(
            @Query("name") String name,
            @Query("sort") String sort,
            @Query("page_no") int page_no,
            @Query("page_size") int page_size
    );

}
