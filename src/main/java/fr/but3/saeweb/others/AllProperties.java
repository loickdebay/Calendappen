package fr.but3.saeweb.others;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:admin.properties")
@PropertySource("classpath:parameters.properties")
@PropertySource("classpath:email.properties")
public class AllProperties {

    private Environment env;

    private String adminEmail;
    private String adminName;
    private String adminPhoneNumber;
    private String adminPassword;
    private String adminFirstname;
    
    private String workday;
    private ArrayList<String> workdays;
    private ArrayList<LocalTime> openingTime = new ArrayList<>();
    private ArrayList<LocalTime> closingTime = new ArrayList<>();
    private LocalTime reservationTime;
    private int maxPeople;
    private String emailSender;
    private int maxtime;
    
    @Autowired
    public AllProperties(Environment env){

        this.env = env;
        
        this.adminEmail = env.getProperty("email");

        this.adminFirstname = env.getProperty("firstname");

        this.adminName = env.getProperty("name");

        this.adminPassword = env.getProperty("password");

        this.adminPhoneNumber = env.getProperty("phonenumber");


        this.workdays = new ArrayList<>(Arrays.asList(env.getProperty("workday").split(",")));
        this.workday = env.getProperty("workday");

        this.emailSender = env.getProperty("emailsender");

        this.maxtime=Integer.parseInt(env.getProperty("maxtime"))+1;

        ArrayList<String>openingTimeRaw = new ArrayList<>(Arrays.asList(env.getProperty("openingTime").split(",")));

        for (int i =0;i < openingTimeRaw.size();i++){
            String[] time = openingTimeRaw.get(i).split(":");
            this.openingTime.add(LocalTime.of(Integer.parseInt(time[0]),Integer.parseInt(time[1])));
        }

        ArrayList<String>closingTimeRaw = new ArrayList<>(Arrays.asList(env.getProperty("closingTime").split(",")));
        for (int i =0;i < closingTimeRaw.size();i++){
            String[] time = closingTimeRaw.get(i).split(":");
            this.closingTime.add(LocalTime.of(Integer.parseInt(time[0]),Integer.parseInt(time[1])));
        }


        String[] reservationTimeValues = env.getProperty("reservationTime").split(":");

        this.reservationTime = LocalTime.of(
        Integer.parseInt(reservationTimeValues[0]),Integer.parseInt(reservationTimeValues[1])
        );
        
        this.maxPeople = Integer.parseInt(env.getProperty("maxPeople"));


    }
    
}
