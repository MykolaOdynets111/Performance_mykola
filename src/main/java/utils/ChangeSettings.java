package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.testng.annotations.Parameters;

public class ChangeSettings extends BaseApiRequests {
    private static Logger logger = Logger.getLogger(ChangeSettings.class);


    public void uncheckSupportHours(String tenantId, String agentId, String urlPlatform, String domain) {
        logger.setLevel(Level.INFO);
        String authURL = getAuthURL(tenantId, agentId, urlPlatform, domain);
        String jwt = getJWT(authURL);
       Response response = RestAssured.given().contentType(ContentType.JSON)
                .header("Authorization", jwt)
                .body(getUncheckAllHoursBody())
                .post(urlPlatform + "/api/support-hours");
        System.out.println(response.getBody());
    }

    public void resetSupportHours(String tenantId, String agentId, String urlPlatform, String domain) {
        logger.setLevel(Level.INFO);
        String authURL = getAuthURL(tenantId, agentId, urlPlatform, domain);
        String jwt = getJWT(authURL);
        RestAssured.given().contentType(ContentType.JSON)
                .header("Authorization", jwt)
                .body(getResetSupportHoursBody())
                .post(urlPlatform + "/api/support-hours");
    }

    private String getUncheckAllHoursBody() {
        return (
                "{\"agentSupportHours\":[{\"startWorkTime\":\"00:00\",\"endWorkTime\":\"23:59\",\"days\":[\"SUNDAY\"]}],\"supportHoursByDepartment\":[]}"
        );

    }

    private String getResetSupportHoursBody() {
        return ("{\"agentSupportHours\":[],\"supportHoursByDepartment\":[]}");
    }

}
