package com.golaxy.utils;

public class HashCode {

	public static int seed = 31;

	public static int BKDRHash(String str) {
		int hash = 0;
		for (int i = 0; i != str.length(); ++i) {
			hash = seed * hash + str.charAt(i);
		}
		return hash;
	}

	public static int APHash(String str) {
		int hash = 0;

		for (int i = 0; i < str.length(); i++) {
			hash ^= ((i & 1) == 0) ? ((hash << 7) ^ str.charAt(i) ^ (hash >> 3))
					: (~((hash << 11) ^ str.charAt(i) ^ (hash >> 5)));
		}

		return hash;
	}

}
