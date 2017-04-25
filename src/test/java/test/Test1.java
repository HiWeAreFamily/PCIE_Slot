package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

public class Test1 {
	public static void main(String[] args) {
		System.out.println(17 + 136 + 680 + 2380 + 6188 + 12376 + 19448 + 24310 + 17 + 136 + 680 + 2380 + 6188 + 12376 + 19448 + 24310);

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
		List<String> l = new ArrayList();

		l.add("aa");

		l.add("bb");

		l.add("cc");

		for (ListIterator iter = l.listIterator(); iter.hasNext();) {
			String str = (String) iter.next();
			if ("bb".equals(str)) {
				iter.remove();
				iter.add("ddd");
			}

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
}
