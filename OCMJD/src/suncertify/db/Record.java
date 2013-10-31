package suncertify.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Record {
	
	private Map<String, Object> fields;
	
	public Record() {
		fields = new HashMap<String, Object>();
	}
	
	public Record(List<String> names, List<Object> contents){
		this();
		
		int index = 0;
		
		if (names.size() == contents.size()) {
			while (index < names.size()) {
				fields.put(names.get(index),contents.get(index));
			}
		}
		//else throw exception
	}
	
	public Object getValue(String key) {
		return fields.get(key);
	}
}
