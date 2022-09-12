package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.List;

public class RemovingDepartments extends BaseApiRequests {

    private static Logger logger = Logger.getLogger(RemovingDepartments.class);

    public void main(String tenantId, String agentId, String urlPlatform, String domain) {
        logger.setLevel(Level.INFO);
        String authURL =getAuthURL(tenantId,agentId,urlPlatform,domain);
        int idsQuantity = 0;
        do {
            List<String> departmentsIds = getDepartments(authURL, urlPlatform);
            idsQuantity = departmentsIds.size();

            logger.info("Departments quantity: " + departmentsIds.size());

            int i = 1;
            String jwt = getJWT(authURL);
            for (String departmentsId : departmentsIds) {
                logger.info("Closing " + (i++) + " of " + idsQuantity + " departments");
                Response response = closeDepartment(jwt, departmentsId, urlPlatform);
                if (response.getStatusCode() != 200) {
                    AllureLogger.logToAllure(response.getBody().asString());
                    logger.info(response.getBody().asString());
                    jwt = getJWT(authURL);
                    closeDepartment(jwt, departmentsId, urlPlatform);
                }
            }

        } while (idsQuantity > 0);

    }

    public static Response closeDepartment(String jwt, String departmentId, String urlPlatform) {
        return RestAssured.given()
                .accept(ContentType.JSON)
                .header("Authorization",
                        jwt)
                .delete(String.format("%s/api/departments/%s", urlPlatform, departmentId));
    }


    public List<String> getDepartments(String authURL, String urlPlatform) {
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .header("Authorization", getJWT(authURL))
                .get(urlPlatform + "/api/departments");
        return response.getBody().jsonPath().getList("id");
    }
}
