package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.group.bean.rule.DynamicGroup;
import org.group.bean.rule.Group;
import org.junit.Test;

public class Test1 {
	public static void main(String[] args) {
		System.out.println(17 + 136 + 680 + 2380 + 6188 + 12376 + 19448 + 24310 + 17 + 136 + 680 + 2380 + 6188 + 12376
				+ 19448 + 24310);

		List<Integer> idfixedSlotsforAs95;
		Integer[] slot = { 1, 2 };
		idfixedSlotsforAs95 = Arrays.asList(slot);
		;
		System.out.println(idfixedSlotsforAs95);

	}

	@Test
	public void TestInteger() {
		Integer i1 = new Integer(2);
		Integer i2 = new Integer(2);
		System.out.println(i1.equals(i2));
	}

	@Test
	public void testListContains() {
		List<String> lista = new ArrayList<String>();
		List<String> listb = new ArrayList<String>();
		lista.add("a");
		lista.add("b");
		lista.add("c");

		// listb.add("b");
		listb.add("a");
		listb.add("c");

		System.out.println(lista.containsAll(listb));
		System.out.println(listb.containsAll(lista));
	}

	@Test
	public void testListIterator() {
		List<String> l = new ArrayList<String>();
		l.add("aa");
		l.add("bb");
		l.add("cc");
		l.add("dd");
		ListIterator<String> iter = l.listIterator();
		System.out.println(l);
		while (iter.hasNext()) {
			String str = iter.next();

			iter.remove();
			System.out.println(l + " " + l.size());
			iter.add(str);
			System.out.println(l + " " + l.size());
			// iter.previous();
			// System.out.println("---" + str);

		}
		System.out.println(l);

		/*
		 * 迭代器用于while循环
		 * 
		 * Iterator iter = l.iterator();
		 * 
		 * while(iter.hasNext()){
		 * 
		 * String str = (String) iter.next();
		 * 
		 * System.out.println(str);
		 * 
		 * }
		 */
	}

	@Test
	public void testListIterator2() {
		List<List<Group>> targetGroups = new ArrayList<List<Group>>();
		Group g1 = new Group("123", Arrays.asList("1", "2", "3"));
		Group g2 = new Group("456", Arrays.asList("4", "5", "6"));
		Group g3 = new Group("789", Arrays.asList("4", "5", "6"));
		// Group G4 = g3;
		List<Group> list1 = new ArrayList<Group>();
		List<Group> list2 = new ArrayList<Group>();
		List<Group> list3 = new ArrayList<Group>();
		list1.add(g1);
		list1.add(g2);
		list1.add(g3);

		list2.add(g1);
		list2.add(g3);

		list3.add(g3);
		targetGroups.add(list3);
//		targetGroups.add(list2);
		targetGroups.add(list1);
		
		System.out.println(targetGroups.contains(list2));

		CopyOnWriteArrayList<List<Group>> targetGroupsCopy = new CopyOnWriteArrayList<List<Group>>(targetGroups);

		List<List<Group>> optimizeGroups1 = new ArrayList<List<Group>>();
		for (List<Group> target : targetGroupsCopy) {
			System.out.println("List is" + target);
			targetGroupsCopy.remove(target);
			if (optimizeGroup(targetGroupsCopy, target)) {
				optimizeGroups1.add(target);
			}
			targetGroupsCopy.add(target);
		}

		int i = targetGroupsCopy.size();
	}

	private boolean optimizeGroup(List<List<Group>> targetGroupsRemoved, List<Group> target) {
		boolean special = true;
		for (List<Group> itemGroup : targetGroupsRemoved) {
			if (itemGroup.containsAll(target)) {
				special = false;
				break;
			}
		}
		return special;

	}

	@Test
	public void addSet() {
		String a = new String("123hhhh");
		String b = new String("123hhhh");
		System.out.println(a.hashCode());
		System.out.println(b.hashCode());
		// hashCode
		for (int i = 0; i < 10; i++) {
			System.out.println(i);
		}
	}

	@Test
	public void TestSetofList() {

		Set<List<Group>> temp = new LinkedHashSet<List<Group>>();
		Group g1 = new Group("123", Arrays.asList("1", "2", "3"));
		Group g2 = new Group("456", Arrays.asList("4", "5", "6"));
		Group g3 = new Group("789", Arrays.asList("4", "5", "6"));
		// Group G4 = g3;
		List<Group> list1 = new ArrayList<Group>();
		List<Group> list2 = new ArrayList<Group>();
		List<Group> list3 = new ArrayList<Group>();
		list1.add(g1);
		list1.add(g2);
		list1.add(g3);

		list2.add(g1);
		list2.add(g3);
		// list2.add(G4);

		list3.add(g3);

		temp.add(list1);
		// temp.add(list2);
		System.out.println(temp.size());
		System.out.println(temp.size());
		System.out.println(temp.contains(list2));

		System.out.println(temp.size());

		System.out.println(list1.containsAll(list2));
		System.out.println(list2.containsAll(list1));

	}

	@Test
	public void testGroup() {
		List<Integer> ints = new ArrayList<Integer>();
		ints.add(1);
		ints.add(2);
		ints.add(3);
		DynamicGroup dynamicGroup = new DynamicGroup("123", ints, Arrays.asList("1", "2", "3"));
		List<Integer> intsss = new ArrayList<Integer>(dynamicGroup.getIdSlots());
		// intsss = dynamicGroup.getIdSlots();
		intsss.add(5);
		System.out.println(dynamicGroup.getIdSlots());
	}
}
