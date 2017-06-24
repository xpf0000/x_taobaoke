package com.yibaitaoke.x.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by x on 2017/6/22.
 */

public class TbkItemModel implements Serializable {


    /**
     * item_url : http://item.taobao.com/item.htm?id=553705113285
     * nick : 流川郁
     * num_iid : 553705113285
     * pict_url : http://img1.tbcdn.cn/tfscom/i2/TB1VSwLFpXXXXcSbVXXXXXXXXXX_!!0-item_pic.jpg
     * provcity : 山东 青岛
     * reserve_price : 4.80
     * seller_id : 52382534
     * title : [pc单机电脑游戏]MEMORIES ～将记忆的全部 中文(买5送1)
     * user_type : 0
     * volume : 0
     * zk_final_price : 4.13
     * small_images : {"string":["http://img4.tbcdn.cn/tfscom/i4/837797071/TB2AbG7qY8kpuFjy0FcXXaUhpXa_!!837797071.jpg","http://img3.tbcdn.cn/tfscom/i4/837797071/TB2JbScqYtlpuFjSspfXXXLUpXa_!!837797071.jpg","http://img4.tbcdn.cn/tfscom/i3/837797071/TB24XWxqYJkpuFjy1zcXXa5FFXa_!!837797071.jpg","http://img1.tbcdn.cn/tfscom/i1/837797071/TB2RBFmymFmpuFjSZFrXXayOXXa_!!837797071.jpg"]}
     */

    private String item_url;
    private String nick;
    private String num_iid;
    private String pict_url;
    private String provcity;
    private String reserve_price;
    private String seller_id;
    private String title;
    private int user_type;
    private int volume;
    private String zk_final_price;
    private SmallImagesBean small_images;

    public String getItem_url() {
        return item_url;
    }

    public void setItem_url(String item_url) {
        this.item_url = item_url;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNum_iid() {
        return num_iid;
    }

    public void setNum_iid(String num_iid) {
        this.num_iid = num_iid;
    }

    public String getPict_url() {
        return pict_url;
    }

    public void setPict_url(String pict_url) {
        this.pict_url = pict_url;
    }

    public String getProvcity() {
        return provcity;
    }

    public void setProvcity(String provcity) {
        this.provcity = provcity;
    }

    public String getReserve_price() {
        return reserve_price;
    }

    public void setReserve_price(String reserve_price) {
        this.reserve_price = reserve_price;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getZk_final_price() {
        return zk_final_price;
    }

    public void setZk_final_price(String zk_final_price) {
        this.zk_final_price = zk_final_price;
    }

    public SmallImagesBean getSmall_images() {
        return small_images;
    }

    public void setSmall_images(SmallImagesBean small_images) {
        this.small_images = small_images;
    }

    public static class SmallImagesBean {
        private List<String> string;

        public List<String> getString() {
            return string;
        }

        public void setString(List<String> string) {
            this.string = string;
        }
    }
}
