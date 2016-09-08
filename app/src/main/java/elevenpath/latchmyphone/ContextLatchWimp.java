package elevenpath.latchmyphone;

import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by jsperk on 04/08/2016.
 */
public class ContextLatchWimp {
    private static ContextLatchWimp ourInstance = new ContextLatchWimp();

    public static ContextLatchWimp getInstance() {
        return ourInstance;
    }

    private ContextLatchWimp() {

    }

    public String Token;
    public LatchApp latchInstance;
    public WimpService service;
    public String accountId;
    public MainActivity ActivityContext;

    public String loadDataPreferenceString(String valueKey) {
        SharedPreferences prefs = ActivityContext.getSharedPreferences("LatchValues", ActivityContext.MODE_PRIVATE);
        String restoredText = prefs.getString(valueKey, "");
        return restoredText;
    }

    public void setDataPreferences(String valueKey, String value) {
        SharedPreferences.Editor editor = ActivityContext.getSharedPreferences("LatchValues", ActivityContext.MODE_PRIVATE).edit();
        editor.putString(valueKey, value);
        editor.commit();
    }

    public void initializeLatch()
    {
        String appId      = ContextLatchWimp.getInstance().loadDataPreferenceString("aplicationId");
        String secretCode = ContextLatchWimp.getInstance().loadDataPreferenceString("aplicationSecretCode");

        ContextLatchWimp.getInstance().service.initializeLatch(appId, secretCode);
    }
}
