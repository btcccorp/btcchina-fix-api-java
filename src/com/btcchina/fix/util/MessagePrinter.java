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

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;

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
    
    public static void main(String args[]) {	
    	MessagePrinter mp=new MessagePrinter();
    	String symbol = mp.getFieldName("55");
    	System.out.println("Symbol is "+symbol);
    	String group = mp.getFieldName("269","0");
    	System.out.println("group is "+group);
    }
    
    public String getFieldName(String tag) {	 
    	String field_name="";
	    try {
	    	File fixdic = new File("data/FIX44.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document parser = dBuilder.parse(fixdic);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			parser.getDocumentElement().normalize();
//			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList fieldList = parser.getElementsByTagName("field");
//			System.out.println("----------------------------");
			for (int fieldIndex = 0; fieldIndex < fieldList.getLength(); fieldIndex++) {
				Node fieldNode = fieldList.item(fieldIndex);
//				System.out.println("\nCurrent Element :" + nNode.getNodeName());
				if (fieldNode.getNodeType() == Node.ELEMENT_NODE) {
					Element fieldElement = (Element) fieldNode;
					String field_number=fieldElement.getAttribute("number");
//					System.out.println("number : " + field_number);
					field_name=fieldElement.getAttribute("name");
//					System.out.println("Field Name : " + field_name);
					if(tag.equals(field_number)){
						return field_name; 
					}
				}
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		return field_name;
	}
    
    public String getFieldName(String group, String tag) {	 
    	String field_name="";
	    try {
	    	File fixdic = new File("data/FIX44.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document parser = dBuilder.parse(fixdic);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			parser.getDocumentElement().normalize();
			NodeList fieldList = parser.getElementsByTagName("field");
		 
			for (int fieldIndex = 0; fieldIndex < fieldList.getLength(); fieldIndex++) {
				Node fieldNode = fieldList.item(fieldIndex);
				if (fieldNode.getNodeType() == Node.ELEMENT_NODE) {
					Element fieldElement = (Element) fieldNode;
					String field_number=fieldElement.getAttribute("number");
					field_name=fieldElement.getAttribute("name");
					if(group.equals(field_number)){
						NodeList value=fieldElement.getElementsByTagName("value");
						for(int leaf=0; leaf<value.getLength();leaf++){
							Node valueNode=value.item(leaf);
							Element valueElement=(Element)valueNode;
							String num=valueElement.getAttribute("enum");
							field_name=valueElement.getAttribute("description");
							if(tag.equals(num)){
								return field_name;
							}
						}
						return field_name; 
					}
				}
			}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		return field_name;
	}
    
	public String getValue(String s){  
        if(s.indexOf("=") > 0){  
        	s=s.substring(s.indexOf("=")+1);
        }  
        return s;  
	}
	
	public String getNum(String s){  
        if(s.indexOf("=") > 0){  
        	s=s.substring(s.indexOf("=")+1);
        }  
        return s;  
	}
	
	public String getTag(String s){  
        if(s.indexOf("=") > 0){  
        	s=s.substring(0, s.indexOf("="));
        }  
        return s;  
	}

}