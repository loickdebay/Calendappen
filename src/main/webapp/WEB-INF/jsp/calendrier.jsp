<%@ page import="java.io.*, java.time.LocalDate" %>
<%@ page import="org.springframework.beans.factory.annotation.Autowired" %>
<%@ page import="org.springframework.jdbc.core.JdbcTemplate" %>
<%@ page import="fr.but3.saeweb.entities.Reservation" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.sql.Date" %>
<%-- <%@ page import="fr.but3.saeweb.repositories.WorkdayRepo" %> --%>
<%@ page import="fr.but3.saeweb.others.Principal" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="fr.but3.saeweb.entities.ClosedDay" %>
<%@ page import="fr.but3.saeweb.entities.WorkDay" %>
<%@ page import="java.sql.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>

<html>
<head>
    <title>Calendrier</title>
    <link rel="stylesheet" type="text/css" href="/css/styleCalendrier.css">
    <style>
        .calendar td { font-size: 30px; }
        table.calendar { width: 50%; }
        .calendar-container {
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .navigation {
            font-size: 30px;
        } 
    </style>
</head>
<body>
<%
Principal principal = (Principal)session.getAttribute("principal");
if(principal.isAdmin){
    out.println("<header class=adminHeader>");
    out.println("Connexion en tant qu'Admin");
}else{
    out.println("<header>");
}
%>
        <div>
            <%
            if(!principal.isAdmin){
                out.println("<a href ='reservationsViewer'><button>Mes rendez-vous</button></a>");
            }
            %>
            <a href="/editAccount">
            <button>Modifier son compte</button>
            </a>
            <a href="/disconnect">
            <button>Déconnexion</button>
            </a>
        </div>
    </header>

<%
    //WorkdayRepo workdayRepo;
    LocalDate localDate = LocalDate.now();;
    String test = "test";
    //String workdays = (String) request.getAttribute("workdays");
    int maxPeople = (int) request.getAttribute("maxPeople");
    String workdays = principal.getWorkday();
%>

<%
    
    if (request.getParameter("mois") != null) {
        localDate = LocalDate.of(Integer.parseInt(request.getParameter("annee")), Integer.parseInt(request.getParameter("mois")), 1);
    }
    int year = localDate.getYear();
    int month = localDate.getMonthValue();
    String monthName = localDate.getMonth().toString();
    int dayOfWeek = localDate.withDayOfMonth(1).getDayOfWeek().getValue();
%>
<h1 class='moisCalendrier'><%=monthName.toLowerCase().substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase()+" "+year%></h1>

<div class='calendar-container'>
<table class='calendar' border='1'>
<tr>
<%
    String everyDayOfWeek="Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday";
    for(String s : everyDayOfWeek.split(",")){
        if(workdays.contains(s.toLowerCase())){
            out.println("<th>"+s.substring(0,3)+"</th>");
        }else{
            out.println("<th class=\"grayBackground\">"+s.substring(0,3)+"</th>");
        }
    }
%>
</tr>

<%
List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
List<WorkDay> exeptionalWorkDays = (List<WorkDay>) request.getAttribute("exeptionalWorkDays");
List<ClosedDay> closedDays = (List<ClosedDay>) request.getAttribute("closedDays");
List<Integer> creneauxCount = (List<Integer>) request.getAttribute("creneauxCount");
for (int i = 1; i < dayOfWeek; i++) {
    out.println("<td></td>");
}
LocalDate ldCal = LocalDate.of(localDate.getYear(),localDate.getMonth(),1);
while (ldCal.getMonthValue() == month) {
    String monthValue = localDate.getMonthValue()+"";
    String dayValue = ldCal.getDayOfMonth()+"";
    if(monthValue.length()<2){
        monthValue="0"+monthValue;
    }
    if(dayValue.length()<2){
        dayValue="0"+dayValue;
    }
    String cellId = + year + "-" + monthValue + "-"+ dayValue;
    String cellClass="calendar-cell";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    java.util.Date utilDate = sdf.parse(cellId);
    Date sqlDate = new Date(utilDate.getTime());
    ClosedDay targetCloseDay = new ClosedDay();
    WorkDay targetWorkDay = new WorkDay();
    targetCloseDay.setDate(sqlDate);
    targetWorkDay.setDate(sqlDate);
    boolean showPercentage=true;

    if(exeptionalWorkDays!=null && exeptionalWorkDays.contains(targetWorkDay)){
        cellClass="";

    }else{
        if(!workdays.contains(ldCal.getDayOfWeek().name().toLowerCase()) || (closedDays != null && closedDays.contains(targetCloseDay))){
            cellClass="non-working-day";
            showPercentage=false;
        }else{
            cellClass="";
        }
    }
    out.println("<td class='" + cellClass + " hoverable-cell' id='" + cellId + "'>");
    int howManyReservations = 0;
    for (Reservation r : reservations){
        if(r.getDate().toString().equals(cellId)){
            howManyReservations++;
        }
    }
    if(showPercentage&&ldCal.isAfter(LocalDate.now().minusDays(1))){
        out.println("<div class='reservation-info' style='display:none;'>");
        int totalSlots=creneauxCount.get(ldCal.getDayOfWeek().getValue() - 1);
        totalSlots=totalSlots*maxPeople;
        double percentage = (howManyReservations / (double) totalSlots) * 100;
        double roundedPercentage = Math.round(percentage * 10.0) / 10.0;
        out.println("Occupation : "+roundedPercentage+"%");
        out.println("</div>");
    }


    out.println("<h3>" + ldCal.getDayOfMonth() + "</h3>");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate localDateCheck = LocalDate.parse(cellId, formatter);

    if(!localDateCheck.isBefore(LocalDate.now())){
        if(exeptionalWorkDays!=null && exeptionalWorkDays.contains(targetWorkDay) && !principal.isAdmin()){
            out.println("<form action='reservation' method='get'>");
            out.println("<input type='hidden' name='id' value='" + cellId + "'>");
            out.println("<button type='submit'>Accès</button>");
            out.println("</form>");
        }
        if(closedDays != null && closedDays.contains(targetCloseDay) && !principal.isAdmin()){
            if(workdays.contains(ldCal.getDayOfWeek().name().toLowerCase())&& !closedDays.contains(targetCloseDay)){
                out.println("<form action='reservation' method='get'>");
                out.println("<input type='hidden' name='id' value='" + cellId + "'>");
                out.println("<button type='submit'>Accès</button>");
                out.println("</form>");
            }
        }else{
            if(workdays.contains(ldCal.getDayOfWeek().name().toLowerCase())||principal.isAdmin()){
                out.println("<form action='reservation' method='get'>");
                out.println("<input type='hidden' name='id' value='" + cellId + "'>");
                out.println("<button type='submit'>Accès</button>");
                out.println("</form>");
            }
        } 
    }

    
    out.println("</td>");

    if (ldCal.getDayOfWeek().name().toLowerCase().equals("sunday")) {
        out.println("</tr><tr>");
    }
    ldCal=ldCal.plusDays(1);
}


%>
<div class="button-container">
    <a href="calendrier?mois=<%= localDate.minusMonths(1).getMonthValue() %>&annee=<%= localDate.minusMonths(1).getYear() %>">
        <button>Précédent</button>
    </a>
    <a href="calendrier?mois=<%= LocalDate.now().getMonthValue() %>&annee=<%= LocalDate.now().getYear() %>">
        <button>Mois actuel</button>
    </a>
    <a href="calendrier?mois=<%= localDate.plusMonths(1).getMonthValue() %>&annee=<%= localDate.plusMonths(1).getYear() %>">
        <button>Suivant</button>
    </a>
</div>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        var hoverableCells = document.querySelectorAll('.hoverable-cell');

        hoverableCells.forEach(function (cell) {
            cell.addEventListener('mouseover', function () {
                var reservationInfo = cell.querySelector('.reservation-info');
                if (reservationInfo) {
                    reservationInfo.style.display = 'block';
                }
            });

            cell.addEventListener('mouseout', function () {
                var reservationInfo = cell.querySelector('.reservation-info');
                if (reservationInfo) {
                    reservationInfo.style.display = 'none';
                }
            });
        });
    });
</script>
</body>
</html>
