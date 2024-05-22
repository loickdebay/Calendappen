<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate, java.time.*" %>
<%@ page import="java.time.LocalDateTime, java.time.*" %>
<%@ page import="java.time.LocalTime, java.time.*" %>
<%@ page import="java.sql.Timestamp, java.time.*" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="fr.but3.saeweb.entities.Reservation" %>
<%@ page import="fr.but3.saeweb.entities.Client" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.sql.Date" %>
<%@ page import="java.util.List" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="/css/styleReservation.css">
    <title>Horaire des réservations disponible</title>
</head>
<body>
<header>
        <div>
            
            <a href ='public'>
            <button class='orangebutton'>Retour</button>
            </a>
            <a href="login">
            <button>Modifier son compte</button>
            </a>
            <a href="login">
            <button>Déconnexion</button>
            </a>
        </div>
    </header>
        <h1>Créneaux de réservations</h1>
        <%String rawDatetest = (String) request.getAttribute("id");%>
<table>
<tr>
    <th>Début</th>
    <th>Fin</th>
    <th>Nombre de réservations</th>
    <th></th>
</tr>
<%
String rawDate = (String) request.getAttribute("id");
LocalTime start = (LocalTime) request.getAttribute("start");
LocalTime end = (LocalTime) request.getAttribute("end");
LocalTime timeSlotValue = (LocalTime) request.getAttribute("reservationTime");
List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");



DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
LocalDate dateTitle = LocalDate.parse(rawDate, formatter);
String formattedDate = dateTitle.format(DateTimeFormatter.ofPattern("d MMMM yyyy"));
out.println("<h1>"+formattedDate+"</h1>");

LocalTime basedTime = LocalTime.of(start.getHour(),start.getMinute());
while( basedTime.isBefore(end) || basedTime.plusHours(timeSlotValue.getHour()).plusMinutes(timeSlotValue.getMinute()).equals(end)){

    out.println("<tr>");
    out.println("<td>");
    out.println(basedTime);
    out.println("</td>");
    LocalTime startTime = basedTime;

    basedTime = basedTime.plusHours(timeSlotValue.getHour());
    basedTime = basedTime.plusMinutes(timeSlotValue.getMinute());

    LocalTime endTime = basedTime;

    String[] date = rawDate.split("-");

    // Crée une instance de LocalDate
    LocalDate localDate = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
    
    LocalDateTime startTimeLocalDate = localDate.atTime(startTime);
    Timestamp timestampStart = Timestamp.valueOf(startTimeLocalDate);

    LocalDateTime endTimeLocalDate = localDate.atTime(endTime);
    Timestamp timestampEnd = Timestamp.valueOf(endTimeLocalDate);
    
    out.println("<td>");
    out.println(basedTime);
    out.println("</td>");
    int howManyReservations = 0;
    for(Reservation r : reservations){
        if (timestampStart.compareTo(r.getStartTime()) == 0 && timestampEnd.compareTo(r.getEndTime()) == 0) {
            howManyReservations++;
        }

    }
    int maxReservations = (int) request.getAttribute("maxReservations");
    out.println("<td>");
    out.println(howManyReservations+"/"+maxReservations);
    out.println("</td>");

    out.println("<td>");




  
        LocalDate currentDate = LocalDate.now();
        Reservation target = new Reservation();
        target.setStartTime(timestampStart);
        target.setEndTime(timestampEnd);
        target.setDate(Date.valueOf(dateTitle));



        if(howManyReservations<maxReservations){
            out.println("<form action='login' method='get'>");
            out.println("<button type='submit'>Prendre rendez-vous</button>");
            out.println("</form>");
        }
        
        else{
            boolean reservationFound = false;
            for(Reservation reservation:reservations){
                if(reservation.getStartTime().equals(target.getStartTime()) && reservation.getEndTime().equals(target.getEndTime()) && 
                    reservation.getDate().equals(target.getDate()) && reservation.getEmail().equals(target.getEmail()) ){
                    reservationFound = true;
                    break;
                }
            }

            if(!reservationFound&&howManyReservations<maxReservations){
                out.println("<form action='login' method='get'>");
                out.println("<button type='submit'>Prendre rendez-vous</button>");
                out.println("</form>"); 
            }
        }
    

    out.println("</td>");
    out.println("</tr>");
}
%>


</table>
<body>


