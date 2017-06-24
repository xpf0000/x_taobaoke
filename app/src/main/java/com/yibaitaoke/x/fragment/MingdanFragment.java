package com.yibaitaoke.x.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.robin.lazy.cache.CacheLoaderManager;
import com.yibaitaoke.x.model.TbkItemModel;
import com.yibaitaoke.x.model.UserModel;
import com.yibaitaoke.x.taobaoke.HomePageActivity;
import com.yibaitaoke.x.taobaoke.R;
import com.yibaitaoke.x.taobaoke.SetPidVC;

import net.XAPPUtil;
import net.XActivityindicator;
import net.XNetUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import util.DataCache;

/**
 * Created by Administrator on 2016/5/21.
 */
public class MingdanFragment extends Fragment implements View.OnClickListener
{
    private View view;
    RoundedImageView header;
    TextView name,pidTV;
    LinearLayout logout,orderLL,pidLL;

    @Override
    public void onResume() {
        super.onResume();

        XNetUtil.APPPrintln("MingdanFragment is onResume !!!!!");

        initUi();

    }

    private void initUi()
    {
        if(AlibcLogin.getInstance().isLogin())
        {
            logout.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(AlibcLogin.getInstance().getSession().avatarUrl,header);
            name.setText(AlibcLogin.getInstance().getSession().nick);

            String openid = AlibcLogin.getInstance().getSession().openId;
            UserModel m = DataCache.getInstance().users.get(openid);

            pidTV.setText(m.getPid());
        }
        else
        {
            logout.setVisibility(View.GONE);
            name.setText("点击登录");
            header.setImageResource(R.mipmap.home_head);
            pidTV.setText("");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        view = inflater.inflate(R.layout.fragment_mine, container, false);
        header = (RoundedImageView) view.findViewById(R.id.mine_head);
        name = (TextView) view.findViewById(R.id.mine_name);
        pidTV = (TextView) view.findViewById(R.id.mine_pid_txt);

        logout = (LinearLayout) view.findViewById(R.id.mine_logout);
        orderLL = (LinearLayout) view.findViewById(R.id.mine_order);
        pidLL = (LinearLayout) view.findViewById(R.id.mine_pid);

        header.setOnClickListener(this);
        name.setOnClickListener(this);

        logout.setOnClickListener(this);
        orderLL.setOnClickListener(this);
        pidLL.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.mine_head || v.getId() == R.id.mine_name)
        {
            do_login();
        }
        else if(v.getId() == R.id.mine_logout)
        {
            AlertView alert = new AlertView("注销登录", "确定要登出账户吗?", null, null,
                    new String[]{"取消", "确定"},
                    getActivity(), AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {

                    if(position == 1)
                    {
                        do_logout();
                    }

                }
            });

            alert.show();
        }
        else if(v.getId() == R.id.mine_order)
        {
            if(!AlibcLogin.getInstance().isLogin())
            {
                do_login();
                return;
            }

            Map<String, String> exParams = new HashMap<>();
            exParams.put(AlibcConstants.ISV_CODE, "appisvcode");

            AlibcMyOrdersPage page = new AlibcMyOrdersPage(0,true);
//设置页面打开方式
            AlibcShowParams showParams = new AlibcShowParams(OpenType.Native, false);

            String openid = AlibcLogin.getInstance().getSession().openId;
            UserModel m = DataCache.getInstance().users.get(openid);

            String[] pids = m.getPid().replace("mm_","").split("_");

            XNetUtil.APPPrintln(pids[0]+" | "+pids[1]+" | "+pids[2]);

            AlibcTaokeParams taokeParams = new AlibcTaokeParams(pids[0],pids[1],pids[2]);
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
        else if(v.getId() == R.id.mine_pid)
        {
            if(!AlibcLogin.getInstance().isLogin())
            {
                do_login();
                return;
            }

            Intent intent = new Intent(getActivity(), SetPidVC.class);
            getActivity().startActivity(intent);


        }

    }



    public void do_login()
    {
        if(AlibcLogin.getInstance().isLogin())
        {
            return;
        }

        AlibcLogin alibcLogin = AlibcLogin.getInstance();


        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                Toast.makeText(getActivity(), "登录成功 ",
                        Toast.LENGTH_LONG).show();

                XNetUtil.APPPrintln(AlibcLogin.getInstance().getSession().toString());
                UserModel u = DataCache.getInstance().users.get(AlibcLogin.getInstance().getSession().openId);

                if(u == null)
                {
                    u = new UserModel();
                    u.setOpenId(AlibcLogin.getInstance().getSession().openId);

                    DataCache.getInstance().users.put(AlibcLogin.getInstance().getSession().openId,u);
                }

                XAPPUtil.saveAPPCache("Users",DataCache.getInstance().users);

                initUi();

            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(getActivity(), "登录失败 ",
                        Toast.LENGTH_LONG).show();


            }
        });

    }

    public void do_logout()
    {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();

        alibcLogin.logout(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int i) {
                Toast.makeText(getActivity(), "登出成功 ",
                        Toast.LENGTH_LONG).show();

                initUi();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getActivity(), "登出失败 ",
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        CallbackContext.onActivityResult(requestCode, resultCode, data);
    }
}
