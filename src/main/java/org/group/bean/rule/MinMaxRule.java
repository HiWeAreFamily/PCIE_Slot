package org.group.bean.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.message.Message;
import org.group.bean.optimize.RuleCondition;

import utils.log.Log4JUtils2;

public class MinMaxRule implements Comparable<MinMaxRule> {
	// rule rule_8871_SLOT_0416_8 extends MinMax {
	// MultipleConditionalGroups = true;
	// ConditionGroup =
	// 8871_PROC_ALL,8871_PCI_Riser_A5FP,8871_A5R6,8871_MB_ATE4;
	// ConditionQualifier = Equal,Equal,Equal,>;
	// ConditionQTY = 2,1,1,0;
	// TargetGroup = 8871_SLOT_0416;
	// Max = 4;
	// ProductPUIDs = 106404;
	// }
	/**
	 * prosesser, riserCard_1, riserCard_2, planar, comPortBracket;
	 * 
	 * 条件一定是5个; 即使是 1,0,0,0,0;
	 */
	private String index;
	private String MultipleConditionalGroups = "true";
	private List<String> conditionGroup = new ArrayList<String>(5);
	private String conditionQualifier = "Equal,Equal,Equal,Equal,Equal";
	private List<Integer> conditionQTY = new ArrayList<Integer>(5);
	private Integer max;
	private TargetGroup targetGroup;
	private String productId = "106404";

	/**
	 * More information about this rule;
	 * 
	 * @return
	 */
	public String formatDebugRule() {
		return "\r\nrule rule_8871_SLOT_" + index + " extends MinMax {\r\n" + "\tMultipleConditionalGroups=" + MultipleConditionalGroups
		        + ";\r\n\tConditionGroup=" + formatList(conditionGroup) + ";\r\n\tConditionQualifier=" + conditionQualifier + ";\r\n\tConditionQTY="
		        + formatList(conditionQTY) + ";\r\n\tTargetGroup=" + targetGroup.key + ";\r\n\tTargetGroup=" + formatList(targetGroup.getPuids())
		        + ";\r\n\tMax=" + max + ";\r\n\tProductId=" + productId + ";\r\n}\r\n";
	}

	/**
	 * @return Final Rule
	 */
	public String formatRealRule() {
		return "\r\nrule rule_8871_SLOT_" + index + " extends MinMax {\r\n" + "\tMultipleConditionalGroups=" + MultipleConditionalGroups
		        + ";\r\n\tConditionGroup=" + formatList(conditionGroup) + ";\r\n\tConditionQualifier=" + conditionQualifier + ";\r\n\tConditionQTY="
		        + formatList(conditionQTY) + ";\r\n\tTargetGroup=" + targetGroup.key + ";\r\n\tMax=" + max + ";\r\n\tProductId=" + productId
		        + ";\r\n}\r\n";
	}

	/*
	 * public MinMaxRule(int index, String[] conditionGroup, Integer[]
	 * conditionQTY, int max) { super(); this.index = index; conditionGroup =
	 * conditionGroup; conditionQTY = conditionQTY; this.max = max; }
	 */

	public MinMaxRule(String index, List<String> conditionGroup, List<Integer> conditionQTY, List<Group> targetGroup, Integer max) {
		super();
		this.index = index;
		this.conditionGroup = conditionGroup;
		this.conditionQTY = conditionQTY;
		this.targetGroup = getFcsFromGroup(targetGroup);
		this.max = max;
	}

	public MinMaxRule(String index, RuleCondition condition, List<Group> optimizeGroup, Integer max) {
		this(index, condition.getConditionGroup(), condition.getConditionQTY(), optimizeGroup, max);
	}

	public TargetGroup getFcsFromGroup(List<Group> groups) {
		String key = "";
		List<String> fcs = new ArrayList<String>();
		Log4JUtils2.getLogger().debug("<<<<<<" + groups);
		for (Group group : groups) {
			if (group instanceof FixedGroup) {
				key += "_FixedGroup" + group.getKey().replace(",", "").replace("[", "").replace("]", "").replace(";", "").replace(" ", "");
				fcs.addAll(group.getPuids());
			}
			if (group instanceof DynamicGroup) {
				key += "_DynamicGroup" + group.getKey().replace(",", "").replace("[", "").replace("]", "").replace(";", "").replace(" ", "");
				fcs.addAll(group.getPuids());
			}
		}
		Log4JUtils2.getLogger().debug("<<<<<< FCs:" + fcs);
		Collections.sort(groups);
		TargetGroup targetGroup = new TargetGroup("8871_SLOT_" + key, fcs);
		return targetGroup;

	}

	/**
	 * Remove [ ] from list.toString();
	 * 
	 * @param list
	 * @return
	 */
	private String formatList(List list) {
		String str = list.toString();

		return str.substring(1, str.length() - 1);
	}

	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}

	/**
	 * @return the multipleConditionalGroups
	 */
	public String getMultipleConditionalGroups() {
		return MultipleConditionalGroups;
	}

	/**
	 * @param multipleConditionalGroups
	 *            the multipleConditionalGroups to set
	 */
	public void setMultipleConditionalGroups(String multipleConditionalGroups) {
		MultipleConditionalGroups = multipleConditionalGroups;
	}

	/**
	 * @return the conditionGroup
	 */
	public List<String> getConditionGroup() {
		return conditionGroup;
	}

	/**
	 * @param conditionGroup
	 *            the conditionGroup to set
	 */
	public void setConditionGroup(List<String> conditionGroup) {
		this.conditionGroup = conditionGroup;
	}

	/**
	 * @return the conditionQualifier
	 */
	public String getConditionQualifier() {
		return conditionQualifier;
	}

	/**
	 * @param conditionQualifier
	 *            the conditionQualifier to set
	 */
	public void setConditionQualifier(String conditionQualifier) {
		this.conditionQualifier = conditionQualifier;
	}

	/**
	 * @return the conditionQTY
	 */
	public List<Integer> getConditionQTY() {
		return conditionQTY;
	}

	/**
	 * @param conditionQTY
	 *            the conditionQTY to set
	 */
	public void setConditionQTY(List<Integer> conditionQTY) {
		this.conditionQTY = conditionQTY;
	}

	/**
	 * @return the max
	 */
	public Integer getMax() {
		return max;
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMax(Integer max) {
		this.max = max;
	}

	/**
	 * @return the targetGroup
	 */
	public TargetGroup getTargetGroup() {
		return targetGroup;
	}

	/**
	 * @param targetGroup
	 *            the targetGroup to set
	 */
	public void setTargetGroup(TargetGroup targetGroup) {
		this.targetGroup = targetGroup;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId
	 *            the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Override
	public int compareTo(MinMaxRule o) {
		// TODO Auto-generated method stub
		return this.index.compareTo(o.index);
	}

}
