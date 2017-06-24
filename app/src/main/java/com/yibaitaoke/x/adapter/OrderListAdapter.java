package com.yibaitaoke.x.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yibaitaoke.x.model.TbkItemModel;
import com.yibaitaoke.x.taobaoke.R;

import java.util.List;

/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class OrderListAdapter extends BaseAdapter {
    Context context;

    private List<TbkItemModel> list;

    public List<TbkItemModel> getList() {
        return list;
    }

    public void setList(List<TbkItemModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public OrderListAdapter(Context context, List<TbkItemModel> arr) {
        this.context = context;
        list = arr;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        getItemView bundle ;

        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_cell, null);
            bundle = new getItemView();
            bundle.name=(TextView)convertView.findViewById(R.id.order_cell_name);
            bundle.price=(TextView)convertView.findViewById(R.id.order_cell_price);
            bundle.num=(TextView)convertView.findViewById(R.id.order_cell_salenum);
            bundle.cprice=(TextView)convertView.findViewById(R.id.order_cell_cprice);

            bundle.img=(ImageView)convertView.findViewById(R.id.order_cell_img);
            bundle.type=(ImageView)convertView.findViewById(R.id.is_cat);

            convertView.setTag(bundle);
        }
        else
        {
            bundle = (getItemView) convertView.getTag();
        }

        TbkItemModel item = list.get(position);

        bundle.name.setText(item.getTitle());
        bundle.price.setText("￥"+item.getReserve_price());
        bundle.cprice.setText("￥"+item.getZk_final_price());
        bundle.num.setText("月销 "+item.getVolume()+"件");

        bundle.price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        ImageLoader.getInstance().displayImage(item.getPict_url(),bundle.img);

        if(item.getUser_type() == 1)
        {
            bundle.type.setVisibility(View.VISIBLE);
        }
        else
        {
            bundle.type.setVisibility(View.GONE);
        }


        return convertView;
    }

    private class getItemView {
        TextView name,cprice,num,price;
        ImageView img,type;
    }
}
