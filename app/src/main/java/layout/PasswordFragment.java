package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import elevenpath.latchmyphone.ContextLatchWimp;
import elevenpath.latchmyphone.R;


public class PasswordFragment extends Fragment {

    View view = null;
    EditText editTextPassword;
    EditText editTextMessageUser;
    Button buttonSave;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle
                                     savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_password,
                container, false);

        initializeControls();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        loadData();

        return view;
    }

    private void loadData() {
        editTextPassword.setText(ContextLatchWimp.getInstance().loadDataPreferenceString("password"));
        editTextMessageUser.setText(ContextLatchWimp.getInstance().loadDataPreferenceString("messageUser"));
    }

    private void saveData() {
        ContextLatchWimp.getInstance().setDataPreferences("password", editTextPassword.getText().toString());
        ContextLatchWimp.getInstance().setDataPreferences("messageUser", editTextMessageUser.getText().toString());

        Toast.makeText(getContext(), "Success.", Toast.LENGTH_LONG).show();
    }

    private void initializeControls() {

        editTextPassword    = (EditText) view.findViewById(R.id.editTextPassword);
        editTextMessageUser = (EditText) view.findViewById(R.id.editTextMessageUser);
        buttonSave          = (Button)   view.findViewById(R.id.buttonSave);
    }


}