package util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

//import com.jph.takephoto.app.TakePhotoActivity;
import com.yibaitaoke.x.taobaoke.R;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import httpUtil.rxjava.ActivityLifecycle;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public abstract class BaseActivity extends Activity{

    protected Context mContext;
    public boolean isPush = true;


    @Override
    protected void onDestroy() {
        //unsubscribe网络请求
        Iterator<SubscriberWrapper> it = subscribers.iterator();
        while (it.hasNext()) {
            SubscriberWrapper subscriberWrapper = it.next();
            if (subscriberWrapper.unsubscribeOn == ActivityLifecycle.OnDestroy) {
                Log.e("rxjava", "onDestroy==============>");
                subscriberWrapper.subscriber.unsubscribe();
                it.remove();
            }
        }
        super.onDestroy();
    }

    private List<SubscriberWrapper> subscribers = new LinkedList<>();
    public void addSubscriber(Subscriber subscriber, ActivityLifecycle unsubscribeOn) {
        subscribers.add(new SubscriberWrapper(subscriber, unsubscribeOn));
    }

    private class SubscriberWrapper {
        Subscriber subscriber;
        ActivityLifecycle unsubscribeOn;

        public SubscriberWrapper(Subscriber subscriber, ActivityLifecycle unsubscribeOn) {
            this.subscriber = subscriber;
            this.unsubscribeOn = unsubscribeOn;
        }
    }


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mContext = this;
        setupUi();
        setupData();
        //新页面接收数据
        isPush = getIntent().getBooleanExtra("isPush",true);

    }

    /**
     * 初始化ui
     */
    protected abstract void setupUi();

    /**
     *
     */
    protected abstract void setupData();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    /**
     * 启动另外一个界面通过push动画
     *
     * @param activity
     */
    public void pushVC(Class activity) {

        if(!CountTime.isBeyoundTime("启动界面", 300)){
            return;
        }

        Intent intentActive = new Intent(this, activity);

        //用Bundle携带数据
        Bundle bundle=new Bundle();
        bundle.putBoolean("isPush", true);
        intentActive.putExtras(bundle);

        startActivity(intentActive);

    }


    /**
     * 启动另外一个界面通过push动画
     *
     * @param activity
     * @param bundle
     */
    public void pushVC(Class activity, Bundle bundle) {

        if(!CountTime.isBeyoundTime("启动界面", 300)){
            return;
        }

        Intent intentActive = new Intent(this, activity);
        //用Bundle携带数据
        bundle.putBoolean("isPush", true);
        intentActive.putExtras(bundle);

        startActivity(intentActive);

    }



    /**
     * 启动另外一个界面通过present动画
     *
     * @param activity
     */
    public void presentVC(Class activity) {

        if(!CountTime.isBeyoundTime("启动界面", 300)){
            return;
        }

        Intent intentActive = new Intent(this, activity);
        //用Bundle携带数据
        Bundle bundle=new Bundle();
        bundle.putBoolean("isPush", false);
        intentActive.putExtras(bundle);

        startActivity(intentActive);


    }

    /**
     * 启动另外一个界面通过present动画
     *
     * @param activity
     * @param bundle
     */
    public void presentVC(Class activity, Bundle bundle) {

        if(!CountTime.isBeyoundTime("启动界面", 300)){
            return;
        }

        Intent intentActive = new Intent(this, activity);
        //用Bundle携带数据
        bundle.putBoolean("isPush", false);
        intentActive.putExtras(bundle);

        startActivity(intentActive);


    }

    public void back()
    {
        doPop();
    }

    public void back(View v)
    {
        doPop();
    }

    public void doPop()
    {
        this.finish();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        doPop();
    }



    public boolean checkIsLogin()
    {

//        if(DataCache.getInstance().user == null)
//        {
//            presentVC(LoginActivity.class);
//            return false;
//        }
//        else
//        {
//            if(DataCache.getInstance().user.getIs_effect() != 1)
//            {
//                presentVC(UserRenzhengVC.class);
//                return false;
//            }
//        }

        return true;
    }


}
