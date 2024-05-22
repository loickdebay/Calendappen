package fr.but3.saeweb;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import fr.but3.saeweb.entities.Client;
import fr.but3.saeweb.entities.Reservation;
import fr.but3.saeweb.others.AllProperties;
import fr.but3.saeweb.repositories.ClientRepo;


@Component
public class Demarrage implements ApplicationRunner{

@Autowired
ApplicationContext applicationContext;

@Autowired
AllProperties allProperties;

@Autowired
private ClientRepo clientRepo;

@Override
public void run(ApplicationArguments args) throws Exception {
    
    System.out.println("APP LAUNCHED");
    ArrayList<Reservation> reservations=new ArrayList<>();
    Client admin = new Client();
    admin.setEmail(allProperties.getAdminEmail());
    admin.setFirstName(allProperties.getAdminFirstname());
    admin.setRole("admin");
    admin.setName(allProperties.getAdminName());
    admin.setPassword(allProperties.getAdminPassword());
    admin.setPhoneNumber(allProperties.getAdminPhoneNumber());
    admin.setReservations(reservations);
    Client isAdminExist = clientRepo.findByEmail(allProperties.getAdminEmail());
    if(isAdminExist == null){
        clientRepo.save(admin);
    }
}
}


