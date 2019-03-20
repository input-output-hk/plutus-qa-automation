package io.iohk.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;


public class FilesUtils {

    public static JsonNode readJson(String filePath) throws Exception{
        File file = new File(filePath);
        String content = FileUtils.readFileToString(file, "utf-8");

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(content);
    }
}
