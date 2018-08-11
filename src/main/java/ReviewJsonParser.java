import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ReviewJsonParser {
    public static final String REVIEW_ID = "review_id";
    public static final String USER_ID = "user_id";
    public static final String BUSINESS_ID = "business_id";
    public static final String STARS = "starts";
    public static final String DATE = "date";
    public static final String TEXT = "text";
    public static final String USEFUL = "useful";
    public static final String FUNNY = "funny";
    public static final String COOL = "cool";

    public JsonNode createNode(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonString);
            return jsonNode;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getReviewId(JsonNode jsonNode) {
        return jsonNode.get(REVIEW_ID).textValue();
    }

    public String getUserId(JsonNode jsonNode) {
        return jsonNode.get(USER_ID).textValue();
    }

    public String getBusinessId(JsonNode jsonNode) {
        return jsonNode.get(BUSINESS_ID).textValue();
    }

    public String getStars(JsonNode jsonNode) {
        return jsonNode.get(STARS).textValue();
    }

    public String getDate(JsonNode jsonNode) {
        return jsonNode.get(DATE).textValue();
    }

    public String getText(JsonNode jsonNode) {
        return jsonNode.get(TEXT).textValue();
    }

    public String getUseful(JsonNode jsonNode) {
        return jsonNode.get(USEFUL).textValue();
    }

    public String getFunny(JsonNode jsonNode) {
        return jsonNode.get(FUNNY).textValue();
    }

    public String getCool(JsonNode jsonNode) {
        return jsonNode.get(COOL).textValue();
    }

}
