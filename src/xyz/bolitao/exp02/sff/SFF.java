package xyz.bolitao.exp02.sff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class SFF implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 分析表
	 */
	private String[][] analyzeTable;

	/**
	 * Select集合
	 */
	private TreeMap<Character, HashMap<String, TreeSet<Character>>> selectMap;
	/**
	 * LL（1）文法产生集合
	 */
	private ArrayList<String> gsArray;
	/**
	 * 表达式集合
	 */
	private HashMap<Character, ArrayList<String>> expressionMap;
	// 好处在于可以把相类似的加到一起去
	/**
	 * 开始符
	 */
	private Character s;
	/**
	 * Vn非终结符集合
	 */
	private TreeSet<Character> vnSet;
	/**
	 * Vt终结符集合
	 */
	private TreeSet<Character> vtSet;
	/**
	 * First集合
	 */
	private HashMap<Character, TreeSet<Character>> firstMap;
	/**
	 * Follow集合
	 */
	private HashMap<Character, TreeSet<Character>> followMap;

	public String[][] getAnalyzeTable() {
		return analyzeTable;
	}

	public void setAnalyzeTable(String[][] analyzeTable) {
		this.analyzeTable = analyzeTable;
	}

	public TreeMap<Character, HashMap<String, TreeSet<Character>>> getSelectMap() {
		return selectMap;
	}

	public void setSelectMap(TreeMap<Character, HashMap<String, TreeSet<Character>>> selectMap) {
		this.selectMap = selectMap;
	}

	public HashMap<Character, ArrayList<String>> getExpressionMap() {
		return expressionMap;
	}

	public void setExpressionMap(HashMap<Character, ArrayList<String>> expressionMap) {
		this.expressionMap = expressionMap;
	}

	public HashMap<Character, TreeSet<Character>> getFirstMap() {
		return firstMap;
	}

	public void setFirstMap(HashMap<Character, TreeSet<Character>> firstMap) {
		this.firstMap = firstMap;
	}

	public HashMap<Character, TreeSet<Character>> getFollowMap() {
		return followMap;
	}

	public void setFollowMap(HashMap<Character, TreeSet<Character>> followMap) {
		this.followMap = followMap;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setGsArray(ArrayList<String> gsArray) {
		this.gsArray = gsArray;
	}

	public Character getS() {
		return s;
	}

	public void setS(Character s) {
		this.s = s;
	}

	public TreeSet<Character> getVnSet() {
		return vnSet;
	}

	public void setVnSet(TreeSet<Character> vnSet) {
		this.vnSet = vnSet;
	}

	public TreeSet<Character> getVtSet() {
		return vtSet;
	}

	public void setVtSet(TreeSet<Character> vtSet) {
		this.vtSet = vtSet;
	}

	public SFF() {
		super();
		gsArray = new ArrayList<String>();
		vnSet = new TreeSet<Character>();
		vtSet = new TreeSet<Character>();
		firstMap = new HashMap<Character, TreeSet<Character>>();
		followMap = new HashMap<Character, TreeSet<Character>>();
		selectMap = new TreeMap<Character, HashMap<String, TreeSet<Character>>>();
	}

	public ArrayList<String> getGsArray() {

		try {
			File file = new File("D:\\gs.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String str = null;
			str = br.readLine();
			s = str.charAt(0);
			//开始符
			gsArray.add(str);
			while ((str = br.readLine()) != null) {
				gsArray.add(str);
			}
			return gsArray;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return gsArray;
		} catch (IOException e) {
			e.printStackTrace();
			return gsArray;
		}

	}

	/**
	 * 获取终结符集合与非终结符集合
	 */
	public void getVnVt() {
		// 非终结符集合vnSet
		for (String gsItem : gsArray) {
			String[] vnt = gsItem.split("->");
			String vn = vnt[0];
			vnSet.add(vn.charAt(0));
		}
		// 终结符集合vtSet
		for (String gsItem : gsArray) {
			String[] vnt = gsItem.split("->");
			String vt = vnt[1];
			for (int i = 0; i < vnt[1].length(); i++) {
				if (!vnSet.contains(vt.charAt(i)))
					vtSet.add(vt.charAt(i));
			}
		}
	}

	/**
	 * 初始化表达式集合
	 */
	public void initExpressionMaps() {
		expressionMap = new HashMap<Character, ArrayList<String>>();
		for (String gsItem : gsArray) {
			String[] vnt = gsItem.split("->");
			String vn = vnt[0];
			String vt = vnt[1];
			if (!expressionMap.containsKey(vn.charAt(0))) {
				ArrayList<String> expArr = new ArrayList<String>();
				expArr.add(vt);
				expressionMap.put(vn.charAt(0), expArr);
			} else {
				ArrayList<String> expArr = expressionMap.get(vn.charAt(0));
				expArr.add(vt);
				expressionMap.put(vn.charAt(0), expArr);
			}
		}
	}

	/**
	 * 获取First集合
	 */
	public void getFirst() {
		Iterator<Character> ite = vnSet.iterator();
		// 获取vn非终结符的内容 以便去求每个的First
		while (ite.hasNext()) {
		    // 找出所有非终结符的First
			Character charItem = ite.next();
			ArrayList<String> itemList = expressionMap.get(charItem);
			for (String itemL : itemList) {
			    // 可以将一个非终结符的first全部找出来
				boolean ifBreak = false;
				// 表面多此一举 但为了防止出错 还是写一下
				for (int i = 0; i < itemL.length(); i++) {
					char item = itemL.charAt(i);
					TreeSet<Character> itemSet = firstMap.get(charItem);
					if (null == itemSet) {
						itemSet = new TreeSet<Character>();
						// 该非终结符还没有过first 则创建
					}
					ifBreak = First(itemSet, charItem, item);
					// 绝对会返回true 已经找到first且添加到firstMap了
					if (ifBreak) {
						break;
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param itemSet
	 *            first集
	 * @param charItem
	 *            非终结符
	 * @param item
	 *            推导式中的一个
	 */
	public boolean First(TreeSet<Character> itemSet, Character charItem, char item) {
		if (item == 'ε' || vtSet.contains(item)) {
		    // 如果是 终结符或者是空串 终止然后将item加入到first集里面去
			itemSet.add(item);
			firstMap.put(charItem, itemSet);
			// charItem 的 first集 为itemSet
			return true;
		} else if (vnSet.contains(item)) {
		    // 如果是非终结符 就继续找First集 递归
			ArrayList<String> itemList = expressionMap.get(item);
			for (int i = 0; i < itemList.size(); i++) {
			    // List是有序的 所以此时用List 为了找item的first 则要遍历表达式为item值
				String itemChar = itemList.get(i);
				First(itemSet, charItem, itemChar.charAt(0));
				// first只取首字符
			}
		}
		return true;
	}

	/**
	 * 获得Follow
	 */
	public void getFollow() {
		for (Character tempKey : vnSet) {
		    // 初始化followMap
			TreeSet<Character> tempSet = new TreeSet<Character>();
			followMap.put(tempKey, tempSet);
		}
		Iterator<Character> iter = vnSet.descendingIterator();
		// 逆向输出
		while (iter.hasNext()) {
			Character charItem = iter.next();
			// 获取要查询的item
			Set<Character> keySet = expressionMap.keySet();
			// 获取表达式的非终结符
			for (Character keyCharItem : keySet) {
			    // 遍历表达式的非终结符 获取结点名
				ArrayList<String> itemList = expressionMap.get(keyCharItem);
				// 获得节点名的字符集 一切都是为了查找 因为follow不是找自己的字符集
																			// 而是自己的后缀
				for (String itemChar : itemList) {
				    // 可能一个非终结符有很多字符集 一个个遍历
					TreeSet<Character> itemSet = followMap.get(charItem);
					// 获取follow集
					Follow(charItem, charItem, keyCharItem, itemChar, itemSet);
					// 新添follow集
				}
			}
		}
	}

	/**
	 * 
	 * @param putCharItem
	 *            正在查询item
	 * @param charItem
	 *            待找item
	 * @param keyCharItem
	 *            节点名
	 * @param itemCharStr
	 *            符号集
	 * @param itemSet
	 *            follow集
	 * @return
	 */
	public void Follow(Character putCharItem, Character charItem, Character keyCharItem, String itemCharStr,
			TreeSet<Character> itemSet) {
		if (charItem.equals(s)) {
			itemSet.add('#');
			followMap.put(putCharItem, itemSet);
		}
		if (FollowUtil.containsAb(vtSet, itemCharStr, charItem)) {
			Character alastChar = FollowUtil.getAlastChar(itemCharStr, charItem);
			itemSet.add(alastChar);
			if (itemSet.contains('ε')) {
				itemSet.add('#');
			}
			itemSet.remove('ε');
			followMap.put(putCharItem, itemSet);
		}
		if (FollowUtil.containsAB(vnSet, itemCharStr, charItem)) {
			Character alastChar = FollowUtil.getAlastChar(itemCharStr, charItem);
			TreeSet<Character> treeSet = firstMap.get(alastChar);
			itemSet.addAll(treeSet);
			if (treeSet.contains('ε')) {
				itemSet.add('#');
			}
			itemSet.remove('ε');
			followMap.put(putCharItem, itemSet);
			if (FollowUtil.containsbABIsNull(vnSet, itemCharStr, charItem, expressionMap)) {
				char tempChar = FollowUtil.getAlastChar(itemCharStr, charItem);
				if (!keyCharItem.equals(charItem)) {
				    // 不存在两个要查询的非终结符连在一起
					Set<Character> keySet = expressionMap.keySet();
					for (Character keyCharItems : keySet) {
						ArrayList<String> charItemArray = expressionMap.get(keyCharItems);
						for (String itemCharStrs : charItemArray) {
							Follow(putCharItem, keyCharItem, keyCharItems, itemCharStrs, itemSet);
						}
					}
				}
			}
		}
		if (FollowUtil.containsbA(vnSet, itemCharStr, charItem)) {
			if (!keyCharItem.equals(charItem)) {
			    // 非终结符等于结点
				Set<Character> keySet = expressionMap.keySet();
				for (Character keyCharItems : keySet) {
					ArrayList<String> charItemArray = expressionMap.get(keyCharItems);
					for (String itemCharStrs : charItemArray) {
						Follow(putCharItem, keyCharItem, keyCharItems, itemCharStrs, itemSet);
					}
				}
			} else {
				if (firstMap.get(charItem).contains('ε')) {
				    // 如果非终结符与结点相同 且 结点的first集含有ε 则添加#
					itemSet.add('#');
					followMap.put(putCharItem, itemSet);
				}
			}
		}
	}

	/**
	 * 获得select
	 */
	public void getSelect() {
		Set<Character> keySet = expressionMap.keySet();
		for (Character selectKey : keySet) {
			ArrayList<String> arrayList = expressionMap.get(selectKey);
			HashMap<String, TreeSet<Character>> selectItemMap = new HashMap<String, TreeSet<Character>>();
			for (String select : arrayList) {
				TreeSet<Character> selectSet = new TreeSet<>();
				if (SelectUtil.isEmpty(select)) {
					selectSet = followMap.get(selectKey);
					selectSet.remove('ε');
					selectItemMap.put(select, selectSet);
				}
				if (SelectUtil.isVtStart(vtSet, select)) {
					selectSet.add(select.charAt(0));
					selectSet.remove('ε');
					selectItemMap.put(select, selectSet);
				}
				if (SelectUtil.isVnStart(vnSet, select)) {
					selectSet = firstMap.get(selectKey);
					selectSet.remove('ε');
					selectItemMap.put(select, selectSet);
				}
				selectMap.put(selectKey, selectItemMap);
			}
		}
	}

	/**
	 * 生成分析表
	 */
	public void AnalyzeTable() {
		Object[] vtArray = vtSet.toArray();
		Object[] vnArray = vnSet.toArray();
		// 预测分析表初始化
		analyzeTable = new String[vnArray.length + 2][vtArray.length + 1];
 
		analyzeTable[vnArray.length+1][0] = "#";
		analyzeTable[vnArray.length+1][vtArray.length] = "acc";
		for(int i = 1;i<vtArray.length;i++) {
			analyzeTable[vnArray.length+1][i] = "";
		}
		// 输出一个占位符
		analyzeTable[0][0] = "Vn\\Vt";
		// 初始化首行
		for (int i = 0; i < vtArray.length; i++) {
			if (vtArray[i].equals('ε')) {
				vtArray[i] = '#';
			}
			analyzeTable[0][i + 1] = vtArray[i] + "";
		}
		for (int i = 0; i < vnArray.length; i++) {
			// 首列初始化
			analyzeTable[i + 1][0] = vnArray[i] + "";
			for (int j = 0; j < vtArray.length; j++) {
				String findUseExp = SelectUtil.findUseExp(selectMap, Character.valueOf((Character) vnArray[i]),
						Character.valueOf((Character) vtArray[j]));
				if (null == findUseExp) {
					analyzeTable[i + 1][j + 1] = "";
				} else {
					analyzeTable[i + 1][j + 1] = findUseExp;
				}
			}
		}
	}

}
