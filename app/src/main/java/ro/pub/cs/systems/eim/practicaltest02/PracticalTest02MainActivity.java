package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private EditText portEditText = null;
    private EditText urlEditText = null;
    private EditText infoEditText = null;
    private TextView infoTextView = null;
    private Button getButton = null;
    private Button connectButton = null;
    private EditText addressEditText = null;
    private ServerThread serverThread = null;

    private final GetInfoClickListener getButtonClickListener = new GetInfoClickListener();

    private final ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();

    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = portEditText.getText().toString();
            if (serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                System.out.println("wtffsfaffsfsfsfsfdas ce naibaa");
                return;
            }
            serverThread.start();
        }
    }


    private class GetInfoClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            String clientAddress = addressEditText.getText().toString();
            String url = urlEditText.getText().toString();
            String port = portEditText.getText().toString();
            if (clientAddress.isEmpty() || url.isEmpty() || port.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String info = infoEditText.getText().toString();
            if (info.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            infoTextView.setText(Constants.EMPTY_STRING);

            ClientThread clientThread = new ClientThread(clientAddress, url, Integer.parseInt(port), info, infoEditText);
            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");

        portEditText = findViewById(R.id.port_edit_text);
        urlEditText = findViewById(R.id.url_edit_text);
        addressEditText = findViewById(R.id.address_edit_text);
        infoEditText = findViewById(R.id.info_edit_text);
        infoTextView = findViewById(R.id.info_shown_text_view);
        getButton = findViewById(R.id.get_button);
        connectButton = findViewById(R.id.connect_button);
        getButton.setOnClickListener(getButtonClickListener);
        connectButton.setOnClickListener(connectButtonClickListener);
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}