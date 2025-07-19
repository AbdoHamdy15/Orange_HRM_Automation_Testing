package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class JsonDataReader {

    public static JsonNode readJsonFile(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File("src/test/resources/testdata/" + fileName);
            return mapper.readTree(file);
        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to read JSON file: " + fileName, e);
        }
    }

    public static Iterator<Object[]> getTestData(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/test/resources/" + fileName);
        List<HashMap<String, String>> data = mapper.readValue(file, new TypeReference<List<HashMap<String, String>>>() {});
        
        return data.stream()
                .map(emp -> new Object[]{emp})
                .iterator();
    }
}
