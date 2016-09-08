package layout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import elevenpath.latchmyphone.ContextLatchWimp;
import elevenpath.latchmyphone.R;
import elevenpath.latchmyphone.WimpService;


public class ServiceFragment extends Fragment {

    View view = null;
    Button startService;
    Button stopService;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle
                                     savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_service,
                container, false);

        initializeControls();

        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartServiceLatch();
            }
        });

        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopServiceLatch();
            }
        });

        return view;
    }

    private void stopServiceLatch() {
        WimpService service = new WimpService();
        service.stopService(new Intent(getContext(), WimpService.class));
    }

    private void StartServiceLatch() {
        ContextLatchWimp.getInstance().accountId = ContextLatchWimp.getInstance().loadDataPreferenceString("accountId");
        ContextLatchWimp.getInstance().service = new WimpService();

        ContextLatchWimp.getInstance().service.startService(new Intent(view.getContext(), WimpService.class));
    }

    private void initializeControls() {

        startService = (Button) view.findViewById(R.id.buttonStart);
        stopService  = (Button) view.findViewById(R.id.buttonStop);

    }
}