import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.InetSocketAddress;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.util.*;
import java.util.stream.Collectors;

public class QueryProcessor {

    public static final String UNKNOWN_USER = "unknown user";
    public static final String UNKNOWN_BUSINESS = "unknown business";
    public static final double FULL_SCORE = 5.0;
    public static final int INIT_YEAR = 2000;
    public static final double SCORE_WEIGHT = 0.2;
    public static final double SCAM_SCORE_LOWER = 2.0;
    public static final double SCAM_SCORE_UPPER =4.95;

    public String getSearchString(int size, String review, String business, String city, String state, String postal) {
        return Json.createObjectBuilder()
                .add("from", 0)
                .add("size", size)
                .add("query", Json.createObjectBuilder()
                        .add("bool", Json.createObjectBuilder()
                                .add("should", Json.createArrayBuilder()
                                        .add(Json.createObjectBuilder()
                                                .add("match", Json.createObjectBuilder()
                                                    .add("text", Json.createObjectBuilder()
                                                            .add("query", review)
                                                            .add("boost", 2))))
                                        .add(Json.createObjectBuilder()
                                                .add("match", Json.createObjectBuilder()
                                                    .add("business_id", Json.createObjectBuilder()
                                                            .add("query", business)
                                                            .add("boost", 3))))
                                        .add(Json.createObjectBuilder()
                                                .add("match", Json.createObjectBuilder()
                                                    .add("city", Json.createObjectBuilder()
                                                            .add("query", city)
                                                            .add("boost", 1))))
                                        .add(Json.createObjectBuilder()
                                                .add("match", Json.createObjectBuilder()
                                                    .add("state", Json.createObjectBuilder()
                                                            .add("query", state)
                                                            .add("boost", 1))))
                                        .add(Json.createObjectBuilder()
                                                .add("match", Json.createObjectBuilder()
                                                    .add("postal_code", Json.createObjectBuilder()
                                                            .add("query", postal)
                                                            .add("boost", 1)))))))
                .build()
                .toString();
    }

    public double calcUserScore(long reviewCount, long friends,
                           long userUseful, long userFunny, long userCool,
                           long userFans, double averageStars) {
        System.out.println("Start calculating score");
        double avgStarScore = averageStars;
        if (averageStars < SCAM_SCORE_LOWER) {
            avgStarScore = averageStars / 10.0;
        } else if (averageStars > SCAM_SCORE_UPPER) {
            avgStarScore = (FULL_SCORE - averageStars) / 10.0;
        } else {
            avgStarScore = 1;
        }

        long avgWeight = 1;
        if (reviewCount != 0) {
            avgWeight = reviewCount;
        }

        System.out.println("got average");

        return (reviewCount * 20.0/100.0 + friends * 20.0/100.0 + userUseful * 10.0/100.0
                + userFunny * 10.0/100.0 + userCool * 10.0/100.0 + userFans * 10.0/100.0
                + avgStarScore * (double)avgWeight * 20.0/100.0)/100;
    }

    public List<SearchResponse> parseSearchResponse(String responseStr) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseStr);

            List<String> user_id = jsonNode.findValuesAsText("user_id");
            System.out.println(user_id);

            List<String> business_id = jsonNode.findValuesAsText("business_id");
            System.out.println(business_id);

            List<JsonNode> starsNode = jsonNode.findValues("stars");
            List<Integer> stars = starsNode.stream().map(x -> x.asInt()).collect(Collectors.toList());
            System.out.println(stars);

            List<String> date = jsonNode.findValuesAsText("date");
            System.out.println(date);

            List<String> text = jsonNode.findValuesAsText("text");
            System.out.println(text);

            List<JsonNode> usefulNode = jsonNode.findValues("useful");
            List<Long> useful = usefulNode.stream().map(x -> x.asLong()).collect(Collectors.toList());
            System.out.println(useful);

            List<JsonNode> coolNode = jsonNode.findValues("cool");
            List<Long> cool = coolNode.stream().map(x -> x.asLong()).collect(Collectors.toList());
            System.out.println(cool);

            List<JsonNode> funnyNode = jsonNode.findValues("funny");
            List<Long> funny = funnyNode.stream().map(x -> x.asLong()).collect(Collectors.toList());
            System.out.println(funny);

            List<JsonNode> reviewCountNode = jsonNode.findValues("review_count");
            List<Long> reviewCount = reviewCountNode.stream().map(x -> x.asLong()).collect(Collectors.toList());
            System.out.println(reviewCount);

            List<JsonNode> friendsNode = jsonNode.findValues("friends_count");
            List<Long> friends = friendsNode.stream().map(x -> x.asLong()).collect(Collectors.toList());
            System.out.println(friends);

            List<JsonNode> userUsefulNode = jsonNode.findValues("user_useful");
            List<Long> userUseful = userUsefulNode.stream().map(x -> x.asLong()).collect(Collectors.toList());
            System.out.println(userUseful);

            List<JsonNode> userFunnyNode = jsonNode.findValues("user_funny");
            List<Long> userFunny = userFunnyNode.stream().map(x -> x.asLong()).collect(Collectors.toList());
            System.out.println(userFunny);

            List<JsonNode> userCoolNode = jsonNode.findValues("user_cool");
            List<Long> userCool = userCoolNode.stream().map(x -> x.asLong()).collect(Collectors.toList());
            System.out.println(userCool);

            List<JsonNode> userFansNode = jsonNode.findValues("user_fans");
            List<Long> userFans = userFansNode.stream().map(x -> x.asLong()).collect(Collectors.toList());
            System.out.println(userFans);

            List<JsonNode> averageStarsNode = jsonNode.findValues("average_stars");
            List<Double> averageStars = averageStarsNode.stream().map(x -> x.asDouble()).collect(Collectors.toList());
            System.out.println(averageStars);

            List<String> city = jsonNode.findValuesAsText("city");
            System.out.println(city);

            List<String> state = jsonNode.findValuesAsText("state");
            System.out.println(state);

            List<String> postal = jsonNode.findValuesAsText("postal_code");
            System.out.println(postal);

            List<SearchResponse> responseList = new ArrayList<>();

            System.out.println("Parsed Response fields, result size:" + user_id.size());

            for (int i = 0; i < user_id.size(); i++) {

                double userScore = 0;
                String c = new String();
                String s = new String();
                String p = new String();
                System.out.println(i);
                System.out.println("step1");

                if (!user_id.get(i).equals(UNKNOWN_USER)) {
                    System.out.println("unknown u");
                    userScore = calcUserScore(reviewCount.get(i), friends.get(i),
                            userUseful.get(i), userFunny.get(i), userCool.get(i),
                            userFans.get(i), averageStars.get(i));
                    System.out.println(userScore);
                }
                System.out.println("step2");

                if (!business_id.get(i).equals(UNKNOWN_BUSINESS)) {
                    c = city.get(i);
                    s = state.get(i);
                    p = postal.get(i);
                }

                System.out.println("step3");
                SearchResponse response = new SearchResponse(user_id.get(i), business_id.get(i), stars.get(i),
                        date.get(i), text.get(i), useful.get(i), funny.get(i), cool.get(i), c, s, p, userScore);

                System.out.println("step4");
                responseList.add(response);
            }

            System.out.println("Parsed Response");

            return responseList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String createResultJson(List<SearchResponse> responseList) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (SearchResponse response: responseList) {
            arrayBuilder
                    .add(Json.createObjectBuilder()
                        .add("user", response.getUser_id())
                        .add("business", response.getBusiness_id())
                        .add("text", response.getText())
                        .add("city", response.getCity())
                        .add("state", response.getState())
                        .add("postal", response.getPostal())
                        .add("date", response.getDate()));
        }

        return arrayBuilder.build().toString();
    }

    public int getDays(String d) {
        String[] date = d.split("-");
        return (Integer.parseInt(date[0]) - INIT_YEAR) * 365
                + (Integer.parseInt(date[1]) * 12) + Integer.parseInt(date[2]);
    }

    public String search(int size, String review, String business, String city, String state, String postal) {
        EsConnector esConnector = new EsConnector();
        esConnector.initClient();

        System.out.println("Getting Search String");
        String searchString = getSearchString(size, review, business, city, state, postal);
        System.out.println("Start Searching");
        String responseStr = esConnector.searchQuery(searchString);
        System.out.println("Parsing Response");
        List<SearchResponse> responseList = parseSearchResponse(responseStr);

        Comparator<SearchResponse> dateScoreDescending = new Comparator<SearchResponse>() {
            @Override
            public int compare(SearchResponse r1, SearchResponse r2) {
                return (int)(r2.getUser_score() * SCORE_WEIGHT + getDays(r2.getDate()) * (1 - SCORE_WEIGHT))
                        - (int)(r1.getUser_score() * SCORE_WEIGHT + getDays(r1.getDate()) * (1 - SCORE_WEIGHT));
            }
        };

        System.out.println("Sorting Response");
        Collections.sort(responseList, dateScoreDescending);

        esConnector.closeClient();

        return createResultJson(responseList);
    }

    static class TestHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
            }

            try{
                String query = new String();
                String business = new String();
                String city = new String();
                String state = new String();
                String postal = new String();


                String queryString =  exchange.getRequestURI().getQuery();
                System.out.println(queryString);

                if (queryString != null) {
                    String[] Arr = queryString.split("&");
                    if (Arr.length == 5) {
                        String[] qLst = Arr[0].split("=");
                        if (qLst.length > 1) {
                            query = qLst[1];
                        }
                        String[] bLst = Arr[1].split("=");
                        if (bLst.length > 1) {
                            business = bLst[1];
                        }
                        String[] cLst = Arr[2].split("=");
                        if (cLst.length > 1) {
                            city = cLst[1];
                        }
                        String[] sLst = Arr[3].split("=");
                        if (sLst.length > 1) {
                            state = sLst[1];
                        }
                        String[] pLst = Arr[4].split("=");
                        if (pLst.length > 1) {
                            postal = pLst[1];
                        }
                    }
                } else {
                    String postString = IOUtils.toString(exchange.getRequestBody());
                    System.out.println(postString);

                    if (postString.length() > 0) {

                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode jsonNode = mapper.readTree(postString);

                        query = jsonNode.get("query").textValue();
                        business = jsonNode.get("business").textValue();
                        city = jsonNode.get("city").textValue();
                        state = jsonNode.get("state").textValue();
                        postal = jsonNode.get("postal").textValue();
                    }
                }

                String postInfo = formData2Dic(query, business, city, state, postal);

                exchange.sendResponseHeaders(200, postInfo.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(postInfo.getBytes());
                os.close();

            }catch (Exception ie) {
                ie.printStackTrace();
            }
        }
    }

    public static String formData2Dic(String q, String b, String c, String s, String p) {
        System.out.println("Form data: " + q + "," + b + "," + c + "," + s + "," + p);
        QueryProcessor queryProcessor = new QueryProcessor();
        String result = queryProcessor.search(10, q, b, c, s, p);
        System.out.println(result);
        return result;
    }

    public static void main(String args[]) {

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
            server.createContext("/yelp/review", new TestHandler());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
