package org.group.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.group.bean.condition.COMPortBracket;
import org.group.bean.condition.Processer;
import org.group.bean.condition.RiserCard;
import org.group.bean.condition.SystemPlanar;
import org.group.bean.rule.DynamicGroup;
import org.group.bean.rule.FixedGroup;
import org.group.bean.rule.Group;
import org.group.util.ListInteger;

import freemarker.core.ReturnInstruction.Return;
import utils.log.Log4JUtils2;

/**
 * @author fangh1 计算模块
 *
 */
public class CaculatorService {

	/**
	 * @param prosesser
	 * @param riserCard_1
	 * @param riserCard_2
	 * @param planar
	 * @param comPortBracket
	 * @param groups
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int calculatorMax(Processer prosesser, RiserCard riserCard_1, RiserCard riserCard_2, SystemPlanar planar, COMPortBracket comPortBracket,
	        List<Group> groups) {
		int max = 0;
		// 9,1,2,3,4,5,6,7,8
		List<Integer> provideSlots = new ArrayList<Integer>();
		provideSlots.add(9);
		// 1.当前条件总共提供了那些1,2,3槽位;
		// 1.1 -->CPU
		Integer[] prosesserSlots = prosesser.getSlots();
		List<Integer> cpuProvide = new ArrayList<Integer>(Arrays.asList(prosesserSlots));
		Log4JUtils2.getLogger().debug("<<<<<< CPUProvide:" + ListInteger.listToString(cpuProvide));
		provideSlots.addAll(cpuProvide);
		// 1.2 -->riserCard_1
		if (null != riserCard_1) {
			List<Integer> riser_1_Provide = new ArrayList<Integer>(Arrays.asList(riserCard_1.getSlots()));
			Log4JUtils2.getLogger().debug("<<<<<< Riser_1_Provide:" + ListInteger.listToString(riser_1_Provide));
			provideSlots.addAll(riser_1_Provide);
		}

		// 1.3 -->riserCard_2
		if (null != riserCard_2) {
			List<Integer> riser_2_Provide = new ArrayList<Integer>(Arrays.asList(riserCard_2.getSlots()));
			Log4JUtils2.getLogger().debug("<<<<<< Riser_2_Provide:" + ListInteger.listToString(riser_2_Provide));
			provideSlots.addAll(riser_2_Provide);
		}
		// 1.4 -->A5AN comPortBracket
		if (null != comPortBracket && "A5AN".equals(comPortBracket.getFc())) {
			List<Integer> a5anRemoveSlot = new ArrayList<Integer>(Arrays.asList(comPortBracket.getSlots()));
			Log4JUtils2.getLogger().debug("<<<<<< A5AN RemoveSlot:" + ListInteger.listToString(a5anRemoveSlot));
			provideSlots.removeAll((a5anRemoveSlot));
		}
		Log4JUtils2.getLogger().debug("====== ProvideSlots:" + ListInteger.listToString(provideSlots));

		// 2.当前条件下该组合可能占用那些1,3槽位;
		Set<Integer> cardSlotsFixedDynamic = new LinkedHashSet<Integer>();
		Set<Integer> cardSlotsFixed = new LinkedHashSet<Integer>();
		for (Group group : groups) {
			if (group instanceof FixedGroup) {
				List<Integer> idfixedSlots = ((FixedGroup) group).getIdSlots();
				cardSlotsFixed.addAll(idfixedSlots);
			}
		}
		Set<Integer> cardSlotsDynamic = new LinkedHashSet<Integer>();
		for (Group group : groups) {
			if (group instanceof DynamicGroup) {
				DynamicGroup dynamicGroup = (DynamicGroup) group;
				if ("Special_AS95;".equals(dynamicGroup.getKey())) {
					// A.AS95 在这个条件下: 可以放得slot
					// TODO lemon confirm
					List<Integer> idDynamicSlot = getSlotSpecial_AS95(prosesser, riserCard_1, riserCard_2, planar, comPortBracket);
					cardSlotsDynamic.addAll(idDynamicSlot);
				} else if (dynamicGroup.getKey().endsWith("A5FN;")) {
					// B.
					List<Integer> idDynamicSlot = dynamicGroup.getIdSlots();
					if (!"A5FN".equals(riserCard_1.getFc())) {
						// 不满足动态条件.删除动态slot;
						idDynamicSlot.remove(new Integer(3));
					}
					cardSlotsDynamic.addAll(idDynamicSlot);
				} else if (dynamicGroup.getKey().endsWith("A5FN_A5R5;")) {
					// C.
					// TODO lemon confirm : if no A5FN, could this type group
					// could install slot 3?
					List<Integer> idDynamicSlot = dynamicGroup.getIdSlots();
					if (!"A5FN".equals(riserCard_1.getFc())) {
						// 不满足动态条件.删除动态slot3;
						idDynamicSlot.remove(new Integer(3));
					}
					if (!"A5R5".equals(riserCard_2.getFc())) {
						// 不满足动态条件.删除动态slot8;
						idDynamicSlot.remove(new Integer(8));
					}
					cardSlotsDynamic.addAll(idDynamicSlot);
				} else if (dynamicGroup.getKey().endsWith("ATE4_AUAF;")) {
					// D.
					List<Integer> idDynamicSlot = dynamicGroup.getIdSlots();
					if ("8871_MB_ATE4".equals(planar.getFc())) {
						// 不满足动态条件.删除动态slot;
						idDynamicSlot.remove(new Integer(5));
					}
					cardSlotsDynamic.addAll(idDynamicSlot);
				} else {
					Log4JUtils2.getLogger().error("****** ERROR:Some DynamicGroup have not done;" + dynamicGroup);
				}
			}
		}
		cardSlotsFixedDynamic.addAll(cardSlotsFixed);
		cardSlotsFixedDynamic.addAll(cardSlotsDynamic);

		// 3.计算最大值
		for (Integer slot : cardSlotsFixedDynamic) {
			if (provideSlots.contains(slot)) {
				max++;
			}
		}
		return max;
	}

	// switch (riserCard_1.getFc()) {
	// case "1CPU":
	// provideSlots.addAll(c);
	// break;
	// case "2CPU":
	//
	// break;
	// default:
	// Log4JUtils2.getLogger().error("******");
	// }

	/**
	 * @param prosesser
	 * @param riserCard_1
	 * @param riserCard_2
	 * @param planar
	 * @param comPortBracket
	 * @return
	 */
	public List<Integer> getSlotSpecial_AS95(Processer prosesser, RiserCard riserCard_1, RiserCard riserCard_2, SystemPlanar planar,
	        COMPortBracket comPortBracket) {
		// 1CPU : 1, 2 (A5FP/A5FQ ) / 3.4 ( A5FN )
		// 2CPU : 1, 2, 6, 7 ( riser 1: A5FP/A5FQ & riser 2 : A5R6)
		// 3.4.5.8 (riser 1: A5FN& riser 2 :A5R5)
		// 2CPU:
		// 1.2( riser 1: A5FP/A5FQ, rise 2 : A5R6 or A5R5 or not select )/
		// 6.7 (riser 1: A5FP/A5FQ and riser 2 : A5R6)/
		// 3.4 (riser 1: A5FN, rise 2 : A5R6 or A5R5 or not select)/
		// 5.8(riser 1: A5FN and riser 2 :A5R5)
		List<Integer> idfixedSlotsforAs95 = new ArrayList<Integer>();
		if ("1CPU".equals(prosesser.getFc())) {
			if ("A5FP".equals(riserCard_1.getFc()) || "A5FQ".equals(riserCard_1.getFc())) {
				Integer[] slot = { 1, 2 };
				idfixedSlotsforAs95 = Arrays.asList(slot);
			}
			if ("A5FN".equals(riserCard_1.getFc())) {
				Integer[] slot = { 3, 4 };
				idfixedSlotsforAs95 = Arrays.asList(slot);
			}

		} else if ("2CPU".equals(prosesser.getFc())) {
			if ("A5FP".equals(riserCard_1.getFc()) || "A5FQ".equals(riserCard_1.getFc())) {
				if ("A5R6".equals(riserCard_2.getFc())) {
					Integer[] slot = { 1, 2, 6, 7 };
					idfixedSlotsforAs95 = Arrays.asList(slot);
				} else {
					Integer[] slot = { 1, 2 };
					idfixedSlotsforAs95 = Arrays.asList(slot);
				}

			}
			if ("A5FN".equals(riserCard_1.getFc())) {
				if ("A5R5".equals(riserCard_2.getFc())) {
					Integer[] slot = { 3, 4, 5, 8 };
					idfixedSlotsforAs95 = Arrays.asList(slot);
				} else {
					Integer[] slot = { 3, 4 };
					idfixedSlotsforAs95 = Arrays.asList(slot);
				}
			}
		} else {
			Log4JUtils2.getLogger().error("******");
		}
		return idfixedSlotsforAs95;
	}
}
