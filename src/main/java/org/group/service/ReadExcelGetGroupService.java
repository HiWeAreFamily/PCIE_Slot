package org.group.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dcc.excel.util.Common;
import org.group.bean.rule.DynamicGroup;
import org.group.bean.rule.FixedGroup;
import org.group.bean.rule.PCISlot;

import utils.log.Log4JUtils2;

/**
 * 2003 HSSFWorkbook HSSFSheet HSSFRow HSSFCell
 * 
 * 2010 XSSFWorkbook XSSFSheet XSSFRow XSSFCell
 * 
 * @author fangh1
 *
 */
public class ReadExcelGetGroupService {
	private List<FixedGroup> fixedGroups = new ArrayList<FixedGroup>();
	private List<DynamicGroup> dynamicGroups = new ArrayList<DynamicGroup>();

	/**
	 * Read the Excel 2010
	 * 
	 * @param sourceFile
	 *            the path of the excel file
	 * @return
	 * @throws IOException
	 */
	public void getGroupsFormXlsx(File sourceFile) throws IOException {
		// 1.
		List<PCISlot> pciSlots = read2010Xlsx(sourceFile);
		// 2.
		analysisGroupFromPciSlots(pciSlots);

	}

	/**
	 * 获取初步数据
	 * 
	 * @param sourceFile
	 * @return
	 * @throws IOException
	 */
	public List<PCISlot> read2010Xlsx(File sourceFile) throws IOException {
		Log4JUtils2.getLogger().debug(Common.PROCESSING + sourceFile);
		InputStream is = new FileInputStream(sourceFile);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);

		PCISlot pciSlot = null;
		List<PCISlot> list = new ArrayList<PCISlot>();
		// 0.Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			Log4JUtils2.getLogger().debug("======getSheetName():" + xssfSheet.getSheetName());
			// 1.find SheetName
			if (xssfSheet != null && "Slots and Adapters (2)".equals(xssfSheet.getSheetName())) {
				// 2.Read the Row from line 7
				for (int rowNum = 6; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					if (xssfRow != null) {
						// SBB MFI FFI Option FeatureCode Description
						XSSFCell FC = xssfRow.getCell(4);
						XSSFCell Slots = xssfRow.getCell(8);
						XSSFCell FD = xssfRow.getCell(9);

						pciSlot = new PCISlot(getValue(FC), getValue(Slots), getValue(FD));
						Log4JUtils2.getLogger().debug(rowNum + " " + pciSlot);
						list.add(pciSlot);
					}
				}
			}
		}
		xssfWorkbook.close();
		Log4JUtils2.getLogger().info("======卡的总数:" + list.size());
		return list;
	}

	/**
	 * 根据初步数据分析Group
	 * 
	 * @param pciSlots
	 * @return
	 */
	public void analysisGroupFromPciSlots(List<PCISlot> pciSlots) {
		Map<String, Set<String>> fixedGroupsMap = new HashMap<String, Set<String>>();
		Map<String, Set<String>> dynamicGroupsMap = new HashMap<String, Set<String>>();

		Set<String> fixedkeySet = null;
		Set<String> dynamickeySet = null;
		// 1.解析数据Group
		for (PCISlot pciSlot : pciSlots) {
			fixedkeySet = fixedGroupsMap.keySet();
			dynamickeySet = dynamicGroupsMap.keySet();
			if ("F".equals(pciSlot.getFD()) || "D".equals(pciSlot.getFD())) {
				// (1).F静态
				if ("F".equals(pciSlot.getFD())) {
					String idSlots = getSlotsForRuleStringForGroup(pciSlot.getSlotRule());
					Log4JUtils2.getLogger().debug("<<<<<< FixedGroup-key:" + idSlots);
					if (fixedkeySet.contains(idSlots)) {
						fixedGroupsMap.get(idSlots).add(pciSlot.getFC().trim());
					} else {
						Set<String> listFc = new LinkedHashSet<String>();
						listFc.add(pciSlot.getFC().trim());
						fixedGroupsMap.put(idSlots, listFc);
					}

				}
				// (2).D动态
				if ("D".equals(pciSlot.getFD())) {
					String slotRule = pciSlot.getSlotRule();
					if (slotRule.contains("ATE4") && slotRule.contains("AUAF")) {
						// 2.1获得可以用的slot
						String idSlots = getSlotsForRuleStringForGroup(pciSlot.getSlotRule());
						idSlots = idSlots + "ATE4_AUAF;";
						Log4JUtils2.getLogger().debug("<<<<<< DynamicGroup-key:" + idSlots);
						if (dynamickeySet.contains(idSlots)) {
							dynamicGroupsMap.get(idSlots).add(pciSlot.getFC().trim());
						} else {
							Set<String> listFc = new LinkedHashSet<String>();
							listFc.add(pciSlot.getFC().trim());
							dynamicGroupsMap.put(idSlots, listFc);
						}
					} else if (slotRule.contains("AS95")) {
						String idSlots = getSlotsForRuleStringForGroup(pciSlot.getSlotRule());
						// idSlots = idSlots + "AS95;";
						idSlots = "Special_AS95;";
						Log4JUtils2.getLogger().debug("<<<<<< DynamicGroup-key:" + idSlots);
						if (dynamickeySet.contains(idSlots)) {
							dynamicGroupsMap.get(idSlots).add(pciSlot.getFC().trim());
						} else {
							Set<String> listFc = new LinkedHashSet<String>();
							listFc.add(pciSlot.getFC().trim());
							dynamicGroupsMap.put(idSlots, listFc);
						}

					} else if ((slotRule.contains("(Slots 3 only for FC A5FN)") || slotRule
							.contains("(Slots 3 only for A5FN)")) && !slotRule.contains("(Slots 8 only for FC A5R5)")) {
						String idSlots = getSlotsForRuleStringForGroup(pciSlot.getSlotRule());
						idSlots = idSlots + "A5FN;";
						Log4JUtils2.getLogger().debug("<<<<<< DynamicGroup-key:" + idSlots);
						if (dynamickeySet.contains(idSlots)) {
							dynamicGroupsMap.get(idSlots).add(pciSlot.getFC().trim());
						} else {
							Set<String> listFc = new LinkedHashSet<String>();
							listFc.add(pciSlot.getFC().trim());
							dynamicGroupsMap.put(idSlots, listFc);
						}

					} else if ((slotRule.contains("(Slots 3 only for FC A5FN)") || slotRule
							.contains("(Slots 3 only for A5FN)")) && slotRule.contains("(Slots 8 only for FC A5R5)")) {
						String idSlots = getSlotsForRuleStringForGroup(pciSlot.getSlotRule());
						idSlots = idSlots + "A5FN_A5R5;";
						Log4JUtils2.getLogger().debug("<<<<<< DynamicGroup-key:" + idSlots);
						if (dynamickeySet.contains(idSlots)) {
							dynamicGroupsMap.get(idSlots).add(pciSlot.getFC().trim());
						} else {
							Set<String> listFc = new LinkedHashSet<String>();
							listFc.add(pciSlot.getFC().trim());
							dynamicGroupsMap.put(idSlots, listFc);
						}

					} else {
						Log4JUtils2.getLogger().error("****** 未处理:" + pciSlot);
					}

				}
			} else {
				Log4JUtils2.getLogger().error("****** ERROR 数据格式异常!");
			}
		}

		Log4JUtils2.getLogger().info("==========");
		Log4JUtils2.getLogger().info("======Keyset:" + fixedkeySet.size());
		for (String key : fixedkeySet) {
			Log4JUtils2.getLogger().info("<<<<<<	fixedkeySet:" + key + "size:" + fixedGroupsMap.get(key));
		}
		Log4JUtils2.getLogger().info("==========");
		Log4JUtils2.getLogger().info("==========");
		Log4JUtils2.getLogger().info("======Keyset:" + dynamickeySet.size());
		for (String key : dynamickeySet) {
			Log4JUtils2.getLogger().info("<<<<<<	dynamickeySet:" + key + "size:" + dynamicGroupsMap.get(key));
		}
		Log4JUtils2.getLogger().info("==========");

		// 2.存储2种Group;

		for (Entry<String, Set<String>> entry : fixedGroupsMap.entrySet()) {
			Log4JUtils2.getLogger().debug("<<<<<< " + entry.getKey());
			List<Integer> idSlots = getSlotsArrayForRuleForGroup(entry.getKey());
			fixedGroups.add(new FixedGroup(entry.getKey(), idSlots, new ArrayList<String>(entry.getValue())));
		}

		for (Entry<String, Set<String>> entry : dynamicGroupsMap.entrySet()) {
			Log4JUtils2.getLogger().debug("<<<<<< " + entry.getKey());
			if ("Special_AS95;".equals(entry.getKey())) {
				dynamicGroups.add(new DynamicGroup(entry.getKey(), new ArrayList<String>(entry.getValue())));
			} else {
				List<Integer> idSlots = getSlotsArrayForRuleForGroup(entry.getKey());
				dynamicGroups.add(new DynamicGroup(entry.getKey(), idSlots, new ArrayList<String>(entry.getValue())));
			}
		}

	}

	/**
	 * getSlotsForRuleForFixedGroup
	 * 
	 * 1CPU : 4, 1, 2, 3 (Slots 3 only for FC A5FN); 2CPU : 4, 5, 1, 6, 2, 7. 3,
	 * 8 (Slots 8 only for FC A5R5)
	 * 
	 * @param pciSlotRule
	 * @return 4, 5, 1, 6, 2, 7. 3
	 */
	private String getSlotsForRuleStringForGroup(String pciSlotRule) {
		List<String> idSlots = new ArrayList<String>();
		String result = null;
		int index1 = pciSlotRule.lastIndexOf("2CPU");
		if (index1 == -1) {
			Log4JUtils2.getLogger().error("****** ERROR 数据格式异常!Fixed Group 没有2CPU描述!");
		} else {
			String slotFor2Cpu;
			int index2 = pciSlotRule.lastIndexOf("(");
			if (index2 != -1 && index2 > index1 + 4) {
				slotFor2Cpu = pciSlotRule.substring(index1 + 4, index2);
			} else {
				slotFor2Cpu = pciSlotRule.substring(index1 + 4);
			}
			Log4JUtils2.getLogger().debug("<<<<<< SlotFor2Cpu:" + slotFor2Cpu);
			Pattern p = Pattern.compile("[^0-9]");
			Matcher m = p.matcher(slotFor2Cpu);
			result = m.replaceAll("");
			for (int i = 0; i < result.length(); i++) {
				idSlots.add(result.substring(i, i + 1));
			}
			Collections.sort(idSlots);
		}
		return idSlots.toString();
	}

	/**
	 * getSlotsForRuleForFixedGroup
	 * 
	 * 1CPU : 4, 1, 2, 3 (Slots 3 only for FC A5FN); 2CPU : 4, 5, 1, 6, 2, 7. 3,
	 * 8 (Slots 8 only for FC A5R5)
	 * 
	 * @param pciSlotRule
	 *            经过处理的特殊规则的Slot描述;
	 * @return 4, 5, 1, 6, 2, 7. 3
	 */
	public List<Integer> getSlotsArrayForRuleForGroup(String slotFor2Cpu) {
		List<Integer> idSlots = new ArrayList<Integer>();
		//
		slotFor2Cpu = slotFor2Cpu.substring(0, 1 + slotFor2Cpu.indexOf("]"));

		Log4JUtils2.getLogger().debug("<<<<<< SlotFor2Cpu:" + slotFor2Cpu);
		Pattern p = Pattern.compile("[^0-9]");
		Matcher m = p.matcher(slotFor2Cpu);
		String result = m.replaceAll("");
		for (int i = 0; i < result.length(); i++) {
			String k1 = result.substring(i, i + 1);
			Integer k = Integer.valueOf(k1);
			idSlots.add(k);
		}
		Collections.sort(idSlots);
		Log4JUtils2.getLogger().debug("<<<<<< idSlots:" + idSlots);
		return idSlots;
	}

	/**
	 * @param xssfRow
	 * @return
	 */
	private String getValue(XSSFCell xssfRow) {
		if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
			double value = xssfRow.getNumericCellValue();
			if (value == (int) value) {
				return String.valueOf((int) value);
			} else {
				return String.valueOf(value);
			}
		} else {
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}

	/**
	 * @return the fixedGroups
	 */
	public List<FixedGroup> getFixedGroups() {
		return fixedGroups;
	}

	/**
	 * @param fixedGroups
	 *            the fixedGroups to set
	 */
	public void setFixedGroups(List<FixedGroup> fixedGroups) {
		this.fixedGroups = fixedGroups;
	}

	/**
	 * @return the dynamicGroups
	 */
	public List<DynamicGroup> getDynamicGroups() {
		return dynamicGroups;
	}

	/**
	 * @param dynamicGroups
	 *            the dynamicGroups to set
	 */
	public void setDynamicGroups(List<DynamicGroup> dynamicGroups) {
		this.dynamicGroups = dynamicGroups;
	}

}