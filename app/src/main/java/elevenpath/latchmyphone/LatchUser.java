package elevenpath.latchmyphone;

import java.util.*;

/**
 * This class model the API for a Latch User. Every action here is related to a User. This
 * means that a LatchUser object should use a pair UserId/Secret obtained from the Latch Website.
 */
public class LatchUser extends LatchAuth {

    /**
     * Create an instance of the class with the User ID and secret obtained from Eleven Paths
     * @param userId
     * @param secretKey
     */
    public LatchUser(String userId, String secretKey){
        this.appId = userId;
        this.secretKey = secretKey;
    }

    public LatchResponse getSubscription() {
        return HTTP_GET_proxy(new StringBuilder(API_SUBSCRIPTION_URL).toString());
    }

    public LatchResponse createApplication(String name, String twoFactor, String lockOnRequest, String contactPhone, String contactEmail) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("name", name);
        data.put("two_factor", twoFactor);
        data.put("lock_on_request", lockOnRequest);
        data.put("contactEmail", contactEmail);
        data.put("contactPhone", contactPhone);
        return HTTP_PUT_proxy(new StringBuilder(API_APPLICATION_URL).toString(), data);
    }

    public LatchResponse removeApplication(String applicationId) {
        return HTTP_DELETE_proxy(new StringBuilder(API_APPLICATION_URL).append("/").append(applicationId).toString());
    }

    public LatchResponse getApplications() {
        return HTTP_GET_proxy(new StringBuilder(API_APPLICATION_URL).toString());
    }

    public LatchResponse updateApplication(String applicationId, String name, String twoFactor, String lockOnRequest, String contactPhone, String contactEmail) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("name", name);
        data.put("two_factor", twoFactor);
        data.put("lock_on_request", lockOnRequest);
        data.put("contactPhone", contactPhone);
        data.put("contactEmail", contactEmail);
        return HTTP_POST_proxy(new StringBuilder(API_APPLICATION_URL).append("/").append(applicationId).toString(), data);
    }
}
