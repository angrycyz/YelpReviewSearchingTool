import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class Indexer {
    public static final String USER_ID = "user_id";
    public static final String BUSINESS_ID = "business_id";
    public static final String REVIEW_COUNT = "review_count";
    public static final String FRIENDS = "friends_count";
    public static final String USER_USEFUL = "user_useful";
    public static final String USER_FUNNY = "user_funny";
    public static final String USER_COOL = "user_cool";
    public static final String USER_FANS = "user_fans";
    public static final String AVERAGE_STARS = "average_stars";

    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String POSTAL = "postal_code";

    public static final String UNKNOWN_USER = "unknown user";
    public static final String UNKNOWN_BUSINESS = "unknown business";
    public static Logger logger = Logger.getLogger("Indexer");
    private Map<String, User> userMap = new HashMap<>();
    private Map<String, Business> businessMap = new HashMap<>();

    public String refineReviewJson(String review) {
        String refinedReview = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(review);
            User user = userMap.getOrDefault(node.get(USER_ID).textValue(), null);

            if (user != null) {
                ((ObjectNode) node).put(USER_ID, user.getName());
                ((ObjectNode) node).put(REVIEW_COUNT, user.getReviewCount());
                ((ObjectNode) node).put(FRIENDS, user.getFriendsCount());
                ((ObjectNode) node).put(USER_USEFUL, user.getUseful());
                ((ObjectNode) node).put(USER_FUNNY, user.getFunny());
                ((ObjectNode) node).put(USER_COOL, user.getCool());
                ((ObjectNode) node).put(USER_FANS, user.getFans());
                ((ObjectNode) node).put(AVERAGE_STARS, user.getAverageStar());
            } else {
                ((ObjectNode) node).put(USER_ID, UNKNOWN_USER);
                ((ObjectNode) node).put(REVIEW_COUNT, 0);
                ((ObjectNode) node).put(FRIENDS, 0);
                ((ObjectNode) node).put(USER_USEFUL, 0);
                ((ObjectNode) node).put(USER_FUNNY, 0);
                ((ObjectNode) node).put(USER_COOL, 0);
                ((ObjectNode) node).put(USER_FANS, 0);
                ((ObjectNode) node).put(AVERAGE_STARS, 0);
            }

            refinedReview = mapper.writeValueAsString(node);

            Business business = businessMap.getOrDefault(node.get(BUSINESS_ID).textValue(), null);

            if (business != null) {
                ((ObjectNode) node).put(BUSINESS_ID, business.getName());
                ((ObjectNode) node).put(CITY, business.getCity());
                ((ObjectNode) node).put(STATE, business.getState());
                ((ObjectNode) node).put(POSTAL, business.getPostal());
            } else {
                ((ObjectNode) node).put(BUSINESS_ID, UNKNOWN_BUSINESS);
                ((ObjectNode) node).put(CITY, "");
                ((ObjectNode) node).put(STATE, "");
                ((ObjectNode) node).put(POSTAL, "");
            }

            refinedReview = mapper.writeValueAsString(node);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return refinedReview;
    }

    public void readReviewFile(String path) {

        File file = new File(path);
        long count = 0;
        EsConnector esConnector = new EsConnector();
        esConnector.initClient();
        esConnector.deleteIndex();

        try {
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                String line = input.nextLine();
                String refinedReview = refineReviewJson(line);
                esConnector.postData(refinedReview);
                count += 1;
                if (count % 1000 == 0) {
                    System.out.println("Parsed " + Long.toString(count) + " reviews");
                }
                if (count == 100000) {
                    break;
                }
            }
            System.out.println("Indexd " + Long.toString(count) + " reviews");
            this.logger.info("Indexd " + Long.toString(count) + " reviews");
        } catch(FileNotFoundException e) {
            this.logger.warning("Review file not found.");
            System.out.println("Review file not found.");
            e.printStackTrace();
        }

        esConnector.closeClient();
    }

    public void readBusinessFile(String path) {

        File file = new File(path);
        long count = 0;

        try {
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                String line = input.nextLine();
                if (line.length() == 0) {
                    continue;
                }
                Business business = new Business(line);
                this.businessMap.put(business.getBusinessId(), business);
                count += 1;
                if (count % 1000 == 0) {
                    System.out.println("Parsed " + Long.toString(count) + " Businesses");
                }
                if (count == 100000) {
                    break;
                }
            }
            System.out.println("Parsed " + Long.toString(count) + " Businesses");
            this.logger.info("Parsed " + Long.toString(count) + " Businesses");
        } catch(FileNotFoundException e) {
            System.out.println("Business file not found.");
            this.logger.warning("Business file not found.");
            e.printStackTrace();
        }

    }

    public void readUserFile(String path) {
        File file = new File(path);
        long count = 0;

        try {
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                String line = input.nextLine();
                if (line.length() == 0) {
                    continue;
                }
                User user = new User(line);
                this.userMap.put(user.getUserId(), user);
                count += 1;
                if (count % 1000 == 0) {
                    System.out.println("Parsed " + Long.toString(count) + " Users");
                }
                if (count == 100000) {
                    break;
                }
            }
            System.out.println("Parsed " + Long.toString(count) + " Users");
            this.logger.info("Parsed " + Long.toString(count) + " Users");
        } catch(FileNotFoundException e) {
            System.out.println("User file not found.");
            this.logger.warning("User file not found.");
            e.printStackTrace();
        }

    }


    public static void main(String args[]) {
        String reviewPath = "/Users/xieyu/Downloads/dataset/review.json";
        String businessPath = "/Users/xieyu/Downloads/dataset/business.json";
        String userPath = "/Users/xieyu/Downloads/dataset/user.json";

        System.out.println("Start Indexing...");

        Indexer indexer = new Indexer();

        indexer.readUserFile(userPath);

        indexer.readBusinessFile(businessPath);

        indexer.readReviewFile(reviewPath);
    }
}
