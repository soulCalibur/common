
package com.ht.common.util;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

	public static String encode(byte[] bytes) {
		Base64 base64 = new Base64();
		return new String(base64.encode(bytes));
	}

	public static byte[] decodeBuffer(String value) {
		Base64 base64 = new Base64();
		return base64.decode(value.getBytes());
	}
}
