package com.yibaitaoke.x.taobaoke;

import android.view.View;
import android.widget.EditText;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.yibaitaoke.x.model.UserModel;

import net.XAPPUtil;
import net.XActivityindicator;

import util.BaseActivity;
import util.DataCache;

/**
 * Created by x on 2017/6/24.
 */

public class SetPidVC extends BaseActivity {


    EditText pidET;

    @Override
    protected void setupUi() {
        setContentView(R.layout.set_pid);

        pidET = (EditText) findViewById(R.id.set_pid_pid);

        String openid = AlibcLogin.getInstance().getSession().openId;
        UserModel m = DataCache.getInstance().users.get(openid);

        pidET.setText(m.getPid());


    }

    @Override
    protected void setupData() {

    }

    public void do_save(View v)
    {
        String pid = pidET.getText().toString().trim();

        if(pid.split("_").length < 4)
        {
            XActivityindicator.showToast("推荐id格式不正确");
            return;
        }

        String openid = AlibcLogin.getInstance().getSession().openId;

        UserModel m = DataCache.getInstance().users.get(openid);

        if(m != null)
        {
            m.setPid(pid);
            XAPPUtil.saveAPPCache("Users",DataCache.getInstance().users);
        }

        back();

    }

}
