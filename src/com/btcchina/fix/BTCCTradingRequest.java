package com.btcchina.fix;

//import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import quickfix.Message;
import quickfix.field.Account;
import quickfix.field.ClOrdID;
import quickfix.field.MassStatusReqID;
import quickfix.field.MassStatusReqType;
import quickfix.field.OrdType;
import quickfix.field.OrderID;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;
import quickfix.fix44.NewOrderSingle;

public class BTCCTradingRequest {
	
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	public NewOrderSingle createNewOrderSingle(String accesskey, String secretkey, char side, char ordertype, double price, double amount, String symbol) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportMarketException
	{
		String methodstr = null;
		if(side == Side.BUY)
		{
			methodstr = this.toBuyOrder3ParamString(price, amount, symbol);
		}else if(side == Side.SELL)
		{
			methodstr = this.toSellOrder3ParamString(price, amount, symbol);
		}
		String accountString = this.getAccountString(accesskey, secretkey, methodstr);
        NewOrderSingle newOrderSingleRequest = new NewOrderSingle();
        newOrderSingleRequest.set(new Account(accountString));
        newOrderSingleRequest.set(new ClOrdID("5"));
        newOrderSingleRequest.set(new OrderQty(amount));
        newOrderSingleRequest.set(new OrdType(ordertype));
        //如果买入 ,OrdType 为1 price 意思为市价单 买30块钱的币  OrderQty无意义
        //如果买入, OrdType 为2 price 意思 买币单价为30  OrderQty表示购买数量
        //如果卖出, OrdType 为1 price 无含义,意思为市价卖,OrderQty为卖出数量
        //如果卖出, OrdType 为2 price 为卖出单价 OrderQty为卖出数量
        newOrderSingleRequest.set(new Price(price));
        newOrderSingleRequest.set(new Side(side));
        newOrderSingleRequest.set(new Symbol(symbol));
        newOrderSingleRequest.set(new TransactTime());
        
        System.out.println("accountString : " + accountString);
        System.out.println("methodstr : " + methodstr);
        return newOrderSingleRequest;
    }
	
	public NewOrderSingle createNewOrderSingle(String accesskey, String secretkey, char side, char ordertype, double amount, String symbol) throws InvalidKeyException, NoSuchAlgorithmException, UnsupportMarketException
	{
//		Double price=null;
		String methodstr = null;
		if(side == Side.BUY)
		{
			methodstr = this.toBuyOrder3ParamString(null, amount, symbol);
		}else if(side == Side.SELL)
		{
			methodstr = this.toSellOrder3ParamString(null, amount, symbol);
		}
		String accountString = this.getAccountString(accesskey, secretkey, methodstr);
        NewOrderSingle newOrderSingleRequest = new NewOrderSingle();
        newOrderSingleRequest.set(new Account(accountString));
        newOrderSingleRequest.set(new ClOrdID("ClOrdID"));
        newOrderSingleRequest.set(new OrderQty(amount));
        newOrderSingleRequest.set(new OrdType(ordertype));
        //如果买入 ,OrdType 为1 price 意思为市价单 买30块钱的币  OrderQty无意义
        //如果买入, OrdType 为2 price 意思 买币单价为30  OrderQty表示购买数量
        //如果卖出, OrdType 为1 price 无含义,意思为市价卖,OrderQty为卖出数量
        //如果卖出, OrdType 为2 price 为卖出单价 OrderQty为卖出数量
//        newOrderSingleRequest.set(new Price());
        newOrderSingleRequest.set(new Side(side));
        newOrderSingleRequest.set(new Symbol(symbol));
        newOrderSingleRequest.set(new TransactTime());
        
        System.out.println("accountString : " + accountString);
        System.out.println("methodstr : " + methodstr);
        return newOrderSingleRequest;
    }
	
	/**
	 * 取消订单请求
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public Message createOrderCancelRequest(String accesskey, String secretkey, int orderid, String market) throws InvalidKeyException, NoSuchAlgorithmException {
		quickfix.fix44.OrderCancelRequest orderCancelRequest = new quickfix.fix44.OrderCancelRequest();
		
		orderCancelRequest.set(new ClOrdID("2"));
		orderCancelRequest.set(new OrigClOrdID("1231234"));//必填，但无意义
		orderCancelRequest.set(new Side(Side.SELL));//必填，但无意义
		
		String methodstr = this.toCancelOrderParamString(orderid, market);
		String accountString = this.getAccountString(accesskey, secretkey, methodstr);
		System.out.println("accountString : " + accountString);
        System.out.println("methodstr : " + methodstr);
		orderCancelRequest.set(new Account(accountString));
		orderCancelRequest.set(new Symbol(market));
		orderCancelRequest.set(new OrderID(String.valueOf(orderid)));//订单编号
		orderCancelRequest.set(new TransactTime(new Date()));
	    return orderCancelRequest;
    }
	
	/**
	 * 订单状态请求
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public Message createOrderMassStatusRequest(String accesskey, String secretkey, String market) throws InvalidKeyException, NoSuchAlgorithmException {
		quickfix.fix44.OrderMassStatusRequest orderMassStatusRequest = new quickfix.fix44.OrderMassStatusRequest();
		orderMassStatusRequest.set(new MassStatusReqID("2123413"));//查询的订单ID
		orderMassStatusRequest.set(new MassStatusReqType(MassStatusReqType.STATUS_FOR_ALL_ORDERS));
		orderMassStatusRequest.set(new Side(Side.BUY));//必填，但无意义
		
		String methodstr = this.toGetOrdersParamString(market);
		String accountString = this.getAccountString(accesskey, secretkey, methodstr);
		orderMassStatusRequest.set(new Account(accountString));
		orderMassStatusRequest.set(new Symbol("BTCCNY"));
		return orderMassStatusRequest;
	}
	
	public Message createOrderStatusRequest(String accesskey, String secretkey, String market, Integer orderid) throws InvalidKeyException, NoSuchAlgorithmException {
		quickfix.fix44.OrderStatusRequest orderStatusRequest = new quickfix.fix44.OrderStatusRequest();
		
		String methodstr = this.toGetOrderParamString(orderid, market);
		String accountString = this.getAccountString(accesskey, secretkey, methodstr);
		
		orderStatusRequest.set(new Account(accountString));
		orderStatusRequest.set(new Symbol(market));
		orderStatusRequest.set(new OrderID(String.valueOf(orderid)));//订单编号
		
		orderStatusRequest.set(new ClOrdID("5"));
		orderStatusRequest.set(new Side(Side.BUY));//必填，但无意义
		return orderStatusRequest;
	}
	
	/**
	 * 账户信息请求 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public Message createUserAccountRequest(String accesskey, String secretkey) throws InvalidKeyException, NoSuchAlgorithmException {
		com.btcchina.fix.AccountInfoRequest accountInfoRequest = new com.btcchina.fix.AccountInfoRequest();
		
		String methodstr = this.toGetAccountInfoParamString();
		String accountString = this.getAccountString(accesskey, secretkey, methodstr);
			
		accountInfoRequest.set(new Account(accountString));
		accountInfoRequest.set(new com.btcchina.fix.AccReqID("123"));
	    return accountInfoRequest;
    }
		
	String getAccountString(String accesskey, String secretkey, String methodstr) throws InvalidKeyException, NoSuchAlgorithmException
	{	
		String tonce = "" + (System.currentTimeMillis() * 1000);	
		String params = "tonce=" + tonce.toString() + "&accesskey=" + accesskey + "&requestmethod=post&id=1&" + methodstr;
	
		String hash = getSignature(params, secretkey);
		String userpass = accesskey + ":" + hash;
		String basicAuth = "Basic " + DatatypeConverter.printBase64Binary(userpass.getBytes());
	
		return tonce + ":" + basicAuth;			
	}

	String getSignature(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {

		// get an hmac_sha1 key from the raw key bytes
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

		// get an hmac_sha1 Mac instance and initialize with the signing key
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);

		// compute the hmac on input data bytes
		byte[] rawHmac = mac.doFinal(data.getBytes());

		return bytArrayToHex(rawHmac);
	}

	String bytArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder();
		for (byte b : a)
			sb.append(String.format("%02x", b & 0xff));
		return sb.toString();
	}
	
	String toBuyOrder3ParamString(Double price, Double amount, String market) throws UnsupportMarketException {
		if(market != null)
		{
			market = market.toUpperCase();
		}
		String priceString = null;
		String amountString = null;
		if(market == null || market.equals("CNYBTC") || market.equals("BTCCNY"))
		{
			market = "BTCCNY";
			if(price == null)
			{
				priceString = "";
			}else{
				priceString = String.format("%f", price);
			}			
			amountString = String.format("%f", amount);
		}else if(market.equals("CNYLTC") || market.equals("LTCCNY"))
		{
			market = "LTCCNY";
			if(price == null)
			{
				priceString = "";
			}else{
				priceString = String.format("%f", price);
			}
			amountString = String.format("%f", amount);
		}else if(market.equals("BTCLTC") || market.equals("LTCBTC"))
		{
			market = "LTCBTC";
			if(price == null)
			{
				priceString = "";
			}else{
				priceString = String.format("%f", price);
			}
			amountString = String.format("%f", amount);		
		}else{
			throw new UnsupportMarketException();
		}
		
		priceString = truncatTailingZeroes(priceString);
		amountString = truncatTailingZeroes(amountString);
		return "method=buyOrder3&params=" + priceString + "," + amountString + "," + market;
	}

	String toSellOrder3ParamString(Double price, Double amount, String market) throws UnsupportMarketException {
		if(market != null)
		{
			market = market.toUpperCase();
		}
		String priceString = null;
		String amountString = null;
		if(market == null || market.equals("CNYBTC") || market.equals("BTCCNY"))
		{
			market = "BTCCNY";
			if(price == null)
			{
				priceString = "";
			}else{
				priceString = String.format("%f", price);
			}			
			amountString = String.format("%f", amount);
		}else if(market.equals("CNYLTC") || market.equals("LTCCNY"))
		{
			market = "LTCCNY";
			if(price == null)
			{
				priceString = "";
			}else{
				priceString = String.format("%f", price);
			}
			amountString = String.format("%f", amount);
		}else if(market.equals("BTCLTC") || market.equals("LTCBTC"))
		{
			market = "LTCBTC";
			if(price == null)
			{
				priceString = "";
			}else{
				priceString = String.format("%f", price);
			}
			amountString = String.format("%f", amount);		
		}else{
			throw new UnsupportMarketException();
		}
		
		priceString = truncatTailingZeroes(priceString);
		amountString = truncatTailingZeroes(amountString);
		
		return "method=sellOrder3&params=" + priceString + "," + amountString + "," + market;
	}

	String truncatTailingZeroes(String s)
	{
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//remove redundant 0  
            s = s.replaceAll("[.]$", "");//if the last is ., remove  
        }  
        return s;  
	}

	String toGetOrderParamString(Integer orderid, String market) {
		String retString = "method=getOrder&params=" + orderid + "," + market + ",1";
		return retString;
	}

	String toGetOrdersParamString(String market)
    {
        String retString = "method=getOrders&params=1," + market + ",1000,0,0,1";
        return retString;
    }

	String toCancelOrderParamString(Integer orderid, String market) {
		String retString = "method=cancelOrder3&params=" + orderid + "," + market;
		return retString;
	}
    
    String toGetAccountInfoParamString()
    {
        String retString = "method=getAccountInfo&params=balance";
        return retString;
    }
}
