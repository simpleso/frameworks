package com.andnux.core.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class PhoneUtils {
	private static volatile PhoneUtils instance;

	private PhoneUtils() {
	}

	public static PhoneUtils getInstance() {
		if (instance == null) {
			synchronized (PhoneUtils.class) {
				if (instance == null) {
					instance = new PhoneUtils();
				}
			}
		}
		return instance;
	}

	public String getPhoneInfo(Context context) {
		StringBuffer stringBuffer = new StringBuffer();
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String mtyb = android.os.Build.BRAND;// 手机品牌
		String mtype = android.os.Build.MODEL; // 手机型号
		String imei = tm.getDeviceId();
		String imsi = tm.getSubscriberId();
		String numer = tm.getLine1Number(); // 手机号码
		String serviceName = tm.getSimOperatorName(); // 运营商
		stringBuffer.append("品牌: " + mtyb + "\n" + "型号: " + mtype + "\n" + "版本: Android "
				+ android.os.Build.VERSION.RELEASE + "\n" + "IMEI: " + imei + "\n" + "IMSI: " + imsi + "\n" + "手机号码: "
				+ numer + "\n" + "运营商: " + serviceName + "\n");
		stringBuffer.append("总内存: " + getTotalMemory(context) + "\n");
		stringBuffer.append("当前可用内存: " + getAvailMemory(context) + "\n");
		stringBuffer.append("CPU名字: " + getCpuName() + "\n");
		stringBuffer.append("CPU最大频率: " + getMaxCpuFreq(context) + "\n");
		stringBuffer.append("CPU最小频率: " + getMinCpuFreq() + "\n");
		stringBuffer.append("CPU当前频率: " + getCurCpuFreq() + "\n");
		return stringBuffer.toString();
	}

	/**
	 * 获取手机内存大小
	 * 
	 * @return
	 */
	private String getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}
			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Formatter.formatFileSize(context.getApplicationContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化
	}

	/**
	 * 获取当前可用内存大小
	 * 
	 * @return
	 */
	private String getAvailMemory(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(mi);
		return Formatter.formatFileSize(context.getApplicationContext(), mi.availMem);
	}

	private String getMaxCpuFreq(Context context) {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim() + "Hz";
	}

	// 获取CPU最小频率（单位KHZ）

	private String getMinCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim() + "Hz";
	}

	// 实时获取CPU当前频率（单位KHZ）

	private String getCurCpuFreq() {
		String result = "N/A";
		try {
			FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			result = text.trim() + "Hz";
			fr.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private String getCpuName() {
		try {
			FileReader fr = new FileReader("/proc/cpuinfo");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			String[] array = text.split(":\\s+", 2);
			for (int i = 0; i < array.length; i++) {
				Log.i("TAG", array[i]);
			}
			fr.close();
			br.close();
			return array[1];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
