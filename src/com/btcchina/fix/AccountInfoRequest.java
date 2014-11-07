package com.btcchina.fix;

import quickfix.FieldNotFound;
import quickfix.fix44.Message;

public class AccountInfoRequest extends Message {

	private static final long serialVersionUID = -3033519792313574631L;
	public static final String MSGTYPE = "U1000";
	
	public AccountInfoRequest() {
        super();
        getHeader().setField(new quickfix.field.MsgType(MSGTYPE));
    }
	
	public void set(AccReqID value) {
        setField(value);
    }
	
	public AccReqID get(AccReqID value) throws FieldNotFound {
        getField(value);
        return value;
    }
	
	public AccReqID getAccReqID() throws FieldNotFound {
		AccReqID value = new AccReqID();
        getField(value);

        return value;
    }
	
	public quickfix.field.Account get(quickfix.field.Account value) throws FieldNotFound {
        getField(value);

        return value;
    }

    public quickfix.field.Account getAccount() throws FieldNotFound {
        quickfix.field.Account value = new quickfix.field.Account();
        getField(value);

        return value;
    }
    
    public void set(quickfix.field.Account value) {
        setField(value);
    }
}
