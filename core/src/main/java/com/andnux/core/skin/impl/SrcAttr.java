package com.andnux.core.skin.impl;

import android.view.View;
import android.widget.ImageView;

import com.andnux.core.skin.BaseAttr;
import com.andnux.core.skin.SkinManager;

public class SrcAttr extends BaseAttr {

	@Override
	public void apply(View view) {
		if(null != view && view instanceof ImageView) {
			ImageView imageView = (ImageView) view;
			imageView.setImageDrawable(SkinManager.getInstance().getDrawable(attrValue));
		}
	}
}
