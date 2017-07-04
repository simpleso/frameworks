package com.andnux.core.skin.impl;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ListView;

import com.andnux.core.skin.BaseAttr;
import com.andnux.core.skin.SkinManager;

public class DividerAttr extends BaseAttr {

	@Override
	public void apply(View view) {
		if(null != view && view instanceof ListView) {
			ListView listView = (ListView) view;
			if(isColorType()) {
				int color = SkinManager.getInstance().getColor(attrValue);
				ColorDrawable drawable = new ColorDrawable(color);
				listView.setDivider(drawable);
				listView.setDividerHeight(1);
			} else if(isDrawableType()) {
				listView.setDivider(SkinManager.getInstance().getDrawable(attrValue));
			}
		}
	}
}
