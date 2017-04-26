package org.group.service;

import java.util.ArrayList;
import java.util.List;

import org.group.bean.condition.COMPortBracket;
import org.group.bean.condition.Processer;
import org.group.bean.condition.RiserCard;
import org.group.bean.condition.SystemPlanar;
import org.group.bean.optimize.RuleCondition;

import utils.log.Log4JUtils2;

/**
 * @author fangh1
 *
 */
public class Conditions {
	public static List<Processer> processerList = new ArrayList<Processer>();
	public static List<RiserCard> riserCard1 = new ArrayList<RiserCard>();
	public static List<RiserCard> riserCard2 = new ArrayList<RiserCard>();
	public static List<SystemPlanar> systemPlanarList = new ArrayList<SystemPlanar>();
	public static List<COMPortBracket> comPortBracketList = new ArrayList<COMPortBracket>();

	static {

		Processer process1 = new Processer("1CPU", new Integer[] { 4 });
		Processer process2 = new Processer("2CPU", new Integer[] { 4, 5 });
		processerList.add(process1);
		processerList.add(process2);

		RiserCard R1 = new RiserCard("None", new Integer[] {});
		RiserCard A5FN = new RiserCard("A5FN", new Integer[] { 1, 3 });
		RiserCard A5FR = new RiserCard("A5FR", new Integer[] { 1 });
		RiserCard A5FP = new RiserCard("A5FP", new Integer[] { 1, 2, 3 });
		RiserCard A5FS = new RiserCard("A5FQ", new Integer[] { 1, 2 });
		riserCard1.add(R1);
		riserCard1.add(A5FN);
		riserCard1.add(A5FP);
		riserCard1.add(A5FR);
		riserCard1.add(A5FS);
		Log4JUtils2.getLogger().debug("<<<<<< RiserCard_1.size=" + riserCard1.size());

		RiserCard R2 = new RiserCard("None", new Integer[] {});
		RiserCard A5R5 = new RiserCard("A5R5", new Integer[] { 6, 8 });
		RiserCard A5R6 = new RiserCard("A5R6", new Integer[] { 6, 7, 8 });
		riserCard2.add(R2);
		riserCard2.add(A5R5);
		riserCard2.add(A5R6);
		Log4JUtils2.getLogger().debug("<<<<<< RiserCard_2.size=" + riserCard2.size());

		SystemPlanar planarATE4 = new SystemPlanar("8871_MB_ATE4");
		SystemPlanar planarAUAF = new SystemPlanar("8871_MB_AUAF");
		systemPlanarList.add(planarATE4);
		systemPlanarList.add(planarAUAF);

		COMPortBracket bracketNone = new COMPortBracket("None", new Integer[] {});
		COMPortBracket bracket = new COMPortBracket("A5AN", new Integer[] { 5 });
		comPortBracketList.add(bracketNone);
		comPortBracketList.add(bracket);

	}

	public static RuleCondition createRuleCondition(Processer prosesser, RiserCard riserCard_1, RiserCard riserCard_2,
			SystemPlanar planar, COMPortBracket comPortBracket) {
		// 条件排列
		List<String> conditionGroup = new ArrayList<String>();
		List<Integer> conditionQTY = new ArrayList<Integer>();
		if ("1CPU".equals(prosesser.getFc())) {
			conditionGroup.add("8871_PROC_ALL");
			conditionQTY.add(1);
		} else {
			conditionGroup.add("8871_PROC_ALL");
			conditionQTY.add(2);
		}
		switch (riserCard_1.getFc()) {
		case "None":
			conditionGroup.add("8871_PCI_Riser1");
			conditionQTY.add(0);
			break;
		case "A5FN":
			conditionGroup.add("8871_A5FN");
			conditionQTY.add(1);
			break;
		case "A5FR":
			conditionGroup.add("8871_PCI_Riser_A5FR");
			conditionQTY.add(1);
			break;
		case "A5FP":
			conditionGroup.add("8871_PCI_Riser_A5FP");
			conditionQTY.add(1);
			break;
		case "A5FQ":
			conditionGroup.add("8871_PCI_Riser_A5FQ");
			conditionQTY.add(1);
			break;
		default:
			Log4JUtils2.getLogger().error("******");
		}
		switch (riserCard_2.getFc()) {
		case "None":
			conditionGroup.add("8871_PCI_Riser2");
			conditionQTY.add(0);
			break;
		case "A5R5":
			conditionGroup.add("8871_A5R5");
			conditionQTY.add(1);
			break;
		case "A5R6":
			conditionGroup.add("8871_A5R6");
			conditionQTY.add(1);
			break;
		default:
			Log4JUtils2.getLogger().error("******");
		}
		switch (planar.getFc()) {
		case "8871_MB_ATE4":
			conditionGroup.add("8871_MB_ATE4");
			conditionQTY.add(1);
			break;
		case "8871_MB_AUAF":
			conditionGroup.add("8871_MB_AUAF");
			conditionQTY.add(1);
			break;
		default:
			Log4JUtils2.getLogger().error("******");
		}

		switch (comPortBracket.getFc()) {
		case "None":
			conditionGroup.add("8871_A5AN");
			conditionQTY.add(0);
			break;
		case "A5AN":
			conditionGroup.add("8871_A5AN");
			conditionQTY.add(1);
			break;
		default:
			Log4JUtils2.getLogger().error("******");
		}
		RuleCondition ruleCondition = new RuleCondition(conditionGroup, conditionQTY);
		return ruleCondition;

	}
}
