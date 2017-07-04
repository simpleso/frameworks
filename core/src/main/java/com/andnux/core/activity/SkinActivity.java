package com.andnux.core.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.andnux.core.skin.ISkinUpdate;
import com.andnux.core.skin.SkinFactory;
import com.andnux.core.skin.SkinManager;

/**
 * Created by Shenbin on 17/6/13.
 */

public class SkinActivity extends FragmentActivity implements ISkinUpdate {

    protected LayoutInflater mInflater;
    private   SkinFactory mFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFactory = new SkinFactory();
        mInflater = getLayoutInflater();
        mInflater.setFactory(mFactory);
//        LayoutInflaterCompat.setFactory(mInflater,mFactory);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SkinManager.getInstance().onAttach(this);
    }

    @Override
    protected void onDestroy() {
        destroySkinRes();
        super.onDestroy();
    }

    public final void destroySkinRes() {
        if(null != mFactory) {
            mFactory.onDestroy();
        }
        mFactory = null;
        mInflater = null;
        SkinManager.getInstance().onDettach(this);
    }

    public final void createSkinView(View view, int id, SkinFactory.AttrName attrName, SkinFactory.EntryType entryType) {
        mFactory.createSkinView(view, attrName, "", id, "", entryType);
    }

    @Override
    public final void updateSkin() {
        mFactory.applySkin();
    }
}
