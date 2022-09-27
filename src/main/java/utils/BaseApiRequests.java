package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Parameters;

public abstract class BaseApiRequests {
    protected String getJWT(String authURL) {
        return RestAssured.given().contentType(ContentType.JSON).get(authURL).getBody().jsonPath().getString("jwt");
    }

    @Parameters({"tenantId", "agentId", "urlPlatform", "domain"})
    protected String getAuthURL(String tenantId, String agentId, String urlPlatform, String domain) {
        return urlPlatform + "/internal/auth/fake-auth-token?agentId=" + agentId + "&tenantId=" + tenantId + "&domain=." + domain + "&createFakeMc2Token=true";
    }

}
