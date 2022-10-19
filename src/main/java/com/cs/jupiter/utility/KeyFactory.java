package com.cs.jupiter.utility;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

public class KeyFactory {

	private static int count = 1;
	public static String alb_num = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	
	public static void main(String[] arg){
		//System.out.println(UUID.randomUUID().toString());
		System.out.println(getStringId());
	}
	
	private static Long generateId(){
		Calendar cal = Calendar.getInstance();
		Integer r = new Random().nextInt(99 - 1) + 1;
		count = count >=99999 ? 1 : count+1;
		return new Long(getPrefixSyskey(cal.getTime()) + (r.toString().length() == 1 ? ("0" + r) : r) + "" + formatNumber());
	}
	public static long getId() {
		return generateId();
	}
	public static String getStringId() {
		return Long.toString(generateId());
	}

	private static String getPrefixSyskey(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(date);
	}

	private static String formatNumber() {
		DecimalFormat df = new DecimalFormat("00000");
		return df.format(count);
	}
	public static long generateRandom(int length) {
	    Random random = new Random();
	    char[] digits = new char[length];
	    digits[0] = (char) (random.nextInt(9) + '1');
	    for (int i = 1; i < length; i++) {
	        digits[i] = (char) (random.nextInt(10) + '0');
	    }
	    return Long.parseLong(new String(digits));
	}
	public static String generateABC(String num){
		Random rnd = new Random();
		char c = (char) (rnd.nextInt(26) + 'a');
	    return Character.toString(c);
	}
	public static String randomStockCode(int len){
		return getRandomString(alb_num, len);
	}
	public static String getRandomString(String prase, int outputnum) {
        String SALTCHARS = prase;
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < outputnum) { 
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
	
}
