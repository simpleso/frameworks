package com.andnux.core.skin.impl;

import android.view.View;
import android.widget.TextView;

import com.andnux.core.skin.BaseAttr;
import com.andnux.core.skin.SkinManager;

public class TextColorAttr extends BaseAttr {

	@Override
	public void apply(View view) {
		if(null != view) {
			if(isColorType()) {
				if(view instanceof TextView) {
					TextView textView = (TextView)view;
					textView.setTextColor(SkinManager.getInstance().getColor(attrValue));
				} else {
					view.setBackgroundColor(SkinManager.getInstance().getColor(attrValue));
				}
			} else if(isDrawableType()) {
				view.setBackgroundDrawable(SkinManager.getInstance().getDrawable(attrValue));
			}
		}
	}
}
