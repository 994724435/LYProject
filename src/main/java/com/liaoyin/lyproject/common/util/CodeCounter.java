package com.liaoyin.lyproject.common.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 作	者：
 * 创建日期：2018年6月23日
 * 描	述：统计项目代码行数
 */
public class CodeCounter {
	
	List<File> list = new ArrayList<File>();
    int linenumber = 0;

    FileReader fr = null;
    BufferedReader br = null;

    public void counter(String path) {
        System.out.println("path:"+path);
        File file = new File(path);
        File files[] = null;
        files = file.listFiles();
        addFile(files);
        isDirectory(files);
        readLinePerFile();
        System.out.println("总共:" + linenumber + "行");
    }

    // 判断是否是目录
    public void isDirectory(File[] files) {
        for (File s : files) {
            if (s.isDirectory()) {
                File file[] = s.listFiles();
                addFile(file);
                isDirectory(file);
                continue;
            }
        }
    }

    //将src下所有文件组织成list
    public void addFile(File file[]) {
        for (int index = 0; index < file.length; index++) {
            list.add(file[index]);
            // System.out.println(list.size());
        }
    }

    //读取非空白行
    public void readLinePerFile() {
        try {
            for (File s : list) {
                int yuan = linenumber;
                if (s.isDirectory()) {
                    continue;
                }
                fr = new FileReader(s);
                br = new BufferedReader(fr);
                String i = "";
                while ((i = br.readLine()) != null) {
                    if (isBlankLine(i))
                        linenumber++;
                }
                System.out.print(s.getName());
                System.out.println("\t\t有" + (linenumber - yuan) + "行");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (Exception e) {
                }
            }
        }
    }

    //是否是空行
    public boolean isBlankLine(String i) {
        if (i.trim().length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static void main(String args[]) {
        CodeCounter lc = new CodeCounter();
        String projectName = "F:/JavaProject/LYProject/src/main/java";//要检测的路径
        lc.counter(projectName);
    }

}
