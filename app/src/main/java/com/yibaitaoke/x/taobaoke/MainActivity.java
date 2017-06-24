package com.yibaitaoke.x.taobaoke;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

//import com.gargoylesoftware.htmlunit.BrowserVersion;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import com.gargoylesoftware.htmlunit.javascript.HtmlUnitContextFactory;

import com.google.gson.Gson;
import com.yibaitaoke.x.adapter.OrderListAdapter;
import com.yibaitaoke.x.model.TKModel;
import com.yibaitaoke.x.model.TbkItemModel;

import net.HttpResult;
import net.ServicesAPI;
import net.XActivityindicator;
import net.XNetUtil;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import httpUtil.ServiceFactoty;
import httpUtil.rxjava.ApplySchedulers;
import httpUtil.rxjava.BaseSubscriber;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import util.ApplicationClass;
import util.BaseActivity;

import static net.ServicesAPI.APPUrl;
import static util.ApplicationClass.APPService;
import static util.ApplicationClass.okHttpClient;


public class MainActivity extends BaseActivity {

    List<TbkItemModel> list = new ArrayList<>();

    SwipeRefreshLayout swipe_refresh;
    ListView mainList;

    OrderListAdapter adapter;

    int page = 1;
    boolean end = false;
    boolean running = false;

    @Override
    protected void setupUi() {
        setContentView(R.layout.activity_main);

        swipe_refresh = (SwipeRefreshLayout)findViewById(R.id.order_list_refresh);
        mainList = (ListView) findViewById(R.id.order_list_listview);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                end = false;
                getData();
            }
        });

        mainList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            if(!end)
                            {
                                getData();
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //to_info(position);

            }
        });

        adapter = new OrderListAdapter(this,list);

        mainList.setAdapter(adapter);

        getData();

    }

    @Override
    protected void setupData() {

    }


    private void getData()
    {
        if (running) {return;}
        running = true;

        ServiceFactoty.getService(ServicesAPI.class,APPUrl).tk_list_str("","total_sales_asc",page,10)
                .compose(new ApplySchedulers())
                .subscribe(new BaseSubscriber<String>(){
                               @Override
                               public void onNext(String s) {

                                   XNetUtil.APPPrintln(s);
                                   getlist(s);

                               }

                               @Override
                               public void onError(Throwable throwable) {
                                   super.onError(throwable);
                                   swipe_refresh.setRefreshing(false);
                                    running = false;
                               }
                           }
                );


    }


    private void getlist(String url)
    {

        Request.Builder requestBuilder = new Request.Builder().url(url);
        Request request = requestBuilder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                swipe_refresh.setRefreshing(false);
                XNetUtil.APPPrintln(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Gson gson = new Gson();

                try
                {
                    TKModel model = gson.fromJson(response.body().charStream(),TKModel.class);

                    List<TbkItemModel> arr = model.getTbk_item_get_response().getResults().getN_tbk_item();

                    XNetUtil.APPPrintln("size: "+arr.size());

                    if(page == 1)
                    {
                        list.clear();
                    }

                    if(arr.size() > 0)
                    {
                        page++;
                        list.addAll(arr);
                    }
                    else
                    {
                        end = true;
                        XActivityindicator.showToast("已全部加载完毕！");
                    }

                    if(adapter != null)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setList(list);
                            }
                        });

                    }


                }
                catch (Exception e)
                {
                    XNetUtil.APPPrintln(e.getMessage());
                }
                swipe_refresh.setRefreshing(false);
                running = false;

            }
        });



    }


}
