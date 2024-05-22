package fr.but3.saeweb.controlers;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import fr.but3.saeweb.entities.Client;
import fr.but3.saeweb.entities.ClosedDay;
import fr.but3.saeweb.entities.Reservation;
import fr.but3.saeweb.entities.ReservationKey;
import fr.but3.saeweb.entities.WorkDay;
import fr.but3.saeweb.others.AllProperties;
import fr.but3.saeweb.others.EmailSender;
import fr.but3.saeweb.others.Principal;
import fr.but3.saeweb.repositories.ClientRepo;
import fr.but3.saeweb.repositories.ClosedDayRepo;
import fr.but3.saeweb.repositories.ReservationRepo;
import fr.but3.saeweb.repositories.WorkDayRepo;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;


@Controller
public class controlerReservations {

    @Autowired
    ClientRepo clientRepo;

    @Autowired
    ClosedDayRepo closedDayRepo;

    @Autowired
    ReservationRepo reservationRepo;

    @Autowired
    AllProperties allProperties;

    @Autowired
    WorkDayRepo workDayRepo;

    @Autowired
    private EmailSender emailSender;

    @GetMapping("/reservation")
    public String showReservation(String id,HttpSession session, ModelMap modelMap){
        Principal principal = (Principal) session.getAttribute("principal");
        if(principal == null){
            return "login";
        }
        String[] date = id.split("-");
        LocalDate localDate = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        int dayOfWeek = localDate.getDayOfWeek().getValue();
        LocalTime start = allProperties.getOpeningTime().get(dayOfWeek-1);
        LocalTime end = allProperties.getClosingTime().get(dayOfWeek-1);
        LocalTime reservationTime = allProperties.getReservationTime();

        ClosedDay dayClosed = closedDayRepo.findByDate(Date.valueOf(localDate));
        WorkDay exceptionalWorkDay = workDayRepo.findByDate(Date.valueOf(localDate));
        boolean isExceptionalDate = false;
        if(exceptionalWorkDay!=null){
            isExceptionalDate = true;
        }
        boolean dateClosed = false;
        if(dayClosed != null){
            dateClosed=true;
        }
        boolean isNonWorkingDay = false;
        String workdays = allProperties.getWorkday();
        if(!workdays.contains(localDate.getDayOfWeek().name().toLowerCase())){
            isNonWorkingDay=true;
        }
        modelMap.addAttribute("isNonWorkingDay", isNonWorkingDay);
        modelMap.addAttribute("isED",isExceptionalDate);
        modelMap.addAttribute("id",id);
        modelMap.addAttribute("start",start);
        modelMap.addAttribute("end", end);
        modelMap.addAttribute("reservationTime", reservationTime);
        modelMap.addAttribute("dateclosed",dateClosed);
        modelMap.addAttribute("limiteDate", allProperties.getMaxtime());
        List<Reservation> reservationsOnThisDate = reservationRepo.findAllByDate(Date.valueOf(localDate));
        modelMap.addAttribute("reservations", reservationsOnThisDate);
        modelMap.addAttribute("maxReservations",allProperties.getMaxPeople());
        return "reservation";
    }

    @Transactional
    @GetMapping("/doReservation")
    public String doReservation(HttpSession session, Timestamp start,Timestamp end,Date date,String login,ModelMap modelmap) throws MessagingException{
        Principal principal = (Principal) session.getAttribute("principal");
        if(principal == null){
            return "login";
        }
        Reservation existingReservation =reservationRepo.findByStartTimeAndEndTimeAndDateAndEmail(start, end, date, login);
        if(existingReservation != null){
            return "redirect:/calendrier";
        }

        Client getClient = clientRepo.findByEmail(login);
        Reservation newReservation = new Reservation();

        newReservation.setClient(getClient);
        newReservation.setDate(date);
        newReservation.setStartTime(start);
        newReservation.setEndTime(end);
        newReservation.setEmail(getClient.getEmail());

        List<Reservation> reservations = getClient.getReservations();
        reservations.add(newReservation);
        getClient.setReservations(reservations);

        ReservationKey reservationKey = new ReservationKey();
        reservationKey.setStartTime(start);
        reservationKey.setEndTime(end);
        reservationKey.setDate(date);
        reservationKey.setEmail(login);
        newReservation.setId(reservationKey);

        
        
        reservationRepo.save(newReservation);
        clientRepo.save(getClient);

        modelmap.addAttribute("reservation", newReservation);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String startFormated = newReservation.getStartTime().toLocalDateTime().format(formatter);
        String endFormated = newReservation.getEndTime().toLocalDateTime().format(formatter);

        String subject = "Création de votre réservation";
        String content = "Votre réservation du "+newReservation.getDate()+"\n"+"De "+startFormated+" à "+endFormated;

        String subjectAdmin = "Création d'une réservation";
        String contentAdmin = "Nouvelle réservation de "+getClient.getName()+" "+getClient.getFirstName()+" du "+newReservation.getDate()+"\n"+"De "+startFormated+" à "+endFormated;

        emailSender.sendEmail(getClient.getEmail(),subject,content);

        emailSender.sendEmail(allProperties.getAdminEmail(), subjectAdmin, contentAdmin);

        return "reservationdone";
    }

    @GetMapping("/closeDay")
    @Transactional
    public String closeDay(HttpSession session, Date date) throws MessagingException{
        Principal principal = (Principal) session.getAttribute("principal");
        if(principal == null){
            return "login";
        }
        ClosedDay newClosedDay = new ClosedDay();
        newClosedDay.setDate(date);
        closedDayRepo.save(newClosedDay);

        List<Reservation> reservations = reservationRepo.findAllByDate(date);
        for(Reservation reservation:reservations){
            Client getclient = reservation.getClient();
            getclient.getReservations().remove(reservation);
            reservationRepo.deleteByStartTimeAndEndTimeAndDateAndEmail(reservation.getStartTime(), reservation.getEndTime(), date, getclient.getEmail());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String startFormated = reservation.getStartTime().toLocalDateTime().format(formatter);
            String endFormated = reservation.getEndTime().toLocalDateTime().format(formatter);
             String subject = "Annulation de votre réservation";
            String content = "Votre réservation du "+reservation.getDate()+"\n"+"De "+startFormated+" à "+endFormated;
            emailSender.sendEmail(getclient.getEmail(),subject,content);
        }

        return "redirect:/calendrier";
    }

    @GetMapping("/closeCreneau")
    @Transactional
    public String closeCreneau(HttpSession session, String rawDate, Timestamp start, Timestamp end) throws MessagingException{
        Principal principal = (Principal) session.getAttribute("principal");
        if(principal == null){
            return "login";
        }
        LocalDate ld = LocalDate.parse(rawDate, DateTimeFormatter.ISO_LOCAL_DATE);
        Date date = Date.valueOf(ld);
        List<Reservation> reservations = reservationRepo.findAllByDate(date);
        for(Reservation reservation:reservations){
            Client getclient = reservation.getClient();
            getclient.getReservations().remove(reservation);
            reservationRepo.deleteByStartTimeAndEndTimeAndDateAndEmail(reservation.getStartTime(), reservation.getEndTime(), date, getclient.getEmail());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String startFormated = reservation.getStartTime().toLocalDateTime().format(formatter);
            String endFormated = reservation.getEndTime().toLocalDateTime().format(formatter);
             String subject = "Annulation de votre réservation";
            String content = "Votre réservation du "+reservation.getDate()+"\n"+" De "+startFormated+" à "+endFormated;
            emailSender.sendEmail(getclient.getEmail(),subject,content);
        }
        return "redirect:/reservation?id="+rawDate;
    }


    @GetMapping("/undoCloseDay")
    public String undoClodeDay(HttpSession session, Date date){
        Principal principal = (Principal) session.getAttribute("principal");
        if(principal == null){
            return "login";
        }
        ClosedDay oldClosedDay = closedDayRepo.findByDate(date);
        if(oldClosedDay != null){
            closedDayRepo.delete(oldClosedDay);
        }

        return "redirect:/calendrier";
    }

    @GetMapping("/doExeptionalDay")
    public String doExeptionalDay(HttpSession session, Date date){
        Principal principal = (Principal) session.getAttribute("principal");
        if(principal == null){
            return "login";
        }
        WorkDay newExeptionalDay = new WorkDay();
        newExeptionalDay.setDate(date);
        WorkDay existingExeptionnalDay = workDayRepo.findByDate(date);
        if(existingExeptionnalDay == null){
            workDayRepo.save(newExeptionalDay);
        }
        return "redirect:/calendrier";
    }
    @Transactional
    @GetMapping("/undoExeptionalDay")
    public String undoExeptionalDay(HttpSession session, Date date) throws MessagingException{
        Principal principal = (Principal) session.getAttribute("principal");
        if(principal == null){
            return "login";
        }
        WorkDay oldExeptionalDay = workDayRepo.findByDate(date);
        if(oldExeptionalDay != null){
            workDayRepo.delete(oldExeptionalDay);
        }

        List<Reservation> reservations = reservationRepo.findAllByDate(date);
        
        for(Reservation reservation:reservations){
            Client getclient = reservation.getClient();
            getclient.getReservations().remove(reservation);
            reservationRepo.deleteByStartTimeAndEndTimeAndDateAndEmail(reservation.getStartTime(), reservation.getEndTime(), date, getclient.getEmail());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String startFormated = reservation.getStartTime().toLocalDateTime().format(formatter);
            String endFormated = reservation.getEndTime().toLocalDateTime().format(formatter);
             String subject = "Annulation de votre réservation";
            String content = "Votre réservation du "+reservation.getDate()+"\n"+" De "+startFormated+" à "+endFormated;
            emailSender.sendEmail(getclient.getEmail(),subject,content);
        }
        return "redirect:/calendrier";
    }

    @GetMapping("/reservationsViewer")
    public String showReservationApplied(HttpSession session,ModelMap modelMap){
        Principal principal = (Principal) session.getAttribute("principal");
        if(principal == null){
            return "login";
        }
        String login = principal.getLogin();

        Client clientget = clientRepo.findByEmail(login);
        List<Reservation> reservations = clientget.getReservations();
        modelMap.addAttribute("reservationList", reservations);
        modelMap.addAttribute("limiteDate", allProperties.getMaxtime());

        return "reservationsViewer";
    }

    @GetMapping("/deleteReservation")
    @Transactional
    public String deleteReservation(Timestamp start,Timestamp end,Date date,String email,HttpSession session,ModelMap modelMap) throws MessagingException{
        Principal principal = (Principal) session.getAttribute("principal");
        if(principal == null){
            return "login";
        }
        String login ="";
        if(principal.isAdmin){
            login=email;
        }else{
            login=principal.getLogin();
        }
        Reservation reservation = reservationRepo.findByStartTimeAndEndTimeAndDateAndEmail(start, end, date,login);
        Client getclient = reservation.getClient();
        getclient.getReservations().remove(reservation);

        reservationRepo.deleteByStartTimeAndEndTimeAndDateAndEmail(start, end, date,getclient.getEmail());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String startFormated = reservation.getStartTime().toLocalDateTime().format(formatter);
        String endFormated = reservation.getEndTime().toLocalDateTime().format(formatter);

        String subject = "Annulation de votre réservation";
        String content = "Votre réservation du "+reservation.getDate()+"\n"+"De "+startFormated+" à "+endFormated;

        String subjectAdmin = "Annulation d'une réservation";
        String contentAdmin = "Annulation de la réservation de "+getclient.getName()+" "+getclient.getFirstName()+" du "+reservation.getDate()+"\n"+"De "+startFormated+" à "+endFormated;

        emailSender.sendEmail(getclient.getEmail(),subject,content);

        emailSender.sendEmail(allProperties.getAdminEmail(), subjectAdmin, contentAdmin);

        return "redirect:/calendrier";
    }
}
