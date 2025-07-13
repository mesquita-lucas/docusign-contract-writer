package hub4.json;

import hub4.Main;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class JSONLoader {
    private JSONLoader() {}

    public static JSONObject loadJson(String fileName) {
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("contract-content.json")) {
            Objects.requireNonNull(is, "contractNotFound");

            JSONParser parser = new JSONParser();

            return (JSONObject) parser.parse(new InputStreamReader(is));
        } catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
