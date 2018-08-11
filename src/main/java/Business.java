import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

public class Business {
    private JsonNode jsonNode;
    private ObjectMapper mapper;
    public static final String BUSINESS_ID = "business_id";
    public static final String NAME = "name";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String POSTAL = "postal_code";
    public static final String CATEGORIES = "categories";

    public Business(String businessJson) {
        try {
            this.mapper = new ObjectMapper();
            this.jsonNode = this.mapper.readTree(businessJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBusinessId() {
        return this.jsonNode.get(BUSINESS_ID).textValue();
    }

    public String getName() {
        return this.jsonNode.get(NAME).textValue();
    }

    public String getCity() {
        return this.jsonNode.get(CITY).textValue();
    }

    public String getState() {
        return this.jsonNode.get(STATE).textValue();
    }

    public String getPostal() {
        return this.jsonNode.get(POSTAL).textValue();
    }

    public List<String> getCategories() {
        JsonNode node = jsonNode.get(CATEGORIES);
        List<String> categories = new ArrayList<>();
        if (node.isArray()) {
            for (final JsonNode objNode : node) {
                categories.add(objNode.textValue());
            }
        }
        return categories;
    }

    public void printAll() {
        System.out.println("Business id: " + getBusinessId()
                + ", Name: " + getName()
                + ", City: " + getCity()
                + ", State: " + getState()
                + ", Postal: " + getPostal()
                + ", Categories: " + getCategories().toString());
    }
}
