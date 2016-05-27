package net.motionintelligence.client.api.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class IOUtil {

	public static String decode(String string) {
		
		try {
			
			return URLDecoder.decode(string, StandardCharsets.UTF_8.name());
		} 
		catch (UnsupportedEncodingException e) {
			throw new AssertionError("UTF-8 is unknown");
		}
	}
	
	public static String encode(String string) {
		
		try {
			
			return URLEncoder.encode(string, StandardCharsets.UTF_8.name());
		} 
		catch (UnsupportedEncodingException e) {
			throw new AssertionError("UTF-8 is unknown");
		}
	}
	
	public static void main(String[] args) {
		
		System.out.println(IOUtil.encode("18923789127*&(!@&#*(^!*&@$^%&*%!"));
	}
}
