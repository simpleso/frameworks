package com.andnux.core.skin;

import android.util.Log;
import android.view.View;

import java.util.List;

public class SkinView {

	public View view;
	public List<BaseAttr> viewAttrs;
	
	public void apply() {
		if(null != view && null != viewAttrs) {
			for(BaseAttr attr : viewAttrs) {
				attr.apply(view);
			}
		}
	}
	
	public void destroy() {
		if(null != viewAttrs) {
			for(BaseAttr attr : viewAttrs) {
				Log.d("destroy skinview", attr.toString());
				attr = null;
			}
			viewAttrs.clear();
			viewAttrs = null;
		}
		view = null;
	}
}
