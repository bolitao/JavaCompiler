package xyz.bolitao.exp02.main;

import java.util.Scanner;

import xyz.bolitao.exp02.array.Analyzer;

public class LL_1Main {

	public static void main(String[] args) {
		Analyzer analyzer = new Analyzer();
		analyzer.initAnalyzeStatck();
		System.out.print("Please enter the string,End with the '#':");
		Scanner can = new Scanner(System.in);
		String str = can.nextLine();
		while(!str.endsWith("#")) {
			System.out.print("Wrong input, please re input:");
			str = can.nextLine();
		}
		analyzer.setStr(str);
		analyzer.analyze();
	}
	
}
