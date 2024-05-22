package fr.but3.saeweb.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ClosedDay {

    @Id
    private Date date;
    
}
