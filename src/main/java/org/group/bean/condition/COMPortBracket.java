package org.group.bean.condition;

import java.util.Arrays;

public class COMPortBracket {
	public String Fc;

	/**
	 * 会占用的Slot
	 */
	public Integer[] slots;

	public COMPortBracket(String fc) {
		super();
		Fc = fc;
	}

	public COMPortBracket() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "COMPortBracket [Fc=" + Fc + ", slots=" + Arrays.toString(slots) + "]";
	}

	public COMPortBracket(String fc, Integer[] slots) {
		super();
		Fc = fc;
		this.slots = slots;
	}

	/**
	 * @return the fc
	 */
	public String getFc() {
		return Fc;
	}

	/**
	 * @param fc
	 *            the fc to set
	 */
	public void setFc(String fc) {
		Fc = fc;
	}

	/**
	 * @return the slots
	 */
	public Integer[] getSlots() {
		return slots;
	}

	/**
	 * @param slots
	 *            the slots to set
	 */
	public void setSlots(Integer[] slots) {
		this.slots = slots;
	}

}
