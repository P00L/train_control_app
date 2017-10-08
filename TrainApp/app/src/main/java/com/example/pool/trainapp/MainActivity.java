package com.example.pool.trainapp;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.triggertrap.seekarc.SeekArc;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private SeekArc mSeekArc;
    private SeekArc mSeekArc1;
    private WebSocketClient mWebSocketClient;
    private TextView mSeekArcProgress;
    private TextView mSeekArcProgress1;
    private ImageView imageView;
    private Boolean direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSeekArc = (SeekArc) findViewById(R.id.seekArc);
        mSeekArcProgress = (TextView) findViewById(R.id.seekArcProgress);
        mSeekArc1 = (SeekArc) findViewById(R.id.seekArc1);
        mSeekArcProgress1 = (TextView) findViewById(R.id.seekArcProgress1);
        imageView = (ImageView) findViewById(R.id.imageView);

        direction = Boolean.TRUE;
        mSeekArc1.setProgressColor(Color.parseColor("#00ffff"));
        mSeekArc1.setArcColor(Color.parseColor("#c2c2c2"));
        // connect to socket
        //TODO spostare allo start dell'applicazione
        //connectWebSocket();

        mSeekArc.setStartAngle(0);
        mSeekArc.setSweepAngle(160);
        mSeekArc.setArcRotation(280);

        mSeekArc1.setStartAngle(0);
        mSeekArc1.setSweepAngle(160);
        mSeekArc1.setArcRotation(280);

        mSeekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onProgressChanged(SeekArc seekArc, int progress,
                                          boolean fromUser) {
                mSeekArcProgress.setText(String.valueOf(((255 - 0) * (progress - 0)) / (100 - 0) + 0));
            }
        });

        mSeekArc1.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {
            }

            @Override
            public void onProgressChanged(SeekArc seekArc, int progress,
                                          boolean fromUser) {
                mSeekArcProgress1.setText(String.valueOf(((255 - 0) * (progress - 0)) / (100 - 0) + 0));
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (direction) {
                    direction = Boolean.FALSE;
                    mSeekArc1.setEnabled(Boolean.FALSE);
                    mSeekArc1.setArcColor(Color.parseColor("#ff0000"));
                    mSeekArc1.setProgress(0);
                    mSeekArcProgress1.setEnabled(Boolean.FALSE);
                    imageView.animate().rotation(180).start();
                    sendMessage("SX");
                }
                else{
                    direction = Boolean.TRUE;
                    mSeekArc1.setEnabled(Boolean.TRUE);
                    mSeekArc1.setProgressColor(Color.parseColor("#00ffff"));
                    mSeekArc1.setArcColor(Color.parseColor("#c2c2c2"));
                    mSeekArcProgress1.setEnabled(Boolean.TRUE);
                    imageView.animate().rotation(0).start();
                    sendMessage("DX");
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void connectWebSocket() {
        Log.i("Websocket", "Connect");
        URI uri;
        try {
            uri = new URI("ws://192.168.4.1:81");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String s) {
                Log.i("Websocket", "message " + s);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

    public void sendMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        //mWebSocketClient.send(message);
    }
}
