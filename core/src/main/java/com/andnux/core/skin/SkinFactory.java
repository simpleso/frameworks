package com.andnux.core.skin;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.View;

import com.andnux.core.skin.impl.BackgroundAttr;
import com.andnux.core.skin.impl.DividerAttr;
import com.andnux.core.skin.impl.ListSelectorAttr;
import com.andnux.core.skin.impl.SrcAttr;
import com.andnux.core.skin.impl.TextColorAttr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class SkinFactory implements Factory,LayoutInflaterFactory {
	
	private List<SkinView> mSkinViews = Collections.synchronizedList(new ArrayList<SkinView>());
	
	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		View view;
		view = createView(name, context, attrs);
		if(null != view) {
			parseAttrs(name, context, attrs, view);
		}
		return view;
	}
	
	public View createView(String name, Context context, AttributeSet attrs) {
		View view = null;
		if(-1 == name.indexOf('.')) {
			if("View".equalsIgnoreCase(name)) {
				view = createView(name, context, attrs, "android.view.");
			}
			if(null == view) {
				view = createView(name, context, attrs, "android.widget.");
			}
			if(null == view) {
				view = createView(name, context, attrs, "android.webkit.");
			}
		} else {
			view = createView(name, context, attrs, null);
		}
		return view;
	}
	
	View createView(String name, Context context, AttributeSet attrs, String prefix) {
		View view = null;
		try {
			view = LayoutInflater.from(context).createView(name, prefix, attrs);
		} catch (Exception e) {
		}
		return view;
	}
	
	private void parseAttrs(String name, Context context, AttributeSet attrs, View view) {
		int attrCount = attrs.getAttributeCount();
		final Resources temp = context.getResources();
		List<BaseAttr> viewAttrs = new ArrayList<>();
		for(int i = 0; i < attrCount; i++) {
			String attrName = attrs.getAttributeName(i);
			String attrValue = attrs.getAttributeValue(i);
			if(isSupportedAttr(attrName)) {
				if(attrValue.startsWith("@")) {
					int id = Integer.parseInt(attrValue.substring(1));
					String entryName = temp.getResourceEntryName(id);
					String entryType = temp.getResourceTypeName(id);
					BaseAttr viewAttr = createAttr(attrName, attrValue, id, entryName, entryType);
					if(null != viewAttr) {
						viewAttrs.add(viewAttr);
					}
				}
			}
		}
		if(viewAttrs.size() > 0) {
			createSkinView(view, viewAttrs);
		}
	}
	
	// attrName:textColor   attrValue:2130968580   entryName:color_new_item_title   entryType:color
	public void createSkinView(View view, AttrName attrName, String attrValue, int id, String entryName, EntryType entryType) {
		BaseAttr viewAttr = createAttr(attrName.toString(), attrValue, id, entryName, entryType.toString());
		if(null != viewAttr) {
			List<BaseAttr> viewAttrs = new ArrayList<>(1);
			viewAttrs.add(viewAttr);
			createSkinView(view, viewAttrs);
		}
	}

	private void createSkinView(View view, List<BaseAttr> viewAttrs) {
		SkinView skinView = new SkinView();
		skinView.view = view;
		skinView.viewAttrs = viewAttrs ;
		mSkinViews.add(skinView);
		if(SkinManager.getInstance().isExternalSkin()) {
			skinView.apply();
		}
	}

	// attrName:textColor   attrValue:2130968580   entryName:color_new_item_title   entryType:color
	private BaseAttr createAttr(String attrName, String attrValue, int id, String entryName, String entryType) {
		BaseAttr viewAttr = null;
		if("background".equalsIgnoreCase(attrName)) {
			viewAttr = new BackgroundAttr();
		} else if("textColor".equalsIgnoreCase(attrName)) {
			viewAttr = new TextColorAttr();
		} else if("divider".equalsIgnoreCase(attrName)) {
			viewAttr = new DividerAttr();
		} else if("listSelector".equalsIgnoreCase(attrName)) {
			viewAttr = new ListSelectorAttr();
		} else if("src".equalsIgnoreCase(attrName)) {
			viewAttr = new SrcAttr();
		}
		if(null != viewAttr) {
			viewAttr.attrName = attrName;
			viewAttr.attrValue = id;
			viewAttr.entryName = entryName;
			viewAttr.entryType = entryType;
		}
		return viewAttr;
	}

	private boolean isSupportedAttr(String attrName) {
		if("background".equalsIgnoreCase(attrName)) {
			return true;
		} else if("textColor".equalsIgnoreCase(attrName)) {
			return true;
		} else if("divider".equalsIgnoreCase(attrName)) {
			return true;
		} else if("listSelector".equalsIgnoreCase(attrName)) {
			return true;
		} else if("src".equalsIgnoreCase(attrName)) {
			return true;
		}
		return false;
	}

	public void applySkin() {
		if(null != mSkinViews) {
			for(SkinView skinView : mSkinViews) {
				if(null != skinView.view) {
					skinView.apply();
				}
			}
		}
	}
	
	public void onDestroy() {
		if(null != mSkinViews) {
			for(SkinView skinView : mSkinViews) {
				skinView.destroy();
				skinView = null;
			}
			mSkinViews.clear();
			mSkinViews = null;
		}
	}

	@Override
	public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
		return onCreateView(name,context,attrs);
	}

	public enum EntryType {
		color,
		drawable
	}
	
	public enum AttrName {
		background,
		textColor,
		divider,
		listSelector,
		src
	}
}
