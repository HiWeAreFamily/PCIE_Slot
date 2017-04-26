package org.group.bean.optimize;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conditionGroup == null) ? 0 : conditionGroup.toString().hashCode());
		result = prime * result + ((conditionQTY == null) ? 0 : conditionQTY.toString().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RuleCondition other = (RuleCondition) obj;
		if (conditionGroup == null) {
			if (other.conditionGroup != null)
				return false;
		} else if (!conditionGroup.toString().equals(other.conditionGroup.toString()))
			return false;

		if (conditionQTY == null) {
			if (other.conditionQTY != null)
				return false;
		} else if (!conditionQTY.toString().equals(other.conditionQTY.toString()))
			return false;
		return true;
	}

}
