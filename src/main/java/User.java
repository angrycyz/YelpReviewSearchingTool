import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.*;

public class User {
    private JsonNode jsonNode;
    private ObjectMapper mapper;
    public static final String USER_ID = "user_id";
    public static final String NAME = "name";
    public static final String REVIEW_COUNT = "review_count";
    public static final String YELPING_SINCE = "yelping_since";
    public static final String FRIENDS = "friends";
    public static final String USEFUL = "useful";
    public static final String FUNNY = "funny";
    public static final String COOL = "cool";
    public static final String FANS = "fans";
    public static final String AVERAGE_STARS = "average_stars";

    public User(String userJson) {
        try {
            this.mapper = new ObjectMapper();
            this.jsonNode = this.mapper.readTree(userJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUserId() {
        return this.jsonNode.get(USER_ID).textValue();
    }

    public String getName() {
        return this.jsonNode.get(NAME).textValue();
    }

    public long getReviewCount() {
        return this.jsonNode.get(REVIEW_COUNT).asLong();
    }

    public String getYelpingSince() {
        return this.jsonNode.get(YELPING_SINCE).textValue();
    }

    public long getFriendsCount() {
        JsonNode node = jsonNode.get(FRIENDS);
        int count = 0;
        if (node.isArray()) {
            for (final JsonNode objNode : node) {
                count += 1;
            }
        }
        return count;
    }

    public long getUseful() {
        return this.jsonNode.get(USEFUL).asLong();
    }

    public long getFunny() {
        return this.jsonNode.get(FUNNY).asLong();
    }

    public long getCool() {
        return this.jsonNode.get(COOL).asLong();
    }

    public long getFans() {
        return this.jsonNode.get(FANS).asLong();
    }

    public double getAverageStar() {
        return this.jsonNode.get(AVERAGE_STARS).asDouble();
    }

    public void printAll() {
        System.out.println("User id: " + getUserId()
                + ", Name: " + getName()
                + ", Review Count: " + Long.toString(getReviewCount())
                + ", Yelping Since: " + getYelpingSince()
                + ", Friends Count: " + Long.toString(getFriendsCount())
                + ", Useful: " + Long.toString(getUseful())
                + ", Funny: " + Long.toString(getFunny())
                + ", Cool: " + Long.toString(getCool())
                + ", Fans: " + Long.toString(getFans())
                + ", AverageStar" + Double.toString(getAverageStar()));
    }
}
