package edu.sjsu.cmpe275.util;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

//import java.sql.Date;

public class Schedule implements Comparable<Schedule>{
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") //, timezone = "GMT-07:00")
	private Date startTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") //, timezone = "GMT-07:00")
	private Date endTime;
	
	
	public Date getStartTime() {
		return startTime;
	}
	@Override
	public String toString() {
		return "Schedule [startTime=" + startTime + ", endTime=" + endTime + "]";
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Schedule(Date startTime, Date endTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Override
	public int compareTo(Schedule o) {

		return this.startTime.compareTo(o.getStartTime());
	}


}
