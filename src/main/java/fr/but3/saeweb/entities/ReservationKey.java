package fr.but3.saeweb.entities;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class ReservationKey implements Serializable {

    private Timestamp startTime;
    private Timestamp endTime;
    private Date date;
    private String email;
}

