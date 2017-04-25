package org.group.bean.condition;

import java.util.Arrays;

public class RiserCard {
	public String Fc;
	public Integer[] slots;

	public RiserCard(String fc, Integer[] slots) {
		super();
		Fc = fc;
		this.slots = slots;
	}

	public RiserCard(Integer[] slots) {
		super();
		this.slots = slots;
	}

	public RiserCard() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "RiserCard [Fc=" + Fc + ", slots=" + Arrays.toString(slots) + "]";
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
