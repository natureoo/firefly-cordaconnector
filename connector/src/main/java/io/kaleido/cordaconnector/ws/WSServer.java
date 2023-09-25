package io.kaleido.cordaconnector.ws;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

public class WSServer {
    private static WebSocketClient ws = null;
    public static void main(String[] args) {

        try {
            ws = new WebSocketClient(new URI("ws://localhost:8080/ws")) {
                private Timer heartBeatTimer;
                private TimerTask heartBeatTask;

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    ws.send("{\"type\":\"listen\",\"topic\":\"eventstream-0-topic\"}");
                    heartBeat();
                }

                @Override
                public void onMessage(String message) {
                    String[] data = message.split("\n");
                    for (int i = 0; i < data.length; i++) {
                        System.out.println(data[i]);
                    }
                    ws.send("{\"type\":\"ack\",\"topic\":\"eventstream-0-topic\"}");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Event stream websocket disconnected");
                }

                @Override
                public void onError(Exception ex) {
                    System.out.println("Event stream websocket error. " + ex.getMessage());
                }

                private void heartBeat() {
//                    ws.sendPing();
//                    if (heartBeatTimer != null) {
//                        heartBeatTimer.cancel();
//                    }
//                    heartBeatTimer = new Timer();
//                    heartBeatTask = new TimerTask() {
//                        @Override
//                        public void run() {
//                            System.out.println("Event stream ping timeout");
//                            ws.close();
//                        }
//                    };
//                    heartBeatTimer.schedule(heartBeatTask, 2000);
                }

                @Override
                public void onWebsocketPong(WebSocket conn, Framedata f)  {
                    heartBeat();
                }
            };
            ws.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
