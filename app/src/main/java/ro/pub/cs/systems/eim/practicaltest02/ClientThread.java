package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {

    private final String address;
    private final String url;
    private final int port;
    private final String info;
    private final TextView infoTextView;

    private Socket socket;

    public ClientThread(String address, String url, int port, String info, TextView infoTextView) {
        this.address = address;
        this.url = url;
        this.port = port;
        this.info = info;
        this.infoTextView = infoTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            printWriter.println(url);
            printWriter.flush();
            printWriter.println(info);
            printWriter.flush();
            String info;

            while ((info = bufferedReader.readLine()) != null) {
                final String finalizedinfo = info;

                infoTextView.post(() -> infoTextView.setText(finalizedinfo));
            }
        }
        catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
