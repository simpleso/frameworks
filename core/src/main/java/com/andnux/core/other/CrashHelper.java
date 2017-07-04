package com.andnux.core.other;

import android.content.Context;
import android.text.format.DateFormat;

import com.andnux.core.utils.PhoneUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class CrashHelper implements Thread.UncaughtExceptionHandler {

	private static CrashHelper mCrashHelper;
	private Thread.UncaughtExceptionHandler defaultUEH;
	private Context mContext;

	private CrashHelper() {
	}

	public static CrashHelper getCrashHelper() {
		if (mCrashHelper == null) {
			synchronized (CrashHelper.class) {
				if (mCrashHelper == null) {
					mCrashHelper = new CrashHelper();
				}
			}
		}
		return mCrashHelper;
	}

	public void init(Context context, String path) {
		this.mContext = context.getApplicationContext();
		this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		Logger.getInstance().init(path);
	}

	@Override
	public void uncaughtException(final Thread thread, final Throwable ex) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		ex.printStackTrace(printWriter);
		final String stacktrace = result.toString();
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(PhoneUtils.getInstance().getPhoneInfo(mContext));
		stringBuffer.append("----------------------------------------------");
		stringBuffer.append(stacktrace);
		final String fileName = DateFormat.format("yyyy年MM月dd日 HH时mm分ss秒", System.currentTimeMillis()) + ".txt";
		Logger.getInstance().e("uncaughtException", stringBuffer.toString(), true, fileName);
		defaultUEH.uncaughtException(thread, ex);
	};
}
