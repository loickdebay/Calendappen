package fr.but3.saeweb.controlers;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import fr.but3.saeweb.others.AllProperties;
import fr.but3.saeweb.others.Principal;
import fr.but3.saeweb.repositories.ClosedDayRepo;
import fr.but3.saeweb.repositories.ReservationRepo;
import fr.but3.saeweb.repositories.WorkDayRepo;
import jakarta.servlet.http.HttpSession;
import fr.but3.saeweb.entities.ClosedDay;
import fr.but3.saeweb.entities.Reservation;
import fr.but3.saeweb.entities.WorkDay;

@Controller
class controlerCalendrier {
    
    @Autowired
    AllProperties allProperties;

    @Autowired
    ClosedDayRepo closedDayRepo;

    @Autowired
    WorkDayRepo workDayRepo;

    @Autowired
    ReservationRepo reservationRepo;

    @GetMapping("/calendrier")
    public String openCalendar(HttpSession session, ModelMap modelMap){
        Principal principal = (Principal) session.getAttribute("principal");
        if(principal == null){
            return "login";
        }
        modelMap.addAttribute("workdays",allProperties.getWorkday());
        List<ClosedDay> closeDays = (List<ClosedDay>) closedDayRepo.findAll();
        modelMap.addAttribute("closedDays", closeDays);
        List<WorkDay> exeptionalWorkDays = (List<WorkDay>) workDayRepo.findAll();
        List<Reservation> reservations = (List<Reservation>) reservationRepo.findAll();
        modelMap.addAttribute("reservations", reservations);
        modelMap.addAttribute("exeptionalWorkDays", exeptionalWorkDays);

        ArrayList<LocalTime> openingTime = allProperties.getOpeningTime();
        ArrayList<LocalTime> closingTime = allProperties.getClosingTime();
        LocalTime reservationTime = allProperties.getReservationTime();
        
        int maxPeople = allProperties.getMaxPeople();
        modelMap.addAttribute("maxPeople", maxPeople);
        List<Integer> creneauxCounts = new ArrayList<>();
        for (int i = 0; i < openingTime.size(); i++) {
            LocalTime OpTime = openingTime.get(i);
            LocalTime ClTime = closingTime.get(i);

            int creneauxCount = calculateCreneauxCount(OpTime, ClTime, reservationTime);
            creneauxCounts.add(creneauxCount);
        }
        
        modelMap.addAttribute("creneauxCount", creneauxCounts);
        return "calendrier";
    }
    private static int calculateCreneauxCount(LocalTime openingTime, LocalTime closingTime, LocalTime reservationTime) {
        long intervalMinutes = ChronoUnit.MINUTES.between(openingTime, closingTime);
        long reservationMinutes = ChronoUnit.MINUTES.between(LocalTime.MIDNIGHT, reservationTime);

        int creneauxCount = (int) (intervalMinutes / reservationMinutes);

        return creneauxCount;
    }
}
