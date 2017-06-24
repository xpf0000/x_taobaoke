package util;

/**
 * Created by admins on 2016/5/20.
 */
public class MyEventBus {
    private String msg;
    private Object info;

    public MyEventBus(String msg) {
        this.msg = msg;
    }

    public MyEventBus(String msg, Object info) {
        this.msg = msg;
        this.info = info;
    }

    public String getMsg() {
        return msg;
    }

    public Object getInfo() {
        return info;
    }

}
