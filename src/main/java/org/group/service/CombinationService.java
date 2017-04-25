package org.group.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.group.bean.rule.Group;

import utils.log.Log4JUtils2;

public class CombinationService {
	public static int count = 0;
	public static List<List<Group>> resultGroupList = new ArrayList<List<Group>>();

	public static void combiantion(Group[] sourceGroups) {
		if (sourceGroups == null || sourceGroups.length == 0) {
			return;
		}
		List<Group> list = new ArrayList<Group>();
		for (int i = 1; i <= sourceGroups.length; i++) {
			combine(sourceGroups, 0, i, list);
		}
		System.out.println("Count=" + count);
	}

	/**
	 * 从字符数组中第begin个字符开始挑选number个字符加入list中
	 * 
	 * @param sourceGroups
	 * @param begin
	 * @param number
	 * @param resultList
	 */
	private static void combine(Group[] sourceGroups, int begin, int number, List<Group> resultList) {
		if (number == 0) {
			List<Group> list = new ArrayList<Group>(resultList);
			boolean bo = resultGroupList.add(list);
			// if (!bo) {
			// Log4JUtils2.getLogger().error("****** 重复添加:" + " " +
			// resultList.toString());
			// }
			Log4JUtils2.getLogger().trace("<<<<<<" + ((count++) + " " + list.toString()));
			return;
		}
		if (begin == sourceGroups.length) {
			return;
		}
		resultList.add(sourceGroups[begin]);
		combine(sourceGroups, begin + 1, number - 1, resultList);
		resultList.remove((Group) sourceGroups[begin]);
		combine(sourceGroups, begin + 1, number, resultList);
	}

	/*
	 * public static void main(String args[]) {
	 * System.out.println("Start CombinationService"); // combiantion(chs); }
	 */
}