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
        // 内存流, 作为临时流
        CharArrayWriter tempStream = new CharArrayWriter();
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

    public static void main(String[] args) {
        String path = "D:\\work\\gproto\\api-gproto\\protofile\\test-enum.proto";
        Map<String, String> keyWordMap = Maps.newHashMap();
        keyWordMap.put(ProtoConstant.JAVA_PACKAGE, null);
        keyWordMap.put(ProtoConstant.JAVA_OUTER_CLASSNAME, null);
        try {
            Map<String, String> result = FileUtil.findContent(path, keyWordMap);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
