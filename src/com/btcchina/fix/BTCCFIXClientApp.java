package com.btcchina.fix;

import java.io.IOException;
import org.apache.log4j.Logger;
import com.btcchina.fix.util.MessagePrinter;
import quickfix.ConfigError;
import quickfix.DataDictionary;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.field.*;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataSnapshotFullRefresh;

/**
 * BTCChina FIX Client Application
 * @author BTCChina
 */
public class BTCCFIXClientApp  implements quickfix.Application {
	private static final Logger log = Logger.getLogger(BTCCFIXClientApp.class);
	
	public void fromAdmin(quickfix.Message msg, SessionID sessionID)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		log.info("receivedType:"+msg.getHeader().getString(35));
		log.info(sessionID+"------ fromAdmin--------"+msg.toString());
	}

	public void fromApp(quickfix.Message msg, SessionID sessionID)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {

		log.info("receivedType:"+msg.getHeader().getString(35));
		log.info(sessionID+"------ fromApp---------"+msg.toString());
	     try
         {
	 		MarketDataIncrementalRefresh msg1= (MarketDataIncrementalRefresh) msg;
			log.warn("In BTCCFIXClientApp::fromApp(quickfix.Message msg, SessionID sessionID)::the header of msg is "+msg1.getGroup(1, quickfix.field.NoMDEntries.FIELD).getString(MDEntryType.FIELD));
	    	 DataDictionary dd = new DataDictionary("FIX44.xml");
	    	 MessagePrinter MP = new MessagePrinter();
	    	 MP.print(dd, msg1);
         }	     
         catch (Exception ex)
         {
        	 log.warn("In BTCCFIXClientApp::fromApp(quickfix.Message msg, SessionID sessionID)::"+ex.getMessage());
        	 MarketDataSnapshotFullRefresh msg2= (MarketDataSnapshotFullRefresh) msg;
        	 log.warn("In BTCCFIXClientApp::fromApp(quickfix.Message msg, SessionID sessionID)::the header of msg is "+msg2.getGroup(1, quickfix.field.NoMDEntries.FIELD).getString(MDEntryType.FIELD));
 	    	 DataDictionary dd2;
			try {
				dd2 = new DataDictionary("FIX44.xml");
				MessagePrinter MP = new MessagePrinter();
				MP.print(dd2, msg2);
			} catch (ConfigError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         }
	}

	public void onCreate(SessionID sessionID) {
		try {
			//there should invoke reset()
			Session.lookupSession(sessionID).reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info(sessionID+"------ onCreate Session-------"+sessionID);
	}
	
	public void onLogon(final SessionID sessionID) {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				quickfix.Message message;

				// MARKET DATA INCREMENTAL REFRESH REQUEST (V)
				message = BTCCMarketDataRequest.marketDataIncrementalRequest("BTCCNY");	
				Session.lookupSession(sessionID).send(message);				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				//MARKET DATA SNAPSHOT FULL REFRESH REQUEST (V)
				message = BTCCMarketDataRequest.marketDataFullSnapRequest("BTCCNY");
				Session.lookupSession(sessionID).send(message);		
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//UNSUBSCRIBE MARKET DATA INCREMENTAL REFRESH (V)
				message = BTCCMarketDataRequest.unsubscribeIncrementalRequest("BTCCNY");	
				Session.lookupSession(sessionID).send(message);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		log.info(sessionID+"------ onLogon-------"+sessionID);
	}

	public void onLogout(SessionID sessionID) {
		log.info(sessionID+"------ onLogout -------"+sessionID);
	}

	public void toAdmin(quickfix.Message msg, SessionID sessionID) {
		log.info(sessionID+"------ toAdmin---------"+msg.toString());
	}

	public void toApp(quickfix.Message msg, SessionID sessionID) throws DoNotSend {
		log.info(sessionID+"------ toApp-----------"+msg.toString());
	}
}
