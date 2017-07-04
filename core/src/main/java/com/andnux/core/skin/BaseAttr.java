package com.andnux.core.skin;

import android.view.View;

public abstract class BaseAttr {

	public String attrName;
	public int attrValue;
	public String entryName;
	public String entryType;

	protected boolean isDrawableType() {
		return "drawable".equalsIgnoreCase(entryType);
	}

	protected boolean isColorType() {
		return "color".equalsIgnoreCase(entryType);
	}
	
	public abstract void apply(View view);
}
