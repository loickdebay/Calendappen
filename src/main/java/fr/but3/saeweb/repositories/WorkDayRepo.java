package fr.but3.saeweb.repositories;

import java.sql.Date;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.but3.saeweb.entities.WorkDay;

@Repository
public interface WorkDayRepo extends CrudRepository<WorkDay,Long>{
    WorkDay findByDate(Date date);
}