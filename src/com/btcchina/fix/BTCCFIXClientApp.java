package com.btcchina.fix;

import java.io.IOException;
import org.apache.log4j.Logger;
import com.btcchina.fix.util.MessagePrinter;
import com.btcchina.fix.BTCCTradingRequest;
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
import quickfix.fix44.MessageCracker;
import quickfix.fix44.NewOrderSingle;

/**
 * BTCChina FIX Client
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
	    	 DataDictionary dd = new DataDictionary("FIX44.xml");
	    	 MessagePrinter MP = new MessagePrinter();
	    	 MP.print(dd, (quickfix.fix44.Message)msg);
         }	     
         catch (Exception ex)
         {
        	 log.warn("In BTCCFIXClientApp::fromApp(quickfix.Message msg, SessionID sessionID)::"+ex.getMessage());
 	    	 DataDictionary dd2;
 	    	 try {
				dd2 = new DataDictionary("FIX44.xml");
				MessagePrinter MP = new MessagePrinter();
				MP.print(dd2, (quickfix.fix44.Message)msg);
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
//				message = BTCCMarketDataRequest.marketDataIncrementalRequest("LTCCNY");	
//				message = BTCCMarketDataRequest.marketDataIncrementalRequest("BTCCNY");	
//				Session.lookupSession(sessionID).send(message);				
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
					
				//MARKET DATA SNAPSHOT FULL REFRESH REQUEST (V)
//				message = BTCCMarketDataRequest.marketDataFullSnapRequest("LTCCNY");
//				Session.lookupSession(sessionID).send(message);		
//				try {
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				//UNSUBSCRIBE MARKET DATA INCREMENTAL REFRESH (V)
//				message = BTCCMarketDataRequest.unsubscribeIncrementalRequest("LTCCNY");	
//				Session.lookupSession(sessionID).send(message);
//				try {
//					Thread.sleep(3000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				
				String ACCESS_KEY = "<YOUR ACCESS KEY>";
			    String SECRET_KEY = "<YOUR SECRET KEY>";
			    
			    BTCCTradingRequest tradeRequest=new BTCCTradingRequest();
			    try{
				    
				    //limit order on market BTCCNY/LTCCNY/LTCBTC
//				    message = tradeRequest.createNewOrderSingle(ACCESS_KEY, SECRET_KEY, Side.SELL, OrdType.LIMIT, 10000, 0.0001, "BTCCNY"); // integer not supported??
//				    message = tradeRequest.createNewOrderSingle(ACCESS_KEY, SECRET_KEY, Side.SELL, OrdType.LIMIT, 1001.1, 2.1, "BTCCNY"); //
//				    message = tradeRequest.createNewOrderSingle(ACCESS_KEY, SECRET_KEY, Side.BUY, OrdType.LIMIT, 1000, 0.0001, "BTCCNY"); //
				    
//				    message = tradeRequest.createNewOrderSingle(ACCESS_KEY, SECRET_KEY, Side.SELL, OrdType.LIMIT, 1001.1, 0.001, "LTCCNY"); //
//				    message = tradeRequest.createNewOrderSingle(ACCESS_KEY, SECRET_KEY, Side.BUY, OrdType.LIMIT, 1, 0.001, "LTCCNY"); //
			    					    
				    //market order sell/buy
//				    message = tradeRequest.createNewOrderSingle(ACCESS_KEY, SECRET_KEY, Side.SELL, OrdType.MARKET, 0.0001d, "BTCCNY");
//				    message = tradeRequest.createNewOrderSingle(ACCESS_KEY, SECRET_KEY, Side.BUY, OrdType.MARKET, 0.0001d, "BTCCNY");
			    	
//				    message = tradeRequest.createNewOrderSingle(ACCESS_KEY, SECRET_KEY, Side.SELL, OrdType.MARKET, 0.001d, "LTCCNY");
//				    message = tradeRequest.createNewOrderSingle(ACCESS_KEY, SECRET_KEY, Side.BUY, OrdType.MARKET, 0.001d, "LTCCNY");
			    					    
			    	// cancel an order
//				    message = tradeRequest.createOrderCancelRequest(ACCESS_KEY, SECRET_KEY, 42664407, "BTCCNY");
			    	
			    	//get account info
//				    message = tradeRequest.createUserAccountRequest(ACCESS_KEY, SECRET_KEY);
			    	
			    	//get order
//				    message = tradeRequest.createOrderStatusRequest(ACCESS_KEY, SECRET_KEY, "BTCCNY",42663920);
				    
				    //get 1000 latest open orders
				    message = tradeRequest.createOrderMassStatusRequest(ACCESS_KEY, SECRET_KEY,"BTCCNY");
				    
				    Session.lookupSession(sessionID).send(message);
			    } catch (Exception e){
			    	log.info("Exception in trading request: "+e.toString());
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
