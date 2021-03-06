package org.group.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.Set;
import java.util.Vector;

import org.group.bean.optimize.RuleCondition;
import org.group.bean.rule.Group;
import org.group.bean.rule.MinMaxRule;

import utils.log.Log4JUtils2;

/**
 * @author fangh1
 *
 */
public class OptimizeService extends Thread {

	Vector<MinMaxRule> minMaxRules;
	RuleCondition condition;
	int conditionIndex;
	Entry<Integer, List<List<Group>>> maxTargetresult;
	boolean flag = true;

	/**
	 * @param minMaxRules
	 *            存储Result
	 * @param condition
	 *            条件
	 * @param conditionIndex
	 *            条件index
	 * @param maxTargetresult
	 *            Max 和 所有待优化List<List<Group>>
	 */
	public OptimizeService(Vector<MinMaxRule> minMaxRules, RuleCondition condition, int conditionIndex,
			Entry<Integer, List<List<Group>>> maxTargetresult) {
		this.minMaxRules = minMaxRules;
		this.condition = condition;
		this.conditionIndex = conditionIndex;
		this.maxTargetresult = maxTargetresult;
	}

	@Override
	public void run() {
		// boolean flag = true;
		if (!flag) {
			Log4JUtils2.getLogger().info("====== 证实确实,线程启动!");
		}
		List<List<Group>> optimizeGroups = new ArrayList<List<Group>>();
		try {
			List<List<Group>> targetGroups = maxTargetresult.getValue();
			optimizeGroups = optimizeGroupListNew(targetGroups);
			Log4JUtils2.getLogger().info(
					"====== 该条件下 Max=" + maxTargetresult.getKey() + ";压缩前数量:" + targetGroups.size() + "压缩后数量:"
							+ optimizeGroups.size());
		} catch (Exception e) {
			flag = false;
			// TODO Auto-generated catch block
			Log4JUtils2.getLogger().error("****** ERROR 检测第一部分异常信息异常Start：");
			Log4JUtils2.getLogger().error(e);
			StackTraceElement[] error = e.getStackTrace();
			for (StackTraceElement stackTraceElement : error) {
				Log4JUtils2.getLogger().error(stackTraceElement.toString());
			}
			System.out.println("*******************************");
			e.printStackTrace();
			Log4JUtils2.getLogger().error("****** ERROR 异常End：");

		}
		// Debug :显示优化前的TargetGroups list
		// int i = 0;
		// for (List<Group> target : targetresult.getValue()) {
		// Log4JUtils2.getLogger().info("<<<<<< " + (++i) + "" +
		// target);
		// }
		List<MinMaxRule> minMaxRulecopy = new ArrayList<MinMaxRule>();
		try {
			/* 呈现具体的rule */
			int ruleIndex = 0;
			for (List<Group> optimizeGroup : optimizeGroups) {
				ruleIndex++;
				MinMaxRule minMaxRule = new MinMaxRule("C" + conditionIndex + "_Max" + maxTargetresult.getKey()
						+ "_Leo" + ruleIndex, condition, optimizeGroup, maxTargetresult.getKey());
				Log4JUtils2.getLogger().info("====== Rule:" + minMaxRule.formatDebugRule());
				minMaxRulecopy.add(minMaxRule);
			}
		} catch (Exception e) {
			flag = false;
			Log4JUtils2.getLogger().error("****** ERROR 检测第二部分异常信息异常Start：");
			Log4JUtils2.getLogger().error(e);
			StackTraceElement[] error = e.getStackTrace();
			for (StackTraceElement stackTraceElement : error) {
				Log4JUtils2.getLogger().error(stackTraceElement.toString());
			}
			System.out.println("*******************************");
			e.printStackTrace();
			Log4JUtils2.getLogger().error("****** ERROR 异常End：");

		}
		if (flag) {// true;
			minMaxRules.addAll(minMaxRulecopy);
		} else {// false
			Log4JUtils2.getLogger().info("====== Fail, Restart a new Tread!");
			ThreadPoolExecutor executorService = ThreadPoolExecutorFactory.getInstance();
			OptimizeService command = new OptimizeService(minMaxRules, condition, conditionIndex, maxTargetresult);
			executorService.execute(command);
		}
	}

	/**
	 * @param targetGroups
	 *            13万条
	 * @return
	 */
	public List<List<Group>> optimizeGroupList(List<List<Group>> targetGroups) {
		List<List<Group>> optimizeGroups = new ArrayList<List<Group>>();
		ListIterator<List<Group>> optimizeGroupsIt = optimizeGroups.listIterator();

		Set<List<Group>> temp = new LinkedHashSet<List<Group>>();
		boolean thisTargetNotNeed = false;
		// 1.需要优化的对象 targetGroups
		for (List<Group> target : targetGroups) {
			if (optimizeGroups.size() == 0) {
				optimizeGroupsIt.add(target);
				temp.add(target);
				// optimizeGroupsIt.previous();
			} else {
				// 2.优化的经过optimizeGroups
				optimizeGroupsIt = optimizeGroups.listIterator();
				while (optimizeGroupsIt.hasNext()) {
					List<Group> optimize = optimizeGroupsIt.next();
					if (optimize.containsAll(target)) {
						// A.小的不要
						thisTargetNotNeed = true;
						break;
					} else if (target.containsAll(optimize)) {
						// B.留下大的,剔除小的
						optimizeGroupsIt.remove();
						if (temp.contains(target)) {
							Log4JUtils2.getLogger().trace("------ 已经加过就不能在加入到优化的里面去了!" + target);
						} else {
							optimizeGroupsIt.add(target);
							// optimizeGroupsIt.previous();
							temp.add(target);
						}
					}
				}

				if (!thisTargetNotNeed && !temp.contains(target)) {
					// 无法优化,新增一个进来
					optimizeGroupsIt.add(target);
					temp.add(target);
				}
				thisTargetNotNeed = false;

			}
		}

		return optimizeGroups;
	}

	/**
	 * @param targetGroups
	 *            13万条
	 * @return
	 */
	public List<List<Group>> optimizeGroupListNew(List<List<Group>> targetGroups) {
		List<List<Group>> optimizeGroups1 = new ArrayList<List<Group>>();
		Set<List<Group>> optimizeGroups2 = new LinkedHashSet<List<Group>>();

		ListIterator<List<Group>> targetGroupsIt = targetGroups.listIterator();

		while (targetGroupsIt.hasNext()) {
			List<Group> target = targetGroupsIt.next();
			targetGroupsIt.remove();
			if (optimizeGroup(targetGroups, target)) {
				optimizeGroups1.add(target);
				optimizeGroups2.add(target);
			}
			targetGroupsIt.add(target);
		}
		if (optimizeGroups1.size() != optimizeGroups2.size()) {
			Log4JUtils2.getLogger().error(
					"****** Error OptimizeGroups1.size != OptimizeGroups2.size; There are" + optimizeGroups1.size()
							+ " and " + optimizeGroups1.size());
		}

		return optimizeGroups1;
	}

	private boolean optimizeGroup(List<List<Group>> targetGroupsRemoved, List<Group> target) {
		boolean special = true;
		for (List<Group> itemGroup : targetGroupsRemoved) {
			if (itemGroup.containsAll(target)) {
				special = false;
				break;
			}
		}
		return special;

	}
}
