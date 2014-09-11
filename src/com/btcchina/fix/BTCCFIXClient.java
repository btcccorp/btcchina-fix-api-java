package com.btcchina.fix;

import java.io.IOException;
import java.io.InputStream;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.DoNotSend;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

/**
 * BTCChina FIX API Client 
 * @author BTCChina
 */
public class BTCCFIXClient {

	public static void main(String args[]) throws ConfigError, DoNotSend, IOException, SessionNotFound, InterruptedException{
		BTCCFIXClientApp app = new BTCCFIXClientApp();
	    InputStream inputStream = BTCCFIXClient.class.getResourceAsStream("/quickfix-client.properties");
	    SessionSettings settings = new SessionSettings(inputStream);
	    MessageStoreFactory storeFactory = new FileStoreFactory(settings);
	    LogFactory logFactory = new FileLogFactory(settings);
	    MessageFactory messageFactory = new DefaultMessageFactory();
	    Initiator initiator = new SocketInitiator(app, storeFactory, settings, logFactory, messageFactory);
	    initiator.block();
	}
}
