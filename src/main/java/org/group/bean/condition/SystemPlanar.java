package org.group.bean.condition;

import java.util.Arrays;

public class SystemPlanar {
	public String Fc;

	public SystemPlanar(String fc) {
		super();
		Fc = fc;
	}

	public SystemPlanar() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * @return the fc
	 */
	public String getFc() {
		return Fc;
	}

	/**
	 * @param fc the fc to set
	 */
	public void setFc(String fc) {
		Fc = fc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Process [Fc=" + Fc + "]";
	}
}
