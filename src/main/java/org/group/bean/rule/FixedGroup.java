package org.group.bean.rule;

import java.util.List;

/**
 * @author Administrator 固定的FixedGroup
 */
public class FixedGroup extends Group {
	public List<Integer> idSlots;

	public FixedGroup() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the idSlots
	 */
	public List<Integer> getIdSlots() {
		return idSlots;
	}

	/**
	 * @param idSlots
	 *            the idSlots to set
	 */
	public void setIdSlots(List<Integer> idSlots) {
		this.idSlots = idSlots;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FixedGroup [key=" + key + "]";
	}

	public FixedGroup(String key, List<Integer> idSlots, List<String> puids) {
		super();
		this.key = key;
		this.idSlots = idSlots;
		this.puids = puids;
	}

	// @Override
	// public String toString() {
	// return "FixedGroup [key=" + key + ", idSlots=" + idSlots + ", puids=" +
	// puids + "]";
	// }

}
