package elevenpath.latchmyphone;

import android.app.ActivityManager;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class WimpService extends Service {
    public WimpService() {

    }

    DevicePolicyManager deviceManger;
    ActivityManager activityManager;
    ComponentName compName;

    public Timer t = new Timer();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        setTimerExecute();


        return START_STICKY;
    }

    public void initializeLatch(String idApp, String secretCode) {
        ContextLatchWimp.getInstance().latchInstance = new LatchApp(idApp, secretCode);
    }

    public LatchResponse pairLatch()
    {
        LatchResponse response = null;
        try
        {
            response = ContextLatchWimp.getInstance().latchInstance.pair(ContextLatchWimp.getInstance().Token);
        }
        catch(Exception ex)
        {
            Log.e("ErroPairApp",ex.getMessage());
        }

        return response;
    }

    public LatchResponse unPairLatch(String value)
    {
        return  ContextLatchWimp.getInstance().latchInstance.unpair(value);
    }

    public LatchResponse getStatus(String accountId) {

        return ContextLatchWimp.getInstance().latchInstance.status(accountId);
    }

    @Override
    public void onDestroy() {
        t.cancel();
        lockWindowLatch();
        super.onDestroy();
    }


    private void setTimerExecute() {

        t.scheduleAtFixedRate(new TimerTask() {
                                  @Override
                                  public void run() {
                                      try {
                                          ContextLatchWimp.getInstance().setDataPreferences("serviceRun","true");
                                          executeCallService();
                                      } catch (JSONException e) {
                                          e.printStackTrace();
                                      }
                                  }
                              }
                , 0, 10000);
    }

    private int countIterations = 0;

    private void executeCallService() throws JSONException {

        LatchResponse response = null;
        String accountId = ContextLatchWimp.getInstance().loadDataPreferenceString("accountId");

        if (accountId != "") {
            response = getStatus(accountId);
        }

        JSONObject json = new JSONObject(response.getData().toString());

        JSONObject valueJson = json.getJSONObject("operations");

        String result = valueJson.getJSONObject(ContextLatchWimp.getInstance().loadDataPreferenceString("aplicationId")).get("status").toString();

        if (result.equals("off")) {
            String lockAgain = ContextLatchWimp.getInstance().loadDataPreferenceString("lockAgain");
            lockWindowLatch();
            if (lockAgain.equals("true"))
                onDestroy();
        } else
        {

        }

    }

    public void lockWindowLatch() {
        Context context = getApplicationContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //Lock device
        DevicePolicyManager mDPM;
        mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);


        String pass = ContextLatchWimp.getInstance().loadDataPreferenceString("password");

        if (!pass.equals(""))
        {
            mDPM.resetPassword(pass, DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
        }


        mDPM.lockNow();
    }

}

