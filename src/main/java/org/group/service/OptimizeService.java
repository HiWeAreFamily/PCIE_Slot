package org.group.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
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

		try {
			List<List<Group>> targetGroups = maxTargetresult.getValue();
			List<List<Group>> optimizeGroups = optimizeGroupList(targetGroups);
			Log4JUtils2.getLogger().info(
					"====== 该条件下 Max=" + maxTargetresult.getKey() + ";压缩前数量:" + targetGroups.size() + "压缩后数量:"
							+ optimizeGroups.size());
			// Debug :显示优化前的TargetGroups list
			// int i = 0;
			// for (List<Group> target : targetresult.getValue()) {
			// Log4JUtils2.getLogger().info("<<<<<< " + (++i) + "" +
			// target);
			// }

			/* 呈现具体的rule */
			int ruleIndex = 0;
			for (List<Group> optimizeGroup : optimizeGroups) {
				ruleIndex++;
				MinMaxRule minMaxRule = new MinMaxRule(conditionIndex + "_" + ruleIndex, condition, optimizeGroup,
						maxTargetresult.getKey());
				Log4JUtils2.getLogger().info("====== Rule:" + minMaxRule.formatDebugRule());
				minMaxRules.add(minMaxRule);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log4JUtils2.getLogger().error("****** ERROR 异常信息：" + e.getMessage());
			// Log4JUtils2.getLogger().error("异常对象：" + e.);
			Log4JUtils2.getLogger().error("****** ERROR 调用堆栈：\n" + e.fillInStackTrace().getMessage());
			Log4JUtils2.getLogger().error("****** ERROR 触发方法：" + e.getStackTrace());
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

	@Deprecated
	private List<Group> optimizeGroup(List<Group> optimize, List<Group> target) {
		if (optimize.containsAll(target)) {
			return optimize;

		} else if (target.containsAll(optimize)) {
			return target;
		} else {
			return target;
		}

	}
}
