package com.golaxy.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	/**
	 * 替换所有空白符，换行符，制表符，空格符
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceAllS(String str) {
		return replace(str, "\\s*|\t|\r|\n");
	}

	/**
	 * 去除标点符号和不可见字符
	 * 
	 * @return
	 */
	public static String removePunctuation(String str) {
		return replace(str, "[\\p{P}\\p{Punct}\\p{Zs}]");
	}

	/**
	 * 匹配所有表情
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceAllemoji(String str) {
		str = replace(str, "\\[\\W{1,5}\\]");
		return replace(str, "\\[\\w+\\]");
	}

	/**
	 * 匹配所有url
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceAllUrl(String str) {
		return replace(str, "http://.+/\\w+");
	}

	public static String replace(String str, String rule) {
		Pattern p = Pattern.compile(rule);
		Matcher m = p.matcher(str);
		str = m.replaceAll("");
		return str;
	}

	public static String regex(String str) {
		Pattern p = Pattern.compile("\u200b");
		Matcher matcher = p.matcher(str);
		str = matcher.replaceAll("");
		return str;
	}
}
