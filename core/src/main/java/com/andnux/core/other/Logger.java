package com.andnux.core.other;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import com.andnux.core.utils.FileUtils;

import java.io.File;

public class Logger {
	private static volatile Logger instance;
	/**
	 * VERBOSE日志形式的标识符
	 */
	public static final int VERBOSE = 5;
	/**
	 * DEBUG日志形式的标识符
	 */
	public static final int DEBUG = 4;
	/**
	 * INFO日志形式的标识符
	 */
	public static final int INFO = 3;
	/**
	 * WARN日志形式的标识符
	 */
	public static final int WARN = 2;
	/**
	 * ERROR日志形式的标识符
	 */
	public static final int ERROR = 1;

	private String mPath;

	private Logger() {
	}

	public static Logger getInstance() {
		if (instance == null) {
			synchronized (Logger.class) {
				if (instance == null) {
					instance = new Logger();
				}
			}
		}
		return instance;
	}

	public void init(String path) {
		this.mPath = path;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public void setPath(String mPath) {
		this.mPath = mPath;
	}

	public String getPath() {
		return mPath;
	}

	/**
	 * 用来输出日志的综合方法（文本内容）
	 * 
	 * @param @param
	 *            tag 日志标识 @param @param msg 要输出的内容 @param @param type
	 *            日志类型 @return void 返回类型 @throws
	 */
	public void log(String tag, String msg, int type, boolean isDeBug, String fileName) {
		switch (type) {
		case VERBOSE:
			v(tag, msg, isDeBug, fileName);// verbose等级
			break;
		case DEBUG:
			d(tag, msg, isDeBug, fileName);// debug等级
			break;
		case INFO:
			i(tag, msg, isDeBug, fileName);// info等级
			break;
		case WARN:
			w(tag, msg, isDeBug, fileName);// warn等级
			break;
		case ERROR:
			e(tag, msg, isDeBug, fileName);// error等级
			break;
		default:
			break;
		}
	}

	/**
	 * verbose等级的日志输出
	 * 
	 * @param tag
	 *            日志标识 @param msg 要输出的内容 @return void 返回类型 @throws
	 */
	public void v(String tag, String msg) {
		v(tag, msg, true);
	}

	/**
	 * verbose等级的日志输出
	 * 
	 * @param tag
	 *            日志标识 @param msg 要输出的内容 @return void 返回类型 @throws
	 */
	public void v(String tag, String msg, boolean isDeBug) {
		v(tag, msg, isDeBug, null);
	}

	/**
	 * verbose等级的日志输出
	 * 
	 * @param tag
	 *            日志标识 @param msg 要输出的内容 @return void 返回类型 @throws
	 */
	public void v(String tag, String msg, boolean isDeBug, String fileName) {
		// 是否开启日志输出
		if (isDeBug) {
			Log.v(tag, msg);
		}
		// 是否将日志写入文件
		if (!TextUtils.isEmpty(fileName)) {
			write(tag, msg, fileName);
		}
	}

	/**
	 * debug等级的日志输出
	 * 
	 * @param tag
	 *            标识 @param msg 内容 @return void 返回类型 @throws
	 */
	public void d(String tag, String msg) {
		d(tag, msg, true);
	}

	/**
	 * debug等级的日志输出
	 * 
	 * @param tag
	 *            标识 @param msg 内容 @return void 返回类型 @throws
	 */
	public void d(String tag, String msg, boolean isDeBug) {
		d(tag, msg, isDeBug, null);
	}

	/**
	 * debug等级的日志输出
	 * 
	 * @param tag
	 *            标识 @param msg 内容 @return void 返回类型 @throws
	 */
	public void d(String tag, String msg, boolean isDeBug, String fileName) {
		if (isDeBug) {
			Log.d(tag, msg);
		}
		if (!TextUtils.isEmpty(fileName)) {
			write(tag, msg, fileName);
		}
	}

	/**
	 * info等级的日志输出
	 * 
	 * @param tag
	 *            标识 @param msg 内容 @return void 返回类型 @throws
	 */
	public void i(String tag, String msg) {
		i(tag, msg, true);
	}

	/**
	 * info等级的日志输出
	 * 
	 * @param tag
	 *            标识 @param msg 内容 @return void 返回类型 @throws
	 */
	public void i(String tag, String msg, boolean isDeBug) {
		i(tag, msg, isDeBug, null);
	}

	/**
	 * info等级的日志输出
	 * 
	 * @param tag
	 *            标识 @param msg 内容 @return void 返回类型 @throws
	 */
	public void i(String tag, String msg, boolean isDeBug, String fileName) {
		if (isDeBug) {
			Log.i(tag, msg);
		}
		if (!TextUtils.isEmpty(fileName)) {
			write(tag, msg, fileName);
		}
	}

	/**
	 * warn等级的日志输出
	 * 
	 * @param tag
	 *            标识 @param msg 内容 @return void 返回类型 @throws
	 */
	public void w(String tag, String msg) {
		w(tag, msg, true);
	}

	/**
	 * warn等级的日志输出
	 * 
	 * @param tag
	 *            标识 @param msg 内容 @return void 返回类型 @throws
	 */
	public void w(String tag, String msg, boolean isDeBug) {
		w(tag, msg, isDeBug, null);
	}

	/**
	 * warn等级的日志输出
	 * 
	 * @param tag
	 *            标识 @param msg 内容 @return void 返回类型 @throws
	 */
	public void w(String tag, String msg, boolean isDeBug, String fileName) {
		if (isDeBug) {
			Log.w(tag, msg);
		}
		if (!TextUtils.isEmpty(fileName)) {
			write(tag, msg, fileName);
		}
	}

	/**
	 * error等级的日志输出
	 * 
	 * @param tag
	 *            标识
	 * @param msg
	 *            内容
	 * @return void 返回类型
	 */
	public void e(String tag, String msg) {
		e(tag, msg, true);
	}

	/**
	 * error等级的日志输出
	 * 
	 * @param tag
	 *            标识
	 * @param msg
	 *            内容
	 * @return void 返回类型
	 */
	public void e(String tag, String msg, boolean isDeBug) {
		e(tag, msg, isDeBug, null);
	}

	/**
	 * error等级的日志输出
	 * 
	 * @param tag
	 *            标识
	 * @param msg
	 *            内容
	 * @return void 返回类型
	 */
	public void e(String tag, String msg, boolean isDeBug, String fileName) {
		if (isDeBug) {
			Log.w(tag, msg);
		}
		if (!TextUtils.isEmpty(fileName)) {
			write(tag, msg, fileName);
		}
	}

	/**
	 * 用于把日志内容写入制定的文件
	 * 
	 * @param @param
	 *            tag 标识 @param @param msg 要输出的内容 @return void 返回类型 @throws
	 */
	public void write(String tag, String msg, String fileNsame) {
		String path = FileUtils.createMkdirsAndFiles(mPath, fileNsame);
		String log = DateFormat.format("yyyy年MM月dd日 HH时mm分ss秒", System.currentTimeMillis()) + tag + "========>>" + msg
				+ "\n=================================分割线=================================";
		FileUtils.write2File(path, log, true);
	}
}
