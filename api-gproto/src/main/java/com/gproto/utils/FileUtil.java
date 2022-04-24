package com.gproto.utils;

import com.google.common.collect.Maps;
import com.gproto.constants.ProtoConstant;
import com.gproto.entity.ProtoInfoEntity;

import java.io.*;
import java.util.Map;

public class FileUtil {


    /**
     * find keywords value string, replace [" ", "\"", ";", "="] to ""
     *
     *
     * @param path
     * @throws IOException
     */
    public static Map<String, String> findContent(String path, Map<String, String> keyWordMap) throws IOException {
        Map<String, String> result = Maps.newHashMap();

        File file = new File(path);
        FileReader in = new FileReader(file);
        BufferedReader bufIn = new BufferedReader(in);
        // 替换
        String line = null;
        while ((line = bufIn.readLine()) != null) {
            // 替换每行中, 符合条件的字符串
            final String lineTemp = line;
            keyWordMap.forEach((keyWord, value) -> {
                if (lineTemp.startsWith(keyWord)) {
                    result.put(keyWord, lineTemp.replaceAll(keyWord, "")
                            .replaceAll("=", "")
                            .replaceAll(" ", "")
                            .replaceAll(";", "")
                            .replaceAll("\"", "")
                    );
                }
            });

            if (result.size() == keyWordMap.size()) {
                break;
            }

        }
        // 关闭 输入流
        bufIn.close();
        return result;
    }

    public static boolean replace(String filePath, Map<String, String> keyWords) throws IOException{
        // 读
        File file = new File(filePath);

        FileReader in = new FileReader(file);
        BufferedReader bufIn = new BufferedReader(in);

        // 内存流, 作为临时流
        CharArrayWriter  tempStream = new CharArrayWriter();

        // 替换
        String line = null;

        while ( (line = bufIn.readLine()) != null) {
            final String tempLine = line;
            keyWords.forEach((key, word) -> {
                // 替换每行中, 符合条件的字符串
                String tempStr = "";
                if(tempLine.startsWith(key)){
                    tempStr = tempLine.replaceAll("com\\.", "com." + word);
                }
                // 将该行写入内存
                try {
                    tempStream.write(tempStr);
                    // 添加换行符
                    tempStream.append(System.getProperty("line.separator"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
//            // 替换每行中, 符合条件的字符串
//            line = line.replaceAll(srcStr, replaceStr);
//            // 将该行写入内存
//            tempStream.write(line);
//            // 添加换行符
//            tempStream.append(System.getProperty("line.separator"));
        }

        // 关闭 输入流
        bufIn.close();

        // 将内存中的流 写入 文件
        FileWriter out = new FileWriter(file);
        tempStream.writeTo(out);
        out.close();
        return true;
    }

    public static boolean replacePackage(String filePath, String uid) throws IOException{
        // 读
        File file = new File(filePath);

        FileReader in = new FileReader(file);
        BufferedReader bufIn = new BufferedReader(in);

        // 内存流, 作为临时流
        CharArrayWriter  tempStream = new CharArrayWriter();

        // 替换
        String line = null;

        while ( (line = bufIn.readLine()) != null) {
                // 替换每行中, 符合条件的字符串
                String tempStr = line;
            if(line.startsWith(ProtoConstant.JAVA_PACKAGE)){
                String pkg = line.replaceAll(ProtoConstant.JAVA_PACKAGE, "");
                String[] pkgWords = pkg.split("\\.");
                if(null == pkgWords || pkgWords.length == 0){
                    return false;
                }
                String pkgWord1 = pkgWords[0];
                pkg = pkg.replaceAll(pkgWord1, pkgWord1 + "." + "0403");
                tempStr = ProtoConstant.JAVA_PACKAGE + pkg;
            }
                // 将该行写入内存
                try {
                    tempStream.write(tempStr);
                    // 添加换行符
                    tempStream.append(System.getProperty("line.separator"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

        // 关闭 输入流
        bufIn.close();

        // 将内存中的流 写入 文件
        FileWriter out = new FileWriter(file);
        tempStream.writeTo(out);
        out.close();
        return true;
    }

    public static void main(String[] args) {
        String path = "c:\\qianzhm\\gproto\\api-gproto\\protofile\\test-enum.proto";
        Map<String, String> keyWordMap = Maps.newHashMap();
        keyWordMap.put(ProtoConstant.JAVA_PACKAGE, "0403");
//        keyWordMap.put(ProtoConstant.JAVA_OUTER_CLASSNAME, null);
        try {
            FileUtil.replacePackage(path, "0403");


//            String str = "option java_package = \"com.saic.val.proto\";";
//
//            if(str.startsWith(ProtoConstant.JAVA_PACKAGE)){
//                String pkg = str.replaceAll(ProtoConstant.JAVA_PACKAGE, "");
//                System.out.println(pkg);
//                String[] pkgWords = pkg.split("\\.");
//                if(null == pkgWords || pkgWords.length == 0){
//                    return;
//                }
//                String pkgWord1 = pkgWords[0];
//                pkg = pkg.replaceAll(pkgWord1, pkgWord1 + "." + "0403");
//                String ret = ProtoConstant.JAVA_PACKAGE + pkg;
//                System.out.println(ret);
//            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
