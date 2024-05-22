package fr.but3.saeweb.repositories;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import fr.but3.saeweb.entities.Reservation;

@Repository
public interface ReservationRepo extends CrudRepository<Reservation,Long>{
    void deleteByStartTimeAndEndTimeAndDate(Timestamp startTime,Timestamp endTime,Date date);
    void deleteByStartTimeAndEndTimeAndDateAndEmail(Timestamp startTime,Timestamp endTime,Date date,String email);
    List<Reservation> findByStartTimeAndEndTimeAndDate(Timestamp startTime,Timestamp endTime,Date date);
    Reservation findByStartTimeAndEndTimeAndDateAndEmail(Timestamp startTime,Timestamp endTime,Date date,String email);
    List<Reservation> findAllByDate(Date date);
}