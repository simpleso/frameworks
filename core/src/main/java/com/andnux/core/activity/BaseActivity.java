package com.andnux.core.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.andnux.core.utils.InjectUtils;

/**
 * Created by Shenbin on 17/6/17.
 */

public class BaseActivity extends SkinActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectUtils.inject(this);
        init();
    }

    protected void  init(){

    }
}
