package com.andnux.core.skin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class SkinManager {

	private static final String TAG = "SkinManager";
	private static final Object mClock = new Object();
	private static SkinManager mInstance;
	
	private Context mContext;
	private Resources mResources;
	private String mSkinPkgName;
	private String mSkinApkPath;
	private SharedPreferences mPreferences;
	private List<ISkinUpdate> mObservers;
	
	public static SkinManager getInstance() {
		if(null == mInstance) {
			synchronized (mClock) {
				if(null == mInstance) {
					mInstance = new SkinManager();
				}
			}
		}
		return mInstance;
	}
	
	public void init(Context context) {
		enableContext(context);
		mContext = context.getApplicationContext();
		mPreferences = mContext.getSharedPreferences("skin",Context.MODE_PRIVATE);
		mSkinApkPath = mPreferences.getString("path","");
	}
	
	public void loadSkin(String skinPath) {
		if (TextUtils.isEmpty(skinPath)){
			restoreDefaultSkin();
		}else {
			loadSkin(skinPath, null);
		}
	}
	
	public void loadSkin(final String skinPath, final ILoadListener listener) {
		enableContext(mContext);
		if(TextUtils.isEmpty(skinPath) || skinPath.equals(mSkinApkPath)) {
			return;
		}
		new AsyncTask<String, Void, Resources>() {
			@Override
			protected void onPreExecute() {
				if(null != listener) {
					listener.onStart();
				}
			}
			
			@Override
			protected Resources doInBackground(String... params) {
				if(null != params && params.length == 1) {
					String skinPath = params[0];
					File file = new File(skinPath);
					if(null != file && file.exists()) {
						PackageManager packageManager = mContext.getPackageManager();
						PackageInfo packageInfo = packageManager.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES);
						if(null != packageInfo) {
							mSkinPkgName = packageInfo.packageName;
						}
						return getResources(mContext, skinPath);
					}
				}
				return null;
			}
			@Override
			protected void onPostExecute(Resources result) {
				super.onPostExecute(result);
				if(null != result) {
					Log.e("", "load skin success");
					mSkinApkPath = skinPath;
					mResources = result;
					notifySkinUpdate();
					if (mPreferences != null){
						mPreferences.edit().putString("path",skinPath).commit();
					}
					if(null != listener) {
						listener.onSuccess();
					}
				} else {
					Log.e("", "load skin failure");
					restoreDefaultSkin(); //失败就使用默认的皮肤
					if(null != listener) {
						listener.onFailure();
					}
				}
			}
		}.execute(skinPath);
	}
	
	public Resources getResources(Context context, String apkPath) {
		try {
			AssetManager assetManager = AssetManager.class.newInstance();
			Method addAssetPath = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
			addAssetPath.setAccessible(true);
			addAssetPath.invoke(assetManager, apkPath);
			Resources r = context.getResources();
			Resources skinResources = new Resources(assetManager, r.getDisplayMetrics(), r.getConfiguration());
			return skinResources;
		} catch (Exception e) {
		}
		return null;
	}
	
	public void restoreDefaultSkin() {
		if(null != mResources) {
			mResources = null;
			mSkinPkgName = null;
			mSkinApkPath = null;
			notifySkinUpdate();
		}
		if (mPreferences != null){
			mPreferences.edit().putString("path","").commit();
		}
	}
	
	public int getColor(int id) {
		enableContext(mContext);
		Resources originResources = mContext.getResources();
		int originColor = originResources.getColor(id);
		if(null == mResources || TextUtils.isEmpty(mSkinPkgName)) {
			return originColor;
		}
		try {
			String entryName = originResources.getResourceEntryName(id);
			int resourceId = mResources.getIdentifier(entryName, "color", mSkinPkgName);
			if (resourceId == 0){
				return  originColor;
			}
			return mResources.getColor(resourceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return originColor;
	}
	
	public Drawable getDrawable(int id) {
		enableContext(mContext);
		Resources originResources = mContext.getResources();
		Drawable originDrawable = originResources.getDrawable(id);
		if(null == mResources || TextUtils.isEmpty(mSkinPkgName)) {
			return originDrawable;
		}
		try {
			String entryName = originResources.getResourceEntryName(id);
			int resourceId = mResources.getIdentifier(entryName, "drawable", mSkinPkgName);
			if (resourceId == 0){
				resourceId = mResources.getIdentifier(entryName, "mipmap", mSkinPkgName);
			}
			if (resourceId == 0){
				resourceId = mResources.getIdentifier(entryName, "color", mSkinPkgName);
			}
			if (resourceId == 0){
				return  originDrawable;
			}
			return mResources.getDrawable(resourceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return originDrawable;
	}
	
	public void onAttach(ISkinUpdate observer) {
		if(null == observer) return;
		if(null == mObservers) {
			mObservers = new ArrayList<>();
		}
		if(!mObservers.contains(observer)) {
			mObservers.add(observer);
		}
	}
	
	public void onDettach(ISkinUpdate observer) {
		if(null == observer || null == mObservers) return;
		mObservers.remove(observer);
		observer = null;
	}
	
	public void notifySkinUpdate() {
		if(null != mObservers) {
			for(ISkinUpdate observer : mObservers) {
				observer.updateSkin();
			}
		}
	}
	
	private void enableContext(Context context) {
		if(null == context) {
			throw new NullPointerException();
		}
	}

	public boolean isExternalSkin() {
		return null == mResources ? false : true;
	}
}
