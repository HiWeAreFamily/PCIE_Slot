package org.group.bean.rule;

import java.util.ArrayList;
import java.util.List;

public class TargetGroup {
	public String key;
	public List<String> puids;

	public TargetGroup(String key, List<String> puids) {
		super();
		this.key = key;
		this.puids = puids;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the puids
	 */
	public List<String> getPuids() {
		return puids;
	}

	/**
	 * @param puids
	 *            the puids to set
	 */
	public void setPuids(List<String> puids) {
		this.puids = puids;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((puids == null) ? 0 : puids.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Group)) {
			return false;
		}
		Group other = (Group) obj;
		if (key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!key.equals(other.key)) {
			return false;
		}
		if (puids == null) {
			if (other.puids != null) {
				return false;
			}
		} else if (!puids.equals(other.puids)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// group 8871_SLOT_451627 extends ProductGroup {
		// groupPuids
		// =100975,43947,5295,47610,115410,54028,115411;
		// description = "PCIe Adapters";
		// }
		return "group 8871_SLOT_" + key + " +extends ProductGroup {\r\n" + "\tgroupPuids=" + formatList(puids)
		        + ";\r\n\tdescription = \"PCIe Adapters\";\r\n}";
	}

	private String formatList(List<String> list) {
		for(int i=0;i<list.size();i++){
			list.set(i, "'"+list.get(i)+"'");
		}
		String str = list.toString();

		return str.substring(1, str.length() - 1);
	}

	public static void main(String[] args) {
		List<String> puid1 = new ArrayList<String>();
		for (int i = 1000; i < 1019; i++) {
			puid1.add(i + "");
		}

		TargetGroup t = new TargetGroup("qwiueykljasdflkjq3y4re", puid1);
		System.out.println(t.toString());

	}
}
