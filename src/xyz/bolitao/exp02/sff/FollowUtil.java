package xyz.bolitao.exp02.sff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * 
 * @author 吴俭
 * 统一说明 正在寻找的为Follow(A)
 */
public class FollowUtil {

	/**
	 * 形式为B->bA的 因为A为B的后缀 所以 B->bA=follow(B) 等同于找B的follow
	 * @param itemCharStr
	 * @param a
	 * @return
	 */
	public static boolean containsbA(TreeSet<Character> vnSet, String itemCharStr, Character a) {
		String str = a.toString();
		String lastStr = itemCharStr.substring(itemCharStr.length()-1);
		if(str.equals(lastStr))
			return true;
		return false;
	}
	
	/**
	 * 形式为AB的 即A后面为非终结符 则AB=first(B)
	 * @param itemCharStr
	 * @param a
	 * @return
	 */
	public static boolean containsAB(TreeSet<Character> vnSet, String itemCharStr, Character a) {
		String str = a.toString();
		if(itemCharStr.contains(str)) {
			int index = itemCharStr.indexOf(str);
			String findStr;
			try {
				findStr = itemCharStr.substring(index + 1, index + 2);
			} catch (Exception e) {
				return false;
			}
			if(vnSet.contains(findStr.charAt(0))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 形式为Ab的 即A后面为终结符 则Ab=b
	 * @param itemCharStr
	 * @param a
	 * @return
	 */
	public static boolean containsAb(TreeSet<Character> vtSet, String itemCharStr, Character a) {
		String str = a.toString();
		if(itemCharStr.contains(str)) {
			int index = itemCharStr.indexOf(str);
			String findStr;
			try {
				findStr = itemCharStr.substring(index + 1, index + 2);
			} catch (Exception e) {
				return false;
			}
			if(vtSet.contains(findStr.charAt(0))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 形如AB 而B存在为ε的 比如B=[+TC,ε]
	 * @param itemCharStr
	 * @param a
	 * @param expressionMap
	 * @return
	 */
	public static boolean containsbABIsNull(TreeSet<Character> vnSet, String itemCharStr, Character a,
			HashMap<Character, ArrayList<String>> expressionMap) {
		String str = a.toString();
		if (containsAB(vnSet, itemCharStr, a)) {//先判断是不是 AB的格式
			Character alastChar = getAlastChar(itemCharStr, a);
			ArrayList<String> arrayList = expressionMap.get(alastChar);
			if (arrayList.contains("ε")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取到A的后面的第一个字符
	 * @param itemCharStr
	 * @param a
	 * @return
	 */
	public static Character getAlastChar(String itemCharStr, Character a) {
		String str = a.toString();
		if(itemCharStr.contains(str)) {
			int index = itemCharStr.indexOf(str);
			String findStr;
			try {
				findStr = itemCharStr.substring(index + 1, index + 2);
			} catch (Exception e) {
				return null;
			}
			return findStr.charAt(0);
		}
		return null;
	}
	
}
