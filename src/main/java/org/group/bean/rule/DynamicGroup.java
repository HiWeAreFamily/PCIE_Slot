package org.group.bean.rule;

import java.util.List;

public class DynamicGroup extends Group {

	private List<Integer> idSlots;
	public List<Integer> extraSlot;

	public DynamicGroup() {
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

	/**
	 * @return the extraSlot
	 */
	public List<Integer> getExtraSlot() {
		return extraSlot;
	}

	public DynamicGroup(String key, List<Integer> idSlots, List<Integer> extraSlot, List<String> puids) {
		super();
		this.key = key;
		this.idSlots = idSlots;
		this.extraSlot = extraSlot;
		this.puids = puids;
	}

	public DynamicGroup(String key, List<Integer> idSlots, List<String> puids) {
		super();
		this.key = key;
		this.idSlots = idSlots;
		this.puids = puids;
	}

	public DynamicGroup(String key, List<String> puids) {
		super();
		this.key = key;
		this.puids = puids;
	}

	/**
	 * @param extraSlot
	 *            the extraSlot to set
	 */
	public void setExtraSlot(List<Integer> extraSlot) {
		this.extraSlot = extraSlot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DynamicGroup [key=" + key + "]";
	}

}
