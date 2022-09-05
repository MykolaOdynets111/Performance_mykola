package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.List;

public class ClosingChats {
    private static Logger logger = Logger.getLogger(ClosingChats.class);

    public void main(String tenantId, String agentId, String urlPlatform, String domain) {
        logger.setLevel(Level.INFO);
        String authURL = urlPlatform + "/internal/auth/fake-auth-token?agentId=" + agentId + "&tenantId=" + tenantId + "&domain=." + domain + "&createFakeMc2Token=true";
        int idsQuantity = 0;
        do {
            List<String> conversationIds = getChats(authURL, urlPlatform);
            idsQuantity = conversationIds.size();

            AllureLogger.logToAllure("New chats quantity: " + conversationIds.size());
            logger.info("New chats quantity: " + conversationIds.size());

            int i = 1;
            String jwt = getJWT(authURL);
            for (String conversationId : conversationIds) {
                logger.info("Closing " + (i++) + " of " + idsQuantity + " chats");
                Response response = closeChats(jwt, conversationId, urlPlatform);
                if (response.getStatusCode() != 200) {
                    AllureLogger.logToAllure(response.getBody().asString());
                    logger.info(response.getBody().asString());
                    jwt = getJWT(authURL);
                    closeChats(jwt, conversationId, urlPlatform);
                }
            }

        } while (idsQuantity > 0);

    }

    public String getJWT(String authURL) {
        return RestAssured.given().contentType(ContentType.JSON).get(authURL).getBody().jsonPath().getString("jwt");
    }

    public static Response closeChats(String jwt, String conversationId, String urlPlatform) {
        return RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization",
                        jwt)
                .get(String.format("%s/api/chats/close-chat-by-id?chatId=%s", urlPlatform, conversationId));
    }

    public List<String> getChats(String authURL, String urlPlatform) {
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .header("Authorization", getJWT(authURL))
                .body("{\n" +
                        "  \"chatStates\": [\n" +
                        "    \"LIVE_IN_SCHEDULER_QUEUE\",\n" +
                        "    \"LIVE_ASSIGNED_TO_AGENT\"\n" +
                        "  ]\n" +
                        "}")
                .post(urlPlatform + "/api/chats/search?page=0&size=200");
        return response.getBody().jsonPath().getList("content.chatId");
    }
}
