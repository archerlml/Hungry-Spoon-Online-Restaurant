package edu.sjsu.cmpe275.domain;

import java.io.Serializable;
import java.util.Date;

//import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name = "chefschedule")
public class ChefSchedule  {
	
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private int id;
	
	private int chefId;
	
//	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT-07:00")
	private Date startTime;
	
//	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT-07:00")
	private Date endTime;
	private Integer orderId;
	
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getChefId() {
		return chefId;
	}


	public void setChefId(int chefId) {
		this.chefId = chefId;
	}

	public Date getStartTime() {
		return startTime;
	}


	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}


	public Date getEndTime() {
		return endTime;
	}


	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	@Override
	public String toString() {
		return "ChefSchedule [startTime=" + startTime + ", endTime=" + endTime + "]";
	}


	public Integer getOrderId() {
		return orderId;
	}


	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}




}
