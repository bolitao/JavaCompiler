package xyz.bolitao.exp02.sff;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class SelectUtil {

	public static boolean isVnStart(TreeSet<Character> vnSet, String select) {
		if(vnSet.contains(select.charAt(0)))
			return true;
		return false;
	}
	
	public static boolean isVtStart(TreeSet<Character> vtSet, String select) {
		if(vtSet.contains(select.charAt(0)))
			return true;
		return false;
	}
	
	public static boolean isEmpty(String select) {
		if(select.charAt(0)=='ε')
			return true;
		return false;
	}
	
	/**
	 * 查找产生式
	 * @param selectMap
	 * @param peek 当前非终结符
	 * @param charAt 当前终结符
	 * @return
	 */
	public static String findUseExp(TreeMap<Character, HashMap<String, TreeSet<Character>>> selectMap, Character peek,
			char charAt) {
		try {
			HashMap<String, TreeSet<Character>> hashMap = selectMap.get(peek);//获取当前非终结符的hashMap
			Set<String> keySet = hashMap.keySet();//获取表达式
			for (String useExp : keySet) {//遍历一个个表达式 如有相同 select产生集 则返回 表达式
				TreeSet<Character> treeSet = hashMap.get(useExp);
				if (treeSet.contains(charAt)) {
					return useExp;
				}
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
	
}
