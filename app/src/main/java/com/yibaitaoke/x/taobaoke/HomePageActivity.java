package com.yibaitaoke.x.taobaoke;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.yibaitaoke.x.fragment.HomeFragment;
import com.yibaitaoke.x.fragment.MingdanFragment;
import com.yibaitaoke.x.model.TKModel;
import com.yibaitaoke.x.model.TbkItemModel;

import net.XAPPUtil;
import net.XActivityindicator;
import net.XNetUtil;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;
import util.StringUtils;
import util.TaobaoUtils;

import static util.ApplicationClass.okHttpClient;

/**
 * 引导页--简介
 * 
 * @author zmz
 * @version 1.0
 */
public class HomePageActivity extends FragmentActivity implements
        OnCheckedChangeListener {

	private DrawerLayout drawerLayout;
	private Intent intent;
	Toolbar toolbar;
	ActionBarDrawerToggle toggle;

	public TextView orderTV;
	public CheckBox taobaoCB,tianmaoCB;
	public EditText minpET,maxpET,minyET,maxyET;

	String activityStyle;
	RadioGroup tabbar;

	private RadioButton home,mine;
	private FragmentTransaction transaction;
	public HomeFragment homeFragment;
	public MingdanFragment mineFragment;

	AlertView alertView;
	public String order = "";

	private String[] orders = {"","total_sales_des","tk_rate_des","tk_total_sales_des","tk_total_commi_des"};
	private String[] orderstrs = {"默认排序", "销量从高到低","佣金从高到低","推广量从高到低","支出佣金从高到低"};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);

		initComponents();
		intent = getIntent();
		activityStyle = intent.getStringExtra("activity");
		//getHotSearch();
		if (homeFragment == null) {
			homeFragment = new HomeFragment();
			transaction.add(R.id.ll_content, homeFragment);
			transaction.commit();
		}

		initDrawerLayout();


		alertView = new AlertView("排序方式", null, "取消", null,
				orderstrs,
				this, AlertView.Style.ActionSheet, new OnItemClickListener(){
			public void onItemClick(Object o,int p){
				XNetUtil.APPPrintln("p: "+p);

				if(p >= 0)
				{
					order = orders[p];
					orderTV.setText(orderstrs[p]);
				}

			}
		});


	}



	private void initDrawerLayout(){

		toolbar = (Toolbar) findViewById(R.id.toolbar);

		drawerLayout=(DrawerLayout)super.findViewById(R.id.drawer_layout);
		drawerLayout.setScrimColor(Color.parseColor("#33000000"));
		toggle=new ActionBarDrawerToggle(this,drawerLayout,
				toolbar,R.string.navigation_drawer_open
				,R.string.navigation_drawer_close){
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
			}
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}

		};
		drawerLayout.setDrawerListener(toggle);
	}


	public void toggleRightSliding(){//该方法控制右侧边栏的显示和隐藏
		if(drawerLayout.isDrawerOpen(Gravity.END)){
			drawerLayout.closeDrawer(Gravity.END);
		}else{
			drawerLayout.openDrawer(Gravity.END);
		}
	}


	public void initComponents() {

		orderTV = (TextView) findViewById(R.id.home_order);
		taobaoCB = (CheckBox) findViewById(R.id.home_taobao);
		tianmaoCB = (CheckBox) findViewById(R.id.home_tianmao);

		minpET = (EditText) findViewById(R.id.home_minprice);
		maxpET = (EditText) findViewById(R.id.home_maxprice);
		minyET = (EditText) findViewById(R.id.home_minyongjin);
		maxyET = (EditText) findViewById(R.id.home_maxyongjin);

		tabbar = (RadioGroup) findViewById(R.id.home_tabbar);
		home = (RadioButton) findViewById(R.id.rb_home);
		mine = (RadioButton) findViewById(R.id.rb_user);

		home.setOnCheckedChangeListener(this);
		mine.setOnCheckedChangeListener(this);

		transaction = getSupportFragmentManager().beginTransaction();

	}



	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			transaction = getSupportFragmentManager().beginTransaction();
			switch (buttonView.getId()) {
			case R.id.rb_home:

				if (homeFragment == null) {
					homeFragment = new HomeFragment();
					transaction.add(R.id.ll_content, homeFragment);
				}

				if (mineFragment != null) {
					transaction.hide(mineFragment);
				}

				transaction.show(homeFragment);

				break;

				case R.id.rb_user:

					if (mineFragment == null) {
						mineFragment = new MingdanFragment();
						transaction.add(R.id.ll_content, mineFragment);
					}

					if (homeFragment != null) {
						transaction.hide(homeFragment);
					}

					transaction.show(mineFragment);
					break;
			}
			transaction.commit();
		}
	}



	public void set_order(View v)
	{
		alertView.show();
	}

	public void do_submit(View v)
	{
		homeFragment.reset();
		homeFragment.getData();
		toggleRightSliding();
	}

	public void to_taobao(View v)
	{

		if (XAPPUtil.checkPackage(this, "com.taobao.taobao")) {
			String path ="taobao://www.taobao.com/";

			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri uri = Uri.parse(path);
			intent.setData(uri);
			startActivity(intent);
		} else {
			XActivityindicator.showToast("未安装淘宝客户端，请先安装");
			XNetUtil.APPPrintln("未安装淘宝客户端，请先安装");
		}

	}


	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			// 提示放屏幕中间
			// Toast toast;
			// toast = Toast.makeText(getApplicationContext(), "再按一次退出程序",
			// Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.CENTER, 0, 0);
			// toast.show();

			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {

			finish();
			System.exit(0);
		}
	}


	public void onResume() {
		super.onResume();

	}

	public void onPause() {
		super.onPause();
	}







}
