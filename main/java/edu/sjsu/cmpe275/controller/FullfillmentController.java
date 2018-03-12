package edu.sjsu.cmpe275.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.cmpe275.domain.ChefSchedule;
import edu.sjsu.cmpe275.service.ChefScheduleService;
import edu.sjsu.cmpe275.util.ChefSched;
import edu.sjsu.cmpe275.util.Schedule;

@RequestMapping(value="/schedule")
@RestController
public class FullfillmentController {

	@Autowired
	private ChefScheduleService chefScheduleService;
	
	
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<Schedule> getSchedules() {
		return chefScheduleService.getScheduleByChefId(1);
	}
	
	
	@RequestMapping(value = "/{preparationTime}", method = RequestMethod.GET)
	public ChefSched getAvailableScheduleByIdAndDate(@PathVariable("preparationTime") Integer preparationTime
														  ) throws Exception{
		return chefScheduleService.getEarliestAvailableSchedule(preparationTime);
	}
	
	@RequestMapping(value = "reserve", method = RequestMethod.POST)
	public void reserveSchedule(@RequestBody ChefSchedule chefSchedule) {
		chefScheduleService.save(chefSchedule);
	}
	
	

}
