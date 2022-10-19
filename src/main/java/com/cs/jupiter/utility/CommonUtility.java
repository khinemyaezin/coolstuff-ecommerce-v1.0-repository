package com.cs.jupiter.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Date;
import java.util.regex.Pattern;


import org.springframework.core.env.Environment;

import com.google.gson.Gson;



public class CommonUtility {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public static Gson gsonBuilder = new Gson();
	
	public static String convertDate_2db(Date d) {
		if(d==null) return "";
		return dateFormat.format(d);
	}
	
	public static String convertTimeStamp_2db(Date d) {
		if(d==null) return "";
		return timeStampFormat.format(d);
	}

	public static Date convertDate_bydb(String d) {
		try {
			return dateFormat.parse(d);
		} catch (Exception e) {
			return new Date();
		}
	}
	public static Date convertTimeStamp_bydb(String d) {
		try {
			return timeStampFormat.parse(d);
		} catch (Exception e) {
			return new Date();
		}
	}

	public static String getCurrentDateTime() {
		return dateFormat.format(new Date());
	}
	

	public static String getCurrentTimeStamp() {
		return timeStampFormat.format(new Date());
	}
	
	
	public static String generateImagePath(String serverPath,String domain) {
		try {
			String p = Pattern.quote(System.getProperty("file.separator"));
			String[] ary = serverPath.split(p);
			int index = Arrays.asList(ary).indexOf(domain);
			if (index == -1)
				return "";
			StringBuilder sb = new StringBuilder();
			for (int i = index; i < ary.length; i++) {
				sb.append("/");
				sb.append(ary[i].toString());
				if (i != ary.length - 1)
					sb.append("/");
			}
			return sb.toString();
		} catch (Exception e) {
			return "";
		}

	}
	public static void outputLog(String log,Environment env) {
		if(env.getProperty("debug").equals("1")) {
			System.out.println(log);
		}
	}
	public static byte[] convertBase64ToByte(String imgString) {
		try {

			Decoder decoder =  Base64.getDecoder();
			return decoder.decode(imgString);
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void main(String[] args){
	
	}
	
	public static InputStream readFile(String path) {
		try {
			return new FileInputStream(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static String formatGeoPoint(double lng, double lat){
		StringBuilder builder = new StringBuilder();
		String startPoint = "POINT(";
		String endPoint = ")";
		builder.append(startPoint);
		builder.append(lng);
		builder.append(" ");
		builder.append(lat);
		builder.append(endPoint);
		return builder.toString();
	}
	static <T> void inspect(Class<T> klazz) {
        Field[] fields = klazz.getDeclaredFields();
        System.out.printf("%d fields:%n", fields.length);
        for (Field field : fields) {
            System.out.printf("%s %s %s%n",
                Modifier.toString(field.getModifiers()),
                field.getType().getSimpleName(),
                field.getName()
            );
        }
    }
}
