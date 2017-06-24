package net;

/**
 * Created by X on 2016/10/1.
 */

public class HttpResult<T> {


    /**
     * page_title : 同城百家 - 城市切换
     * ctl : city
     * act : app_index
     * status : 1
     * info :
     * city_name : 福州
     * return : 1
     * sess_id : nfrul8fgn1mmkvpp79m6qs3us5
     * ref_uid : null
     */

    private String page_title;
    private String ctl;
    private String act;
    private int status;
    private String info;
    private String city_name;
    private String sess_id;
    private Object ref_uid;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getPage_title() {
        return page_title;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }

    public String getCtl() {
        return ctl;
    }

    public void setCtl(String ctl) {
        this.ctl = ctl;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getSess_id() {
        return sess_id;
    }

    public void setSess_id(String sess_id) {
        this.sess_id = sess_id;
    }

    public Object getRef_uid() {
        return ref_uid;
    }

    public void setRef_uid(Object ref_uid) {
        this.ref_uid = ref_uid;
    }
}
