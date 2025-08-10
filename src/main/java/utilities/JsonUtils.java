package utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.HashMap;
public class JsonUtils {

    private static final String JSON_FILE_PATH = "src/test/resources/";
    String jsonReader;
    String jsonFileName;

    public JsonUtils(String jsonFileName) {
        this.jsonFileName = jsonFileName;
        try {
            JSONObject data = (JSONObject) new JSONParser().parse(new FileReader(JSON_FILE_PATH + jsonFileName + ".json"));
            jsonReader = data.toJSONString();
        } catch (Exception e) {
            LogsUtil.error(e.getMessage());
        }
    }

    //login-credentials.username
    public String getJsonData(String jsonPath) {
        String testData = "";
        try {
            testData = JsonPath.read(jsonReader, jsonPath);
        } catch (Exception e) {
            LogsUtil.error(e.getMessage(), "No results for json path: '" + jsonPath + "' in the json file: '" + this.jsonFileName + "'");
        }
        LogsUtil.info("Json path: '" + jsonPath + "' in the json file: '" + this.jsonFileName + "' has value: '" + testData + "'");
        return testData;
    }

    // New method for reading JSON files as List<HashMap>
    public static List<HashMap<String, String>> readJsonDataAsList(String jsonFilePath) {
        try {
            String jsonContent = FileUtils.readFileToString(new File(jsonFilePath), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonContent, new TypeReference<List<HashMap<String, String>>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON data", e);
        }
    }

    // Method for reading JSON files as List of specific class
    public static <T> List<T> readJsonDataAsList(String jsonFilePath, Class<T> clazz) {
        try {
            String jsonContent = FileUtils.readFileToString(new File(jsonFilePath), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonContent, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON data", e);
        }
    }
}
