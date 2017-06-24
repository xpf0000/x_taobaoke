package com.yibaitaoke.x.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.google.gson.Gson;
import com.yibaitaoke.x.adapter.OrderListAdapter;
import com.yibaitaoke.x.model.TKModel;
import com.yibaitaoke.x.model.TbkItemModel;
import com.yibaitaoke.x.model.UserModel;
import com.yibaitaoke.x.taobaoke.HomePageActivity;
import com.yibaitaoke.x.taobaoke.MainActivity;
import com.yibaitaoke.x.taobaoke.R;

import net.XActivityindicator;
import net.XNetUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;
import util.DataCache;
import util.StringUtils;
import util.TaobaoUtils;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static util.ApplicationClass.okHttpClient;


/**
 * 首页展示
 *
 * @author zpp
 * @date 201501204
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    View view;
    ImageView menu;
    EditText searchET;

    List<TbkItemModel> list = new ArrayList<>();

    SwipeRefreshLayout swipe_refresh;
    ListView mainList;

    OrderListAdapter adapter;

    public String searchkey = "";

    int page = 1;
    boolean end = false;
    boolean running = false;

    public void reset()
    {
        page = 1;
        end = false;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        menu = (ImageView) view.findViewById(R.id.home_menu);
        menu.setOnClickListener(this);

        searchET = (EditText) view.findViewById(R.id.search);

        searchET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == event.KEYCODE_ENTER) {

                    ((InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getActivity().getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    String key = searchET.getText().toString().trim();

                    if(!key.equals(searchkey) && key.length() > 0)
                    {
                        searchkey = key;
                        page = 1;
                        end = false;
                        getData();
                    }

                }

                return false;
            }
        });

        swipe_refresh = (SwipeRefreshLayout)view.findViewById(R.id.order_list_refresh);
        mainList = (ListView) view.findViewById(R.id.order_list_listview);

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

                to_info(position);

            }
        });

        adapter = new OrderListAdapter(getActivity(),list);

        mainList.setAdapter(adapter);

        getData();

        return view;
    }

    private void to_info(int position)
    {
        TbkItemModel model = list.get(position);

        Map<String, String> exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");

        AlibcPage page = new AlibcPage(model.getItem_url());
//设置页面打开方式
        AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);

        AlibcTaokeParams taokeParams = null;

        if(AlibcLogin.getInstance().isLogin())
        {
            String openid = AlibcLogin.getInstance().getSession().openId;
            UserModel m = DataCache.getInstance().users.get(openid);

            if(m.getPid().length() > 0)
            {
                String[] pids = m.getPid().replace("mm_","").split("_");

                XNetUtil.APPPrintln(pids[0]+" | "+pids[1]+" | "+pids[2]);

                taokeParams = new AlibcTaokeParams(pids[0],pids[1],pids[2]);
            }


        }

        /**
         * 打开电商组件, 使用默认的webview打开
         *
         * @param activity             必填
         * @param tradePage            页面类型,必填，不可为null，详情见下面tradePage类型介绍
         * @param showParams           show参数
         * @param taokeParams          淘客参数
         * @param trackParam           yhhpass参数
         * @param tradeProcessCallback 交易流程的回调，必填，不允许为null；
         * @return 0标识跳转到手淘打开了,1标识用h5打开,-1标识出错
         */
        AlibcTrade.show(getActivity(), page, showParams, taokeParams, null, new AlibcTradeCallback() {
            @Override
            public void onTradeSuccess(AlibcTradeResult alibcTradeResult) {


            }

            @Override
            public void onFailure(int i, String s) {

                XActivityindicator.showToast(s);
                XNetUtil.APPPrintln(s);
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.home_menu)
        {
            ((HomePageActivity)getActivity()).toggleRightSliding();
        }

    }


    public void getData()
    {

        Map<String,String> map = new HashMap<>();

        map.put("method","taobao.tbk.item.get");
        map.put("app_key","23719215");
        map.put("sign_method","md5");
        map.put("timestamp",(StringUtils.formatDateTime(new Date(System.currentTimeMillis()))));
        map.put("format","json");
        map.put("v","2.0");


        //map.put("adzone_id","73840212");
        map.put("fields","num_iid,title,pict_url,reserve_price,zk_final_price,user_type,provcity,item_url,seller_id,volume,nick");
        //map.put("cat","50010850");

        map.put("page_no",((Integer)page).toString());
        map.put("page_size","10");

        if(searchkey.length() > 0)
        {
            map.put("q",searchkey);
        }
        else
        {
            map.put("cat","50010850,1101,121454013,50013618,50008055,50014075,50016434,50011975");
        }


        HomePageActivity home = (HomePageActivity)getActivity();

        if(home.tianmaoCB.isChecked() && !home.taobaoCB.isChecked())
        {
            map.put("is_tmall","true");
        }

        if(home.order.length() > 0)
        {
            map.put("sort",home.order);
        }

        String sp = home.minpET.getText().toString().trim();
        if(sp.length() > 0)
        {
            map.put("start_price",sp);
        }

        String ep = home.maxpET.getText().toString().trim();
        if(ep.length() > 0)
        {
            map.put("end_price",ep);
        }


        String sy = home.minyET.getText().toString().trim();
        if(sy.length() > 0)
        {
            map.put("start_tk_rate",sy);
        }


        String ey = home.maxyET.getText().toString().trim();
        if(ey.length() > 0)
        {
            map.put("end_tk_rate",ey);
        }




        try {
            String sign = TaobaoUtils.signTopRequest(map,null,"05819a9702a54170f8fff091b261e305");
            map.put("sign",sign);

            FormBody.Builder formBodyBuilder = new FormBody.Builder();

            Set<Map.Entry<String, String>> entrySet = map.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }

            Request.Builder requestBuilder = new Request.Builder()
                    .url("http://gw.api.taobao.com/router/rest")
                    .post(formBodyBuilder.build())
                    ;

            Request request = requestBuilder.build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipe_refresh.setRefreshing(false);
                        }
                    });
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
                            getActivity().runOnUiThread(new Runnable() {
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipe_refresh.setRefreshing(false);
                        }
                    });

                    running = false;

                }
            });




        } catch (IOException e) {
            e.printStackTrace();
        }






    }

}
