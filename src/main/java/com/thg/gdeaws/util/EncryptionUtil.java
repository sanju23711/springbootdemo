package com.thg.gdeaws.util;

import java.security.MessageDigest;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

public class EncryptionUtil {
	
	public static void main(String[] args) {
		
		System.out.println(generatePassword(8, 16));
		System.out.println("H3&6?@*u6f*f63H9?3$u0y0Pf%fu0u6!96&@f9y6".equals(encryptPassword("UQm2q#3L4")));
		
		System.out.println(encryptPassword("4n47_ChV"));
	}
	
	public static String generateReceipt(){
		return encrypt(String.valueOf(System.nanoTime()), getRecieptHex());
	}
	
	public static String generateBadge(){
		return encrypt(UUID.randomUUID().toString(), getBadgeHex());
	}
	
	public static String encryptPassword(String unencrypted){
		return encrypt(unencrypted, "MD5", "SHA-1", "UTF8", getPasswordHex());
	}
	
	public static String encrypt(String unencrypted, char[] hex){
		return encrypt(unencrypted, "MD5", "SHA-1", "UTF8", hex);
	}
	
	public static String encrypt(String unencrypted, String digest, String algorithm, String encoding, char[] hex){
		if(StringUtils.isEmpty(unencrypted)) return "";
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(digest);
			messageDigest = MessageDigest.getInstance(algorithm);
			byte[] md5Bytes = messageDigest.digest(unencrypted.getBytes(encoding));
			String encrypted = new String(toHexString(md5Bytes, hex));
			return encrypted;
		} catch (Exception nsae) { } 
		return unencrypted;
	}
	
	private static char[] toHexString(byte[] data, char[] hex) {
		int l = data.length;
		char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = hex[(0xF0 & data[i]) >>> 4];
			out[j++] = hex[0x0F & data[i]];
		}
		return out;
	}
	
	private static char[] getPasswordHex(){
		return new char[]{'0','!','@','H','$','%','&','*','?','9','P','y','f','u','6','3'};
	}
	
	private static char[] getBadgeHex(){
		return new char[]{'1','2','3','4','5','6','7','8','9','a','e','i','0','u','a','0'};
	}
	
	private static char[] getRecieptHex(){
		return new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p'};
	}
	
	
	private static String generatePassword(int minLength, int maxLength){
		String[] seed = new String[4];
		seed[0] = "abcdefghijklmnopqrstuvwxyz";
		seed[1] = seed[0].toUpperCase();
		seed[2] = "0123456789";
		seed[3] = "!@$*_-";
		
		int length = randomInt(minLength, maxLength);
		String password = "";
		for(int i = 0; i < length; i++){
			int order = randomInt(0, seed.length - 1);
			password = password + seed[order].charAt(randomInt(0, seed[order].length() - 1));
		}
		return password;
	}
	
	private static int randomInt(int Min, int Max){
		return Min + (int)(Math.random() * ((Max - Min) + 1));
	}
	
	

}
