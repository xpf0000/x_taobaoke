package util;

import android.app.Activity;
import android.content.Intent;

import com.robin.lazy.cache.CacheLoaderManager;
import com.yibaitaoke.x.model.UserModel;

import java.util.HashMap;
import java.util.Map;

//import com.robin.lazy.cache.CacheLoaderManager;
//
//
//import org.greenrobot.eventbus.EventBus;



/**
 * Created by X on 2016/10/2.
 */

public class DataCache {

    private static volatile DataCache instance=null;

    public static  DataCache getInstance(){
        if(instance==null){
            synchronized(DataCache .class){
                if(instance==null){
                    instance=new DataCache ();
                }
            }
        }
        return instance;
    }

    public int land = 0;
    public boolean msgshow = false;

    public HashMap<String,UserModel> users = new HashMap();

    public void init()
    {

    }


    private DataCache()
    {
        HashMap<String,UserModel> obj = CacheLoaderManager.getInstance().loadSerializable("Users");

        if(obj != null)
        {
            users = obj;
        }

    }

}
