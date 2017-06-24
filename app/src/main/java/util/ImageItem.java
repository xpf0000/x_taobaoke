package util;

import android.graphics.Bitmap;

import java.io.IOException;
import java.io.Serializable;


public class ImageItem implements Serializable {

	private String imagePath,url;
	private Bitmap bitmap;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public void clean()
	{
		url = null;
		imagePath = null;

		if(bitmap != null)
		{
			if(!bitmap.isRecycled()){
				bitmap.recycle();   //回收图片所占的内存
				System.gc();  //提醒系统及时回收
			}
		}

		bitmap = null;
	}

	public Bitmap getBitmap() {
		if(bitmap == null){
			try {
				bitmap = Bimp.revitionImageSize(imagePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

}
