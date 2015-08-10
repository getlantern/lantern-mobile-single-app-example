package org.getlantern.flashlighttester;

import go.client.Client;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import org.getlantern.flashlighttester.R;

public class MainActivity extends Activity {

    private Button killButton;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        // Initializing application context.
        //.init(getApplicationContext());

        killButton = (Button)findViewById(R.id.stopProxyButton);
        startButton = (Button)findViewById(R.id.startProxyButton);

        // Disabling stop button.
        killButton.setEnabled(false);

        // Enabling proxy button.
        startButton.setEnabled(true);
    }

    public void stopProxyButtonOnClick(View v) {

        Log.v("DEBUG", "Attempt to stop running proxy.");
        try {
            Client.StopClientProxy();
        } catch (Exception e) {
            throw new RuntimeException(e);
        };

        Log.v("DEBUG", "Proxy successfully stopped");

        // Disabling stop button.
        killButton.setEnabled(false);

        // Enabling proxy button.
        startButton.setEnabled(true);

    }

    public void startProxyButtonOnClick(View v) {
        Log.v("DEBUG", "Attempt to run client proxy on :9192");

        try {
            Client.RunClientProxy("0.0.0.0:9192",
                                  "FlashlightTester",
                                  new Client.GoCallback.Stub() {
                                      @Override
                                      public void Do() {
                                      }
                                  });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Log.v("DEBUG", "RunClientProxy done on :9192");

        // Enabling stop button.
        killButton.setEnabled(true);

        // Disabling proxy button.
        startButton.setEnabled(false);
    }
}
