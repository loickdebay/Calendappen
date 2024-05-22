package fr.but3.saeweb.repositories;

import java.sql.Date;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.but3.saeweb.entities.ClosedDay;

@Repository
public interface ClosedDayRepo extends CrudRepository<ClosedDay,Long>{
    ClosedDay findByDate(Date date);
}