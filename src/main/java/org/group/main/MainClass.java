package org.group.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.group.bean.condition.COMPortBracket;
import org.group.bean.condition.Processer;
import org.group.bean.condition.RiserCard;
import org.group.bean.condition.SystemPlanar;
import org.group.bean.optimize.RuleCondition;
import org.group.bean.rule.FixedGroup;
import org.group.bean.rule.Group;
import org.group.bean.rule.MinMaxRule;
import org.group.bean.rule.TargetGroup;
import org.group.service.CaculatorService;
import org.group.service.CombinationService;
import org.group.service.Conditions;
import org.group.service.OptimizeService;
import org.group.service.ReadExcelGetGroupService;

import utils.log.Log4JUtils2;

/**
 * @author Administrator
 * 
 *         核心思想 :区分不同范围级别的Group : fixed-->fixed+dynamic-->dynamic+dynamic
 */
public class MainClass {
	public static List<FixedGroup> fixedGroups = new ArrayList<FixedGroup>();

	public static File sourceFile = new File("D:\\MT\\8871\\8871_T3_v1.036.xlsx");

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("start...");

		// 1.加载数据源 .获得Group
		ReadExcelGetGroupService getGroupService = new ReadExcelGetGroupService();
		getGroupService.getGroupsFormXlsx(sourceFile);

		// 2.所有的摆列组合
		List<Group> allGroups = new ArrayList<Group>();
		allGroups.addAll(getGroupService.getFixedGroups());
		allGroups.addAll(getGroupService.getDynamicGroups());
		Log4JUtils2.getLogger().info("====== 排列组合Gourp.size=" + allGroups.size());

		Group[] FDGroup = allGroups.toArray(new Group[allGroups.size()]);
		CombinationService.combiantion(FDGroup);
		Log4JUtils2.getLogger().info("====== 排列组合Gourp.size=" + CombinationService.resultGroupList.size());

		// 3.条件排列组合(重点在于两个CPU) --->演示两个CPU的情况

		// 4.各个Group在当前条件下的MAX,同时组织结构准备压缩
		Vector<MinMaxRule> minMaxRules = new Vector<MinMaxRule>();
		// TODO 可以优化Value
		Map<RuleCondition, Map<Integer, List<List<Group>>>> ruleMap = new HashMap<RuleCondition, Map<Integer, List<List<Group>>>>();
		int conditionCount = 0;
		int ruleIndexAll = 0;
		CaculatorService caculatorService = new CaculatorService();

		for (Processer prosesser : Conditions.processerList) {
			for (RiserCard riserCard_1 : Conditions.riserCard1) {
				for (RiserCard riserCard_2 : Conditions.riserCard2) {
					for (SystemPlanar planar : Conditions.systemPlanarList) {
						// TODO Lemon confirm? 1cpu allow A5AN
						for (COMPortBracket comPortBracket : Conditions.comPortBracketList) {
							conditionCount++;

							RuleCondition ruleCondition = Conditions.createRuleCondition(prosesser, riserCard_1,
									riserCard_2, planar, comPortBracket);

							Map<Integer, List<List<Group>>> targetResult = new HashMap<Integer, List<List<Group>>>();
							for (int i = 0; i < 10; i++) {
								targetResult.put(i, new ArrayList<List<Group>>());
							}
							// 5.真枪实干->Max
							for (List<Group> groups : CombinationService.resultGroupList) {
								ruleIndexAll++;
								/*
								 * [A].commont : 为了方便测试动态group if (group
								 * instanceof DynamicGroup) { int max =
								 * caculatorService.calculatorMax(prosesser,
								 * riserCard_1, riserCard_2, planar,
								 * comPortBracket, groups); }
								 */

								int max = caculatorService.calculatorMax(prosesser, riserCard_1, riserCard_2, planar,
										comPortBracket, groups);

								/*
								 * [B].性能太低,List放1600万条数据不现实; MinMaxRule
								 * minMaxRule = new MinMaxRule(ruleIndex,
								 * conditionGroup, conditionQTY, groups, max);
								 * Log4JUtils2.
								 * getLogger().info(minMaxRule.formatRule());
								 * minMaxRules.add(minMaxRule);
								 */

								targetResult.get(max).add(groups);

								if (ruleIndexAll % 100000 == 0) {
									System.out.println(ruleIndexAll);
									// break;
								}
							}
							ruleMap.put(ruleCondition, targetResult);
						}
					}

				}
			}

		}
		Log4JUtils2.getLogger().info("====== 组合条件的种数:" + conditionCount);
		Log4JUtils2.getLogger().info("====== RuleIndex :" + ruleIndexAll);
		// 5.压缩算法+多线程优化性能
		Log4JUtils2.getLogger().info("====== 需要优化RuleMap size:" + ruleMap.size());
		int conditionIndex = 0;
		int ruleIndex = 0;
		ThreadPoolExecutor executorService = new ThreadPoolExecutor(100, 150, 10, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(1200), new ThreadPoolExecutor.AbortPolicy());
		
		
		for (Entry<RuleCondition, Map<Integer, List<List<Group>>>> entry : ruleMap.entrySet()) {
			Log4JUtils2.getLogger().info("===== Start 第" + (++conditionIndex) + "组;\t条件为: " + entry.getKey());

			Map<Integer, List<List<Group>>> maxAndTargetResults = entry.getValue();

			for (Entry<Integer, List<List<Group>>> maxAndTargetResult : maxAndTargetResults.entrySet()) {
				
				if (maxAndTargetResult.getValue().size() == 0) {
					Log4JUtils2.getLogger().info("====== 该条件下 Max=" + maxAndTargetResult.getKey() + "的Rule 不存在");
				} else {
					/* ====================多线程处理==================== */
					OptimizeService command = new OptimizeService(minMaxRules, entry.getKey(), conditionIndex,
							maxAndTargetResult);
					executorService.execute(command);
				}
			}
		}
		// 等待所有线程完成,并获取当前状态;
		executorService.shutdown();
		while (true) {
			if (executorService.isTerminated()) {
				System.out.println("线程结束了！");
				break;
			} else {
				Log4JUtils2.getLogger().info("====== 执行线程数:" + executorService.getActiveCount());
				Log4JUtils2.getLogger().info("====== 队列中排队的任务的数量:" + executorService.getQueue().size());
//				Log4JUtils2.getLogger().info(
//						"====== 队列中排队的任务的数量:" + (1200 - executorService.getQueue().remainingCapacity()));
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Thread.sleep(10000);
		// 6.展示结果
		Set<TargetGroup> setTargetGroups = new LinkedHashSet<TargetGroup>();
		Log4JUtils2.getLogger().info("======");
		Log4JUtils2.getLogger().info("======");
		Log4JUtils2.getLogger().info("======");
		Log4JUtils2.getLogger().info("======");
		Log4JUtils2.getLogger().info("====== 优化后的Rule size: " + minMaxRules.size());
		for (MinMaxRule minMaxRule : minMaxRules) {
			setTargetGroups.add(minMaxRule.getTargetGroup());
		}
		Log4JUtils2.getLogger().info("====== 需要建立TargetGroup.size: " + setTargetGroups.size());

		for (MinMaxRule minMaxRule : minMaxRules) {
			setTargetGroups.add(minMaxRule.getTargetGroup());
			Log4JUtils2.getLogger().info(minMaxRule.formatRealRule());
		}
		for (TargetGroup targetGroup : setTargetGroups) {
			Log4JUtils2.getLogger().info(targetGroup);
		}
		Log4JUtils2.getLogger().info("======全部结束!");
	}
}
