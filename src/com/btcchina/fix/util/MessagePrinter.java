package com.btcchina.fix.util;

import java.util.Iterator;

import quickfix.DataDictionary;
import quickfix.Field;
import quickfix.FieldMap;
import quickfix.FieldNotFound;
import quickfix.FieldType;
import quickfix.Group;
import quickfix.field.MsgType;
import quickfix.fix44.Message;

public class MessagePrinter {
	 
    public void print(DataDictionary dd, Message message) throws FieldNotFound {
        String msgType = message.getHeader().getString(MsgType.FIELD);
        printFieldMap("", dd, msgType, message.getHeader());
        printFieldMap("", dd, msgType, message);
        printFieldMap("", dd, msgType, message.getTrailer());
    }
 
    private void printFieldMap(String prefix, DataDictionary dd, String msgType, FieldMap fieldMap)
            throws FieldNotFound {
 
        Iterator fieldIterator = fieldMap.iterator();
        while (fieldIterator.hasNext()) {
            Field field = (Field) fieldIterator.next();
            if (!isGroupCountField(dd, field)) {
                String value = fieldMap.getString(field.getTag());
                if (dd.hasFieldValue(field.getTag())) {
                    value = dd.getValueName(field.getTag(), fieldMap.getString(field.getTag())) + " (" + value + ")";
                }
                System.out.println(prefix + dd.getFieldName(field.getTag()) + ": " + value);
            }
        }
 
        Iterator groupsKeys = fieldMap.groupKeyIterator();
        while (groupsKeys.hasNext()) {
            int groupCountTag = ((Integer) groupsKeys.next()).intValue();
            System.out.println(prefix + dd.getFieldName(groupCountTag) + ": count = "
                    + fieldMap.getInt(groupCountTag));
            Group g = new Group(groupCountTag, 0);
            int i = 1;
            while (fieldMap.hasGroup(i, groupCountTag)) {
                if (i > 1) {
                    System.out.println(prefix + "  ----");
                }
                fieldMap.getGroup(i, g);
                printFieldMap(prefix + "  ", dd, msgType, g);
                i++;
            }
        }
    }
 
    private boolean isGroupCountField(DataDictionary dd, Field field) {
        return dd.getFieldTypeEnum(field.getTag()) == FieldType.NumInGroup;
    }
 
}