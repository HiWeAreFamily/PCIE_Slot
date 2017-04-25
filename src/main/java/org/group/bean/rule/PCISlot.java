package org.group.bean.rule;

/**
 * @author fangh1 原始数据
 *
 */
public class PCISlot {
	public String FC = "";
	public String SlotRule = "";
	public String FD = "";

	/**
	 * @return the fC
	 */
	public String getFC() {
		return FC;
	}

	/**
	 * @param fC
	 *            the fC to set
	 */
	public void setFC(String fC) {
		FC = fC;
	}

	/**
	 * @return the slotRule
	 */
	public String getSlotRule() {
		return SlotRule;
	}

	/**
	 * @param slotRule
	 *            the slotRule to set
	 */
	public void setSlotRule(String slotRule) {
		SlotRule = slotRule;
	}

	public PCISlot(String fC, String slotRule) {
		super();
		FC = fC;
		SlotRule = slotRule;
	}

	public PCISlot() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PCISlot [FC=" + FC + ", SlotRule=" + SlotRule + ", FD=" + FD + "]";
	}

	public PCISlot(String fC, String slotRule, String fD) {
		super();
		FC = fC;
		SlotRule = slotRule;
		FD = fD;
	}

	/**
	 * @return the fD
	 */
	public String getFD() {
		return FD;
	}

	/**
	 * @param fD
	 *            the fD to set
	 */
	public void setFD(String fD) {
		FD = fD;
	}

}
