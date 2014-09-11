package com.btcchina.fix;

import quickfix.Message;
import quickfix.field.MDEntryType;
import quickfix.field.MDReqID;
import quickfix.field.MarketDepth;
import quickfix.field.SubscriptionRequestType;
import quickfix.field.Symbol;

/**
 * MarkertData Request
 * @author BTCChina
 */
public class BTCCMarketDataRequest {
	/**
	 * MARKET DATA SNAPSHOT FULL REFRESH REQUEST (V)
	 * @return @tickerRequest request message
	 */
	public static Message marketDataFullSnapRequest(String symbol) {
		quickfix.fix44.MarketDataRequest tickerRequest = new quickfix.fix44.MarketDataRequest();
		
		quickfix.fix44.MarketDataRequest.NoRelatedSym noRelatedSym = new quickfix.fix44.MarketDataRequest.NoRelatedSym();
		noRelatedSym.set(new Symbol(symbol));
		tickerRequest.addGroup(noRelatedSym);
				
		tickerRequest.set(new MDReqID("123"));		
		tickerRequest.set(new SubscriptionRequestType('0'));
		tickerRequest.set(new MarketDepth(0));
		
		addMDType(tickerRequest, '0');
		addMDType(tickerRequest, '1');
		addMDType(tickerRequest, '2');
		addMDType(tickerRequest, '3');
		addMDType(tickerRequest, '4');
		addMDType(tickerRequest, '5');
		addMDType(tickerRequest, '6');
		addMDType(tickerRequest, '7');
		addMDType(tickerRequest, '8');
		addMDType(tickerRequest, '9');
		addMDType(tickerRequest, 'A');
		addMDType(tickerRequest, 'B');
		addMDType(tickerRequest, 'C');
	   	    
	    return tickerRequest;
	}

	/**
	 * MARKET DATA INCREMENTAL REFRESH REQUEST (V)
	 * @return @tickerRequest request message
	 */
	public static Message marketDataIncrementalRequest(String symbol) {
		quickfix.fix44.MarketDataRequest tickerRequest = new quickfix.fix44.MarketDataRequest();
		
		quickfix.fix44.MarketDataRequest.NoRelatedSym noRelatedSym = new quickfix.fix44.MarketDataRequest.NoRelatedSym();
		noRelatedSym.set(new Symbol(symbol));
		tickerRequest.addGroup(noRelatedSym);
				
		tickerRequest.set(new MDReqID("123"));		
		tickerRequest.set(new SubscriptionRequestType('1'));
		tickerRequest.set(new MarketDepth(0));
		
		addMDType(tickerRequest, '0');
		addMDType(tickerRequest, '1');
		addMDType(tickerRequest, '2');
		addMDType(tickerRequest, '3');
		addMDType(tickerRequest, '4');
		addMDType(tickerRequest, '5');
		addMDType(tickerRequest, '6');
		addMDType(tickerRequest, '7');
		addMDType(tickerRequest, '8');
		addMDType(tickerRequest, '9');
		addMDType(tickerRequest, 'A');
		addMDType(tickerRequest, 'B');
		addMDType(tickerRequest, 'C');
	   	    
	    return tickerRequest;
	}
	
	/**
	 * UNSUBSCRIBE MARKET DATA INCREMENTAL REFRESH (V)
	 * @return @tickerRequest request message
	 */
	public static Message unsubscribeIncrementalRequest(String symbol) {
		quickfix.fix44.MarketDataRequest tickerRequest = new quickfix.fix44.MarketDataRequest();
		
		quickfix.fix44.MarketDataRequest.NoRelatedSym noRelatedSym = new quickfix.fix44.MarketDataRequest.NoRelatedSym();
		noRelatedSym.set(new Symbol(symbol));
		tickerRequest.addGroup(noRelatedSym);
				
		tickerRequest.set(new MDReqID("123"));		
		tickerRequest.set(new SubscriptionRequestType('2'));
		tickerRequest.set(new MarketDepth(0));
		
		addMDType(tickerRequest, '0');
		addMDType(tickerRequest, '1');
		addMDType(tickerRequest, '2');
		addMDType(tickerRequest, '3');
		addMDType(tickerRequest, '4');
		addMDType(tickerRequest, '5');
		addMDType(tickerRequest, '6');
		addMDType(tickerRequest, '7');
		addMDType(tickerRequest, '8');
		addMDType(tickerRequest, '9');
		addMDType(tickerRequest, 'A');
		addMDType(tickerRequest, 'B');
		addMDType(tickerRequest, 'C');
	   	    
	    return tickerRequest;
	}

	private static void addMDType(quickfix.fix44.MarketDataRequest tickerRequest, char type) {
		quickfix.fix44.MarketDataRequest.NoMDEntryTypes g0 = new quickfix.fix44.MarketDataRequest.NoMDEntryTypes();
		g0.set(new MDEntryType(type));
	    tickerRequest.addGroup(g0);
	}
}
