package edu.sjsu.cmpe275.repository;

import java.util.Date;
//import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import edu.sjsu.cmpe275.domain.ChefSchedule;

public interface ChefScheduleRepository extends CrudRepository<ChefSchedule, Integer>{
	List<ChefSchedule> findChefScheduleByChefId(Integer chefId);
	
	@Query("SELECT c FROM ChefSchedule c WHERE ((:startTime <= c.startTime AND :endTime > c.startTime) "
			+ "OR (c.startTime <= :startTime AND c.endTime >= :endTime)"
			+ "OR (:endTime >= c.endTime AND :startTime < c.endTime))  AND c.chefId = :chefId" )
	List<ChefSchedule> findChefScheduleByDate(@Param("startTime") Date startTime, 
			@Param("endTime") Date endTime,
			@Param("chefId") Integer chefId);
}
