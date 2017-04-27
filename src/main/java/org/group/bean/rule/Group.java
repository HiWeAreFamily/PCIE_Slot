/**
 * 
 */
package org.group.bean.rule;

import java.util.List;

/**
 * @author fangh1
 *
 */
public class Group implements Comparable<Group> {
	/**
	 * [1, 2, 3, 6, 7, 8]A5FN;
	 * 
	 * [1, 2, 3, 6, 7, 8];
	 */
	public String key;
	public List<String> puids;

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
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Group [key=" + key + "]";
	}

	public Group(String key, List<String> puids) {
		super();
		this.key = key;
		this.puids = puids;
	}

	public Group() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(Group o) {
		// TODO Auto-generated method stub
		int val = this.key.compareTo(o.key);
		return val;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof Group) {
			Group o = (Group) obj;
			return this.getKey().equals(o.getKey());
		} else {
			return false;
		}

	}

}
