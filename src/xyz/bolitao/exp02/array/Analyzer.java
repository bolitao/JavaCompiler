package xyz.bolitao.exp02.array;

import java.util.ArrayList;
import java.util.Stack;

import xyz.bolitao.exp02.sff.SFF;
import xyz.bolitao.exp02.sff.SelectUtil;

public class Analyzer {
	/**
	 * 分析栈
	 */
	private Stack<Character> analyzeStatck;

	/**
	 * 输入串
	 */
	private String str;

	SFF sff = new SFF();

	ArrayList<String> gsList = sff.getGsArray();

	public Stack<Character> getAnalyzeStatck() {
		return analyzeStatck;
	}

	public void setAnalyzeStatck(Stack<Character> analyzeStatck) {
		this.analyzeStatck = analyzeStatck;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	/**
	 * 输出前期的工作
	 */
	public Analyzer() {
		sff.getVnVt();
		System.out.println("文法:\t"+gsList.toString());
		System.out.println("Vn:\t"+sff.getVnSet());
		System.out.println("Vt:\t"+sff.getVtSet());
		sff.initExpressionMaps();
		sff.getFirst();
		System.out.println("first:\t"+sff.getFirstMap());
		sff.getFollow();
		System.out.println("follow:\t"+sff.getFollowMap());
		sff.getSelect();
		System.out.println("select:\t"+sff.getSelectMap());
		sff.AnalyzeTable();
		System.out.println("分析表:");
		for (int i = 0; i < sff.getAnalyzeTable().length; i++) {
			for (int j = 0; j < sff.getAnalyzeTable()[0].length; j++) {
				System.out.print(sff.getAnalyzeTable()[i][j] + "\t\t");
			}
			System.out.println();
		}
		analyzeStatck = new Stack<>();
	}

	/**
	 * 初始化分析栈
	 */
	public void initAnalyzeStatck() {
		analyzeStatck.push('#');
		analyzeStatck.push(sff.getS());
	}

	/**
	 * 分析
	 */
	public void analyze() {
		Character s = analyzeStatck.peek();
		String findUseExp = SelectUtil.findUseExp(sff.getSelectMap(), s, str.charAt(0));
		if (str.equals("#") && s == '#') {
		    // 如果匹配完成 输出成功
			System.out.println("#\t\t\t#\t\t\tacc");
			System.out.println("Match success!!!");
			return;
		}
		if (findUseExp == null) {
		    // 如果找不到匹配的文法 则输出报错
			System.out.println("error,input string does not belong to this grammar.");
			return;
		}
		if (findUseExp.equals("ε")) {
		    // 找到了ε 分析栈直接出栈
			System.out.println(analyzeStatck.toString() + "\t\t\t" + str + "\t\t\tε/P");
			analyzeStatck.pop();
		} else if (sff.getVtSet().contains(findUseExp.charAt(0))) {
			// 匹配终结符成功 都出栈 将多余的入栈
			char[] ch = (new StringBuilder(findUseExp.substring(1)).reverse()).toString().toCharArray();
			Character[] itemChar = new Character[ch.length];
			analyzeStatck.pop();
			String nstr = "";
			for (int i = 0; i < ch.length; i++) {
				itemChar[i] = Character.valueOf(ch[i]);
				analyzeStatck.push(itemChar[i]);
				nstr += ch[i];
			}
			if (!nstr.equals(""))
				System.out.println(analyzeStatck.toString() + "\t\t\t" + str + "\t\t\t" + nstr + "/N");
			else
				System.out.println(analyzeStatck.toString() + "\t\t\t" + str + "\t\t\tε/N");
			str = str.substring(1);
		} else if (sff.getVnSet().contains(findUseExp.charAt(0))) {
		    // 直接出栈 入栈
			char[] ch = (new StringBuilder(findUseExp).reverse()).toString().toCharArray();
			Character[] itemChar = new Character[ch.length];
			analyzeStatck.pop();
			String pstr = "";
			for (int i = 0; i < ch.length; i++) {
				itemChar[i] = Character.valueOf(ch[i]);
				analyzeStatck.push(itemChar[i]);
				pstr += ch[i];
			}
			System.out.println(analyzeStatck.toString() + "\t\t\t" + str + "\t\t\t" + pstr + "/P");
		}
		analyze();// 循环
	}

}
