package com.richitec.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;

public class CryptoUtil {
	private static Log log = LogFactory.getLog(CryptoUtil.class);
	/**
	 * calculate MD5 digest for the text
	 * @param text
	 * @return md5 digest string
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException 
	 */
	public static String md5(String text) {
		String digestText = "";
		try {
			MessageDigest digester = MessageDigest.getInstance("MD5");
			byte[] digest = digester.digest(text.getBytes("UTF-8"));
			digestText = HexUtils.convert(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return digestText;
	}
	
	public static String signHttpParam(Map<String, String> params, String key) {
		if (params != null && !params.isEmpty()) {
			ArrayList<String> paramList = new ArrayList<String>();
			Set<String> keys = params.keySet();
			for (String paramKey : keys) {
				if (!paramKey.equals("sign")) {
					paramList.add(paramKey + params.get(paramKey));
				}
			}
			Collections.sort(paramList);
			log.info("params after sorting");
			printList(paramList);
			
			StringBuffer sb2 = new StringBuffer();
			sb2.append(key);
			for (int i = 0; i < paramList.size(); i++) {
				sb2.append(paramList.get(i));
			}
			sb2.append(key);
			
			String digest = md5(sb2.toString());
			log.info("digest: " + digest);
			
			return digest;
		} else {
			return "";
		}
	}
	
	private static void printList(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			log.info(list.get(i));
		}
	}
}
