package com.andnux.core.skin.impl;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.AbsListView;

import com.andnux.core.skin.BaseAttr;
import com.andnux.core.skin.SkinManager;

public class ListSelectorAttr extends BaseAttr {

	@SuppressWarnings("deprecation")
	@Override
	public void apply(View view) {
		if(null != view && view instanceof AbsListView) {
			AbsListView absListView = (AbsListView) view;
			if(isColorType()) {
				int result = SkinManager.getInstance().getColor(attrValue);
				if(0 != result) {
					absListView.setSelector(result);
				} else {
					absListView.setSelector(new BitmapDrawable());
				}
			} else if(isDrawableType()) {
				absListView.setSelector(SkinManager.getInstance().getDrawable(attrValue));
			}
		}
	}
}
