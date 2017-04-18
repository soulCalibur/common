
package com.ht.common.util;

import java.util.List;

public final class ListUtil {

	/***
	 * 判断list是否不为空
	 * @param list
	 * @return
	 */
	public static boolean isNotEmpty(List<?> list) {
		return !isEmpty(list);
	}

	/***
	 * 判断list是否为空
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(List<?> list) {
		return list == null || list.isEmpty();
	}

	/**
	 * 截取集合 当fromIndex = toIndex返回时空集合
	 * @param list
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 */
	public static <I> List<I> subList(List<I> list, int fromIndex, int toIndex) {
		if (list.size() < toIndex) {
			toIndex = list.size();
		}
		if (list.size() < fromIndex)
			fromIndex = list.size();
		return list.subList(fromIndex, toIndex);
	}

	private ListUtil() {
	}
}
