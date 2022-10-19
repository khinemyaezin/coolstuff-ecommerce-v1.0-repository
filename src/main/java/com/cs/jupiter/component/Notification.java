package com.cs.jupiter.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.cs.jupiter.model.jun.ChatData;

@Component
public class Notification {
	@Autowired
	public Notification(SocketIOServer server) {
		server.addConnectListener(onConnected());
		server.addEventListener("chatevent", ChatData.class, new DataListener<ChatData>() {
			@Override
			public void onData(SocketIOClient client, ChatData data, AckRequest ackSender) throws Exception {
                server.getBroadcastOperations().sendEvent("chatevent", data);
				
			}
		});

	}
	private ConnectListener onConnected() {
        return client -> {
            HandshakeData handshakeData = client.getHandshakeData();
           System.out.println("Client[{}] - Connected to chat module through '{}'" + client.getSessionId().toString() + " : " + handshakeData.getUrl());
        };
    }
	
}
