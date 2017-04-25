package org.group.bean.optimize;

import java.util.ArrayList;
import java.util.List;

public class RuleCondition {
	List<String> conditionGroup;
	List<Integer> conditionQTY;

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

	public RuleCondition(List<String> conditionGroup, List<Integer> conditionQTY) {
		super();
		this.conditionGroup = conditionGroup;
		this.conditionQTY = conditionQTY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RuleCondition [conditionGroup=" + conditionGroup + ", conditionQTY=" + conditionQTY + "]";
	}

}
