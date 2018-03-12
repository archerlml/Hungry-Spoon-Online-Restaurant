package edu.sjsu.cmpe275.service;

//import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.domain.ChefSchedule;
import edu.sjsu.cmpe275.repository.ChefScheduleRepository;
import edu.sjsu.cmpe275.util.ChefSched;
import edu.sjsu.cmpe275.util.Schedule;

@Service(value="chefScheduleService")
public class ChefScheduleServiceImpl implements ChefScheduleService {
	
	private static final String workDayStartHour = "05:00:00";
	private static final String workDayEndHour = "21:00:00";
	private static final int orderPeriod = 30; //how many days from now on you can place order
	
	// minutesDuration * 60seconds * 1000miniseconds
	private static final long oneMinuteInMiniSeconds = 60 * 1000;
	// 60minutes * 60seconds * 1000miniseconds
	private static final long oneHourInMiniSeconds = 60 * oneMinuteInMiniSeconds;
	
	private List<Integer> chefs;
	
	{
		chefs = new ArrayList<Integer>();
		chefs.add(1);
		chefs.add(2);
		chefs.add(3);
	}
	
	
	@Autowired
	private ChefScheduleRepository chefScheduleRepository;

	@Override
	public List<Schedule> getScheduleByChefId(Integer chefId) {
		List<Schedule> schedules = new ArrayList<Schedule>();
		Iterable<ChefSchedule> chefSchedules = chefScheduleRepository.findChefScheduleByChefId(chefId);
		for (ChefSchedule chefSchedule : chefSchedules) {
			Schedule schedule = new Schedule(chefSchedule.getStartTime(),chefSchedule.getEndTime());

			schedules.add(schedule);	
		}
		
		mergeSchedules(schedules);
		
		return schedules;
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ)
	@Override
	public ChefSchedule save(ChefSchedule chefSchedule) {
		return chefScheduleRepository.save(chefSchedule);
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ)
	@Override
	public void deleteScheduleById(Integer id) {
		chefScheduleRepository.delete(id);
	}

	@Override
	public ChefSched getEarliestAvailableSchedule(Integer minutesDuration) throws Exception {
		List<ChefSched> schedules = new ArrayList<ChefSched>();
		for (Integer chefId: chefs) {
			Schedule schedule = getEarliestAvailableTimeByChefId(chefId, minutesDuration);
			ChefSched chefSched = new ChefSched(chefId, schedule.getStartTime(), schedule.getEndTime());
			schedules.add(chefSched);
		}
		Collections.sort(schedules, new Comparator<ChefSched>(){

			@Override
			public int compare(ChefSched o1, ChefSched o2) {
				return o1.getStartTime().compareTo(o2.getStartTime());
			}
			
		});
		if (!schedules.isEmpty() && schedules.get(0) != null) {
			return schedules.get(0);
		}
		return null;
	}
	
	public Schedule getEarliestAvailableTimeByChefId(Integer chefId, int minutesDuration) throws ParseException {
//		Date now = new Date(Calendar.getInstance().getTime().getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -7);
		Date now = calendar.getTime();
		
		
		calendar.setTime(now);
		Schedule todaySchedule = getWorkDayStartAndEnd(now);
		if (todaySchedule.getEndTime().compareTo(now) <= 0) {
			calendar.add(Calendar.DATE, 1);
		}
		for (int i = 0; i < orderPeriod; i++) {
			Schedule schedule = getWorkDayStartAndEnd(calendar.getTime());
			Date start = now.compareTo(schedule.getStartTime()) > 0 ? now : schedule.getStartTime();
			if (schedule.getEndTime().compareTo(now) <= 0 
					|| schedule.getEndTime().getTime() - start.getTime() < minutesDuration * oneMinuteInMiniSeconds
					) {
				calendar.add(Calendar.DATE, 1);
				continue;
			}
			
			Date workDayStartPlusOneHour = new Date(schedule.getStartTime().getTime() + oneHourInMiniSeconds);
			schedule.setStartTime(start);
			List<ChefSchedule> chefSchedules = chefScheduleRepository
					.findChefScheduleByDate(start, schedule.getEndTime(), chefId);
			if (chefSchedules.isEmpty()){
				Date earlistEnd = new Date(start.getTime() + minutesDuration * oneMinuteInMiniSeconds);
				earlistEnd = earlistEnd.compareTo(workDayStartPlusOneHour) < 0 ? workDayStartPlusOneHour : earlistEnd;
				return new Schedule(start,earlistEnd);
			}

			List<Schedule> availableSchedules = getAvailableSchedulesFromChefSchedules(chefSchedules);
			for (Schedule availableSchedule :availableSchedules) {
				long duration = availableSchedule.getEndTime().getTime() -availableSchedule.getStartTime().getTime();
				// minutes = 60 seconds * 1000 miniseconds
				if (minutesDuration <= duration / (60 * 1000)) {
					Date earlistStart = availableSchedule.getStartTime();
					Date earlistEnd = new Date(earlistStart.getTime() + minutesDuration * oneMinuteInMiniSeconds);
					
					earlistEnd = earlistEnd.compareTo(workDayStartPlusOneHour) < 0 ? workDayStartPlusOneHour : earlistEnd;
					
					
					return new Schedule(earlistStart, earlistEnd);
				}
			}
			calendar.add(Calendar.DATE, 1);
		}
		return null;
	}
	
	public void mergeSchedules(List<Schedule> schedules) {
		if (schedules.isEmpty() || schedules.size() == 1) {
			return;
		}
		
		//sort schedules by their start time
		Collections.sort(schedules);
		
		int i = 1;
		int size = schedules.size();
		while ( i < size) {
			if (schedules.get(i-1).getEndTime().compareTo(schedules.get(i).getStartTime()) < 0) {
				i++;
			} else {
				int cmpEndTime = schedules.get(i-1).getEndTime().compareTo(schedules.get(i).getEndTime());
				if (cmpEndTime < 0) {
					schedules.get(i-1).setEndTime(schedules.get(i).getEndTime());
				}
				schedules.remove(i);
				size--;
				if (i == size) {
					break;
				}
			}
		}
	}
	
	public boolean isPickupTimeAvailable(Date pickUpTime, int minutesDuration) {
		ChefSched chefSched = getAvailablePreparationSchedule(pickUpTime, minutesDuration);
		if (chefSched != null) {
			return true;
		} else {
			return false;
		}
	}
	
//	public Date getEarliestAvailablePickUpTime(int minutesDuration) {
//		ChefSched chefSched = null;
//		try {
//			chefSched = getEarliestAvailableSchedule(minutesDuration);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		Schedule daySchedule = null;
//		try {
//			daySchedule = getWorkDayStartAndEnd(chefSched.getStartTime());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		long officeStartInSeconds = daySchedule.getStartTime().getTime() + oneHourInMiniSeconds;
//		long earlistPreparationFinishTimeInSeconds = chefSched.getStartTime().getTime() + minutesDuration * oneMinuteInMiniSeconds;
//		if ( earlistPreparationFinishTimeInSeconds - officeStartInSeconds <= 0 ) {
//			return new Date(officeStartInSeconds); 
//		} else {
//			return new Date(earlistPreparationFinishTimeInSeconds); 
//		}
//	}
	
	
	public ChefSched getAvailablePreparationSchedule(Date pickUpTime, int minutesDuration) {
		if (pickUpTime == null) {
			return null;
		}
		Schedule currentDay = null;
		try {
			currentDay = getWorkDayStartAndEnd(pickUpTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date workDayStart = currentDay.getStartTime();
		Date workDayEnd = currentDay.getEndTime();
		
		Date latestPreparationTime = new Date(pickUpTime.getTime() - minutesDuration * oneMinuteInMiniSeconds);

		// preparation time should be within 1hour before pickuptime
		Date earlistPreparationTime = new Date(latestPreparationTime.getTime() - 1 * oneHourInMiniSeconds);
		if (latestPreparationTime.compareTo(workDayStart) < 0 
				|| pickUpTime.compareTo(workDayEnd) > 0
				|| pickUpTime.getTime() - workDayStart.getTime() < oneHourInMiniSeconds) {
			return null;
		}
		
		long preparationTimeInMiniSeconds = minutesDuration * oneMinuteInMiniSeconds;
		for (int i = 0; i < chefs.size(); i++) {
			Integer chefId = chefs.get(i);
			List<ChefSchedule> chefSchedules = chefScheduleRepository.
					findChefScheduleByDate(earlistPreparationTime, pickUpTime, chefId);
			List<Schedule> availableSchedules = null;
			try {
				availableSchedules = getAvailableSchedulesFromChefSchedules(chefSchedules,earlistPreparationTime,pickUpTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if (availableSchedules == null || availableSchedules.isEmpty()) {
				continue;
			}
			for (Schedule schedule : availableSchedules) {
				if (schedule.getEndTime().getTime() - schedule.getStartTime().getTime() >= preparationTimeInMiniSeconds) {
					return new ChefSched(chefId, schedule.getStartTime(),
							new Date(schedule.getStartTime().getTime() + minutesDuration * oneMinuteInMiniSeconds));
				}
			}
		}
		
		return null;
	}
	
	public int isScheduleAvailable(Schedule newSchedule) throws ParseException {
		Schedule workDaySchedule = getWorkDayStartAndEnd(newSchedule.getStartTime());
		Date workDayStart = workDaySchedule.getStartTime();
		Date workDayEnd = workDaySchedule.getEndTime();
		if (newSchedule.getEndTime().compareTo(workDayStart) < 0 ||
				newSchedule.getEndTime().compareTo(workDayEnd) > 0) {
			return 0;
		}
		
		for (Integer chefId : chefs) {
			if (isScheduleAvailableByChefId(newSchedule, chefId)) {
				return chefId;
			}
		}
		
		return 0;
	}
	
	public boolean isScheduleAvailableByChefId(Schedule newSchedule, Integer chefId) throws ParseException {
//		Schedule workDaySchedule = getWorkDayStartAndEnd(newSchedule.getStartTime());
//		Date workDayStart = workDaySchedule.getStartTime();
//		Date workDayEnd = workDaySchedule.getEndTime();
		
		List<ChefSchedule> chefSchedules = chefScheduleRepository
				.findChefScheduleByDate(newSchedule.getStartTime(), newSchedule.getEndTime(), chefId);
		if (chefSchedules.isEmpty()) {
			return true;
		} else {
			return false;
		}
		
//		List<Schedule> schedules = getAvailableSchedulesFromChefSchedules(chefSchedules);
//		if (isScheduleAvailable(newSchedule, schedules)) {
//			return true;
//		} else {
//			return false;
//		}
		
	}
	
	private List<Schedule> getAvailableSchedulesFromChefSchedules(List<ChefSchedule> chefSchedules, Date startTime, Date endTime) throws ParseException {
		List<Schedule> schedules = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -7);
		Date now = calendar.getTime();
		startTime = startTime.compareTo(now) > 0 ? startTime : now;
		endTime = endTime.compareTo(now) > 0 ? endTime : now;
		if (startTime.compareTo(endTime) >= 0) {
			return schedules;
		}
		if (chefSchedules.isEmpty()) {
			Schedule availableSchedule = new Schedule(startTime, endTime);
			schedules.add(availableSchedule);
			return schedules;
		}
		Schedule workDaySchedule = getWorkDayStartAndEnd(chefSchedules.get(0).getStartTime());
		Date workDayStart = workDaySchedule.getStartTime();
		Date workDayEnd = workDaySchedule.getEndTime();
		
		
		if (endTime.compareTo(workDayStart) < 0 || startTime.compareTo(workDayEnd) > 0) {
			return schedules;
		}

		for (ChefSchedule chefSchedule : chefSchedules) {
			Date start = chefSchedule.getStartTime().compareTo(now) > 0 ? chefSchedule.getStartTime() : now;
			Date end = chefSchedule.getEndTime().compareTo(now) > 0 ? chefSchedule.getEndTime() : now;
			schedules.add(new Schedule(start, end));
		}
		mergeSchedules(schedules);
		List<Schedule> availableSchedules = new ArrayList<>();
		if (schedules.size() == 1) {
			Date start = schedules.get(0).getStartTime();
			Date end = schedules.get(0).getEndTime();
			if (start.compareTo(endTime) >= 0 || end.compareTo(startTime) <= 0) {
				availableSchedules.add(new Schedule(startTime, endTime));
			} else if (start.compareTo(startTime) > 0 && end.compareTo(endTime) < 0) {
				availableSchedules.add(new Schedule(startTime, start));
				availableSchedules.add(new Schedule(end, endTime));
			} else {
				if (start.compareTo(startTime) > 0) {
					start = start.compareTo(endTime) < 0 ? start : endTime;
					availableSchedules.add(new Schedule(start, endTime));
				}

				if (end.compareTo(endTime) < 0) {
					end = end.compareTo(startTime) > 0 ? end : startTime;
					availableSchedules.add(new Schedule(end, endTime));
				}
			}
			return availableSchedules;
		}
		
		
		for (int i = 0; i < schedules.size(); i++) {
			Date start = null;
			Date end = null;
			if (i == 0) {
				if (schedules.get(i).getStartTime().compareTo(startTime) > 0) {
					start = startTime;
					end = schedules.get(i).getStartTime();
				} else {
					continue;
				}
			} else {
				start = schedules.get(i - 1).getEndTime();
				
				end = schedules.get(i).getStartTime();
				

				
			}
			if (start != null) {
				start = start.compareTo(startTime) > 0 ? start : startTime;
				end = end.compareTo(endTime) < 0 ? end : endTime;
				Schedule availableSchedule = new Schedule(start, end);
				availableSchedules.add(availableSchedule);
			}
			if (i == schedules.size() - 1) {
				if (schedules.get(i).getEndTime().compareTo(endTime) < 0) {
					start = schedules.get(i).getEndTime();
					start = start.compareTo(startTime) > 0 ? start : startTime;
					end = endTime;
					Schedule availableSchedule = new Schedule(start, end);
					availableSchedules.add(availableSchedule);
				}
			}
		}

		return availableSchedules;
	}
	
	// @param chefschedules : chefschedule within a specific day
	// @return available time slots of a day from chef schedules
	public List<Schedule> getAvailableSchedulesFromChefSchedules(List<ChefSchedule> chefSchedules) throws ParseException {
		
//		if (chefSchedules.isEmpty()) {
//			return null;
//		}
		Schedule workDaySchedule = getWorkDayStartAndEnd(chefSchedules.get(0).getStartTime());
		Date workDayStart = workDaySchedule.getStartTime();
		Date workDayEnd = workDaySchedule.getEndTime();
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -7);
		Date now = calendar.getTime();
		workDayStart = workDayStart.compareTo(now) > 0 ? workDayStart : now;
		
		List<Schedule> schedules = getAvailableSchedulesFromChefSchedules(chefSchedules, workDayStart, workDayEnd);



		return schedules;
	}
	
	
	//check if a new proposed schedule is within available schedules
	private boolean isScheduleAvailable(Schedule newSchedule, List<Schedule> availableSchedules) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -7);
		Date now = calendar.getTime();
		Schedule todaySchedule = getWorkDayStartAndEnd(now);
		Date workDayStart = todaySchedule.getStartTime();
		Date workDayEnd = todaySchedule.getEndTime();

		if (newSchedule.getStartTime().compareTo(now ) < 0 
				|| newSchedule.getStartTime().compareTo(workDayStart) < 0
				|| newSchedule.getEndTime().compareTo(workDayEnd) > 0) {
			return false;
		}
		
		
		if (availableSchedules == null || availableSchedules.isEmpty()) {
			return false;
		}
		//sort schedules by their start time
		Collections.sort(availableSchedules);
		

		for (int i = 0; i < availableSchedules.size(); i++) {
			if (newSchedule.getStartTime().compareTo(availableSchedules.get(i).getEndTime()) > 0) {
				continue;
			} else {
				if (newSchedule.getEndTime().compareTo(availableSchedules.get(i).getStartTime()) <= 0) {
					return true;
				} else {
					return false;
				}
			}
		}
		
		return false;
	}
	
	private Schedule getWorkDayStartAndEnd(Date date) throws ParseException {
		SimpleDateFormat shortformat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat longformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String todayString = shortformat.format(date);
		String workDayStartStr = todayString + " " + workDayStartHour;
		String workDayEndStr = todayString + " " + workDayEndHour;
		Date workDayStart = longformat.parse(workDayStartStr);
		Date workDayEnd = longformat.parse(workDayEndStr);
		
		return new Schedule(workDayStart, workDayEnd);
	}
}
