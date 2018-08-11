import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;

public class EsConnector{
    private RestClient restClient = null;
    private Process es_process;
    public static final String ENDPOINT = "yelp/reviews/";
    public static final String ENDPOINT_BASE = "/yelp";

    public String searchQuery(String jsonString) {
        Response response = null;
        String responseJson = "";
        try {
            Map<String, String> params = Collections.emptyMap();

            HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
            response = this.restClient.performRequest("GET", ENDPOINT + "_search?scroll=1m", params, entity);
            responseJson = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(response.getStatusLine().toString());
        return responseJson;
    }

    public Response postData(String jsonString) {
        Response response = null;
        try {
            Map<String, String> params = Collections.emptyMap();

            HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
            response = this.restClient.performRequest("POST", ENDPOINT, params, entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(response.getStatusLine().toString());
        return response;
    }

    public void initClient() {
//        runEsExecutable();
        if (restClient == null) {
            this.restClient = RestClient.builder(
                    new HttpHost("localhost", 9200, "http")).build();
        }
    }

    public void closeClient() {
        if (restClient == null)
            return;
        try {
            this.restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        stopEsExecutable();
    }

    public void runEsExecutable() {
//        tmp elasticsearch path: /Users/xieyu/Downloads/elasticsearch-6.2.4/bin/elasticsearch
        System.out.println("Please give elasticsearch executable path...");
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] line_arg = line.trim().split("\\s+");
                    if (line_arg.length == 1) {
                        String[] cmd = {"sh", line_arg[0]};
                        this.es_process = Runtime.getRuntime().exec(cmd);
                    } else {
                        System.err.println("Invalid arguments number");
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopEsExecutable() {
        this.es_process.destroy();
        if (this.es_process.exitValue() != 0) {
            System.out.println("Abnormal process termination");
        }
    }

    public void deleteIndex() {
        try {
            Response response = restClient.performRequest("DELETE", ENDPOINT_BASE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
