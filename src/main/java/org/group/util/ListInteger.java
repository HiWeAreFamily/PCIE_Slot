package org.group.util;

import java.util.List;

public class ListInteger {

	/**
	 * @param intList
	 * @return
	 */
	public static String listToString(List<Integer> intList) {
		String str = "";
		for (Integer i : intList) {
			str += i + ",";
		}
		return str;

	}

}
