package fr.but3.saeweb.entities;

import java.sql.Timestamp;
import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@IdClass(ReservationKey.class)
public class Reservation {

    @Id
    private Timestamp startTime;

    @Id
    private Timestamp endTime;

    @Id
    private Date date;

    @Id
    private String email;

    @ManyToOne()
    @JoinColumn(name="id_client",nullable = false)
    private Client client;
    
    public void setId(ReservationKey reservationKey) {
        this.startTime = reservationKey.getStartTime();
        this.endTime = reservationKey.getEndTime();
        this.date = reservationKey.getDate();
        this.email = reservationKey.getEmail();
    }

}
