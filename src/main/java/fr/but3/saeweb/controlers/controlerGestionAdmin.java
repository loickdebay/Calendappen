package fr.but3.saeweb.controlers;

import fr.but3.saeweb.entities.Reservation;
import fr.but3.saeweb.others.AllProperties;
import fr.but3.saeweb.others.Principal;
import fr.but3.saeweb.repositories.ClientRepo;
import fr.but3.saeweb.repositories.ClosedDayRepo;
import fr.but3.saeweb.repositories.ReservationRepo;
import jakarta.servlet.http.HttpSession;

import java.sql.Timestamp;
import java.util.List;
import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class controlerGestionAdmin {

    @Autowired
    ClientRepo clientRepo;

    @Autowired
    ClosedDayRepo closedDayRepo;

    @Autowired
    ReservationRepo reservationRepo;

    @Autowired
    AllProperties allProperties;

    @GetMapping("/gestionAdmin")
    public String showReservation(HttpSession session, Timestamp start, Timestamp end, String date, ModelMap modelMap){
        Principal principal = (Principal) session.getAttribute("principal");
        if(principal == null){
            return "login";
        }
        Timestamp ts1 = (Timestamp) start;
        Timestamp ts2 = (Timestamp) end;
        Date newDate = Date.valueOf(date);
        List<Reservation> reservations = reservationRepo.findByStartTimeAndEndTimeAndDate(ts1, ts2, newDate);
        modelMap.addAttribute("start", start);
        modelMap.addAttribute("end", end);
        modelMap.addAttribute("reservations", reservations);
        modelMap.addAttribute("date", date);
        return "gestionAdmin";
    }
}
