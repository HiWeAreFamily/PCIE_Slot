package org.group.bean.condition;

import java.util.Arrays;

public class Processer {
	public String Fc;
	public Integer[] slots;

	public Processer(String fc, Integer[] slots) {
		super();
		Fc = fc;
		this.slots = slots;
	}

	public Processer(String fc) {
		super();
		Fc = fc;
	}

	public Processer(Integer[] slots) {
		super();
		this.slots = slots;
	}

	public Processer() {
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
		return "Process [Fc=" + Fc + ", slots=" + Arrays.toString(slots) + "]";
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
