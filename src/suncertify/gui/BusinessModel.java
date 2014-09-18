package suncertify.gui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import suncertify.db.DB;
import suncertify.db.DBException;
import suncertify.db.Data;
import suncertify.db.RecordNotFoundException;
import suncertify.db.SecurityException;

/**
 * ALL METHODS IN THIS CLASS THROW NETWORK EXCEPTION
 * IF THE DB IS AN INSTANCE OF DataProxy
 * @author ejhnhng
 *
 */
public class BusinessModel {
	
	private static List<Observer> observers = new ArrayList<Observer>();
	
	private DB dataAccess;
	
	public BusinessModel(DB dataAccess) {
		this.dataAccess = dataAccess;
	}
	
	public void addObserver(Observer observer) {
		observers.add(observer);
	}
	
	public void fireModelChangeEvent() {
		for (Observer observer: observers) {
			// change params after
			observer.update(null, null);
		}
	}
	
	public Map<Integer, Room> searchRooms(SearchCriteria criteria) throws RecordNotFoundException {
        
        String[] searchCriteria = criteria.getCriteria();
        int[] matchingRecordNumbers = dataAccess.find(searchCriteria);
        Map<Integer, Room> roomMap = new LinkedHashMap<Integer, Room>();
        
        for (int i = 0; i < matchingRecordNumbers.length; i++) {
            int recNo = matchingRecordNumbers[i];
            String[] data = dataAccess.read(recNo);
            Room room = new Room(recNo, data);
            roomMap.put(i, room);
        }
        
        return roomMap;
    }   
	
	public void book(Room room) throws RecordNotFoundException {
	    int recNo = room.getRecNo();
	    String[] data = room.getData();
	    
	    if (alreadyBooked(recNo)) {
	    	throw new RecordNotFoundException();
	    }
	    else {
	    	try {
	            long lockCookie = dataAccess.lock(recNo);
	            dataAccess.update(recNo, data, lockCookie);
	            dataAccess.unlock(recNo, lockCookie);
	        } catch (RecordNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (SecurityException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }
		
		fireModelChangeEvent();
	}
	
	private boolean alreadyBooked(int recNo) throws RecordNotFoundException {
		String[] roomData = dataAccess.read(recNo);
		String owner = roomData[6];
		if (owner.equals("")) {
			return false;
		}
		return true;
	}
	
	
}
