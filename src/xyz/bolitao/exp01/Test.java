package xyz.bolitao.exp01;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Boli Tao
 */
public class Test extends JFrame {
    Analyse analyse = new Analyse();
    String[] title = {"行", "符号", "结果"};
    String[][] values = {};
    /**
     * 显示结果的表格
     */
    private JTable table;
    /**
     * 控制表格的模型
     */
    private DefaultTableModel model;
    private JScrollPane scrollPane;

    private String readFromFile(String path) throws IOException {
        File file = new File(path);
        String s = null;
        FileInputStream fileInputStream = new FileInputStream(file);
        int length = fileInputStream.available();
        byte[] array = new byte[length];
        int len = fileInputStream.read(array);
        s = new String(array, 0, length);
        return s;
    }

    /**
     * 执行分析的方法
     */
    private void doAnalyse() throws IOException {
        ArrayList<Word> wordArrayList = new ArrayList<Word>();
        ArrayList<UnRecognized> unRecognizedArrayList = new ArrayList<UnRecognized>();
        String s, ts, str;
        Word word;
        String path;
        int i;
        int opcodes = -1;
        int errorNum = 0;
        int count = 0;

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入文件路径：");
        path = scanner.next();
        s = readFromFile(path);
        if (s.length() > 1) {
            ts = analyse.pretreat(s);
            wordArrayList = analyse.divide(ts);
            values = new String[wordArrayList.size()][3];
            while (wordArrayList.size() > 0) {
                word = (Word) wordArrayList.remove(0);
                str = word.getWord();
                i = analyse.check(str);
                switch (i) {
                    case 1:
                        opcodes = analyse.checkDigit(str);
                        break;
                    case 2:
                        opcodes = analyse.checkChar(str);
                        break;
                    case 3:
                        opcodes = analyse.checkString(str);
                        break;
                    default:
                }
                if (opcodes == 0) {
                    UnRecognized u = new UnRecognized(word.getRow(), str);
                    unRecognizedArrayList.add(u);
                    errorNum++;
                }
                values[count][0] = String.valueOf(word.getRow());
                values[count][1] = str;
                values[count][2] = String.valueOf(opcodes);
                count++;
            }
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            while (model.getRowCount() > 0) {
                model.removeRow(model.getRowCount() - 1);
            }
            model.setDataVector(values, title);
            table = new JTable(model);
        } else {
            System.out.println("源程序为空，请检查");
        }
    }

    /**
     * 构造方法
     * 图形界面的初始化
     */
    private Test() throws IOException {
        this.setTitle("分析程序");
        model = new DefaultTableModel(values, title);
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().add(table);
        scrollPane.getViewport().setPreferredSize(new Dimension(100, 100));
        doAnalyse();
        this.add(scrollPane);
        this.setVisible(true);
        this.setSize(350, 800);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(true);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(
                "**************************************************\n" +
                        "关键字1，标识符2，常数3，运算符4，分隔符5，\n" +
                        "**************************************************");
        Test test = new Test();
    }
}