package com.andnux.core.skin.impl;

import android.view.View;

import com.andnux.core.skin.BaseAttr;
import com.andnux.core.skin.SkinManager;

public class BackgroundAttr extends BaseAttr {

	@Override
	public void apply(View view) {
		if(null != view) {
			if(isColorType()) {
				view.setBackgroundColor(SkinManager.getInstance().getColor(attrValue));
			} else if(isDrawableType()) {
				view.setBackgroundDrawable(SkinManager.getInstance().getDrawable(attrValue));
			}
		}
	}
}
