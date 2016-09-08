package layout;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import elevenpath.latchmyphone.ContextLatchWimp;
import elevenpath.latchmyphone.LatchResponse;
import elevenpath.latchmyphone.R;


public class SettingFragment extends Fragment {

    View view = null;
    Button saveButton;
    Button pairButton;
    EditText editTextAppId;
    EditText editTextSecretCode;
    EditText editTextToken;
    CheckBox checkBoxLockPhone;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle
                                     savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting,
                container, false);

        initializeControls();
        getSettingValues();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSetting();
            }
        });

        pairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pairApp();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });

        return view;
    }

    private void pairApp() throws JSONException {

        ContextLatchWimp.getInstance().initializeLatch();
        ContextLatchWimp.getInstance().Token = editTextToken.getText().toString();

        if (!validateInstanceLatch())
        {
            Toast.makeText(getContext(), "Please, verify AppId and Secret Code.", Toast.LENGTH_LONG);
            return;
        }

        LatchResponse response =  ContextLatchWimp.getInstance().service.pairLatch();

        if (response.getData() == null)
        {
            Toast.makeText(getContext(), "Please, try again", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject json = new JSONObject(response.getData().toString());

        String value = json.getString("accountId");
        ContextLatchWimp.getInstance().setDataPreferences("accountId", value);

        if (!value.equals(""))
        {
            ContextLatchWimp.getInstance().setDataPreferences("isPair","true");
            Toast.makeText(getContext(), "Saved data!", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getContext(), "Please, try again", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateInstanceLatch() {
        return ContextLatchWimp.getInstance().latchInstance != null;
    }

    private void getSettingValues() {

        String appId      = ContextLatchWimp.getInstance().loadDataPreferenceString("aplicationId");
        String secretCode = ContextLatchWimp.getInstance().loadDataPreferenceString("aplicationSecretCode");

        if (!appId.equals("") && !secretCode.equals(""))
        {
            editTextAppId.setText(appId);
            editTextSecretCode.setText(secretCode);
        }

        String isPair = ContextLatchWimp.getInstance().loadDataPreferenceString("isPair");

        if (!isPair.equals(""))
        {
            pairButton.setEnabled(true);
        }
    }

    private void initializeControls() {
        saveButton         = (Button) view.findViewById(R.id.buttonSaveValues);
        editTextAppId      = (EditText) view.findViewById(R.id.editTextAppId);
        editTextSecretCode = (EditText) view.findViewById(R.id.editTextSecretCode);
        pairButton         = (Button) view.findViewById(R.id.buttonPair);
        editTextToken      = (EditText) view.findViewById(R.id.editTextToken);
        checkBoxLockPhone  = (CheckBox) view.findViewById(R.id.checkBoxLockPhone);
    }

    private void saveSetting() {

        String appId      = editTextAppId.getText().toString();
        String secretCode = editTextSecretCode.getText().toString();
        boolean isCheck    = checkBoxLockPhone.isChecked();
        String isCheckValue;

        ContextLatchWimp.getInstance().setDataPreferences("aplicationId",appId);
        ContextLatchWimp.getInstance().setDataPreferences("aplicationSecretCode",secretCode);

        if (isCheck)
            isCheckValue = "true";
        else
            isCheckValue = "false";

        ContextLatchWimp.getInstance().setDataPreferences("lockAgain", isCheckValue);

        ContextLatchWimp.getInstance().service.initializeLatch(appId, secretCode);

        Toast.makeText(getContext(), "Saved data!", Toast.LENGTH_LONG).show();

    }


}