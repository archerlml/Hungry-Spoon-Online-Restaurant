package edu.sjsu.cmpe275.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import edu.sjsu.cmpe275.domain.ChefSchedule;
import edu.sjsu.cmpe275.util.ChefSched;
import edu.sjsu.cmpe275.util.Schedule;

public interface ChefScheduleService {
	
	/**
	 * 
	 * @param chefId
	 * @return
	 */
	List<Schedule> getScheduleByChefId(Integer chefId);
	
	/**
	 * persist a ChefSchedule object by writing a sql entry into database
	 * @param chefSchedule: a ChefSchedule object
	 * @return a ChefSchedule object
	 */
	ChefSchedule save(ChefSchedule chefSchedule);
	
	/**
	 * delete schedule by its id
	 * @param id : schedule id, generated by system
	 */
	void deleteScheduleById(Integer id);
	
	/**
	 * 
	 * @param minutesDuration : how long the proposed schedule last, i.e., 10min
	 * @return a ChefSched which contains something like {"chefId":1, "startTime":time1(java.util.Date object), "endTime":time2}
	 * @throws Exception
	 */
	ChefSched getEarliestAvailableSchedule(Integer minutesDuration) throws Exception;
	
	
	/**
	 * 
	 * @param newSchedule: a Schedule object contains something like: {"startTime":time1(java.util.Date object), "endTime":time2}
	 * @return 0 if not available, 1 or 2 or 3 for id of chef with available schedule
	 * @throws ParseException
	 */
	int isScheduleAvailable(Schedule newSchedule) throws ParseException;
	
	/**
	 * 
	 * @param pickUpTime : a proposed pick up time
	 * @param minutesDuration : minutes for the order to prepare
	 * @return null if not available, a ChefSched object with chef id, start time, end time
	 */
	ChefSched getAvailablePreparationSchedule(Date pickUpTime, int minutesDuration);
	
	/**
	 * 
	 * @param pickUpTime : a proposed pick up time
	 * @param minutesDuration : minutes for the order to prepare
	 * @return true if available, false if not available
	 */
	boolean isPickupTimeAvailable(Date pickUpTime, int minutesDuration);
	

}