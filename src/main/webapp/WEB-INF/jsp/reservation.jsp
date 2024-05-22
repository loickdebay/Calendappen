<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate, java.time.*" %>
<%@ page import="java.time.LocalDateTime, java.time.*" %>
<%@ page import="java.time.LocalTime, java.time.*" %>
<%@ page import="java.sql.Timestamp, java.time.*" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="fr.but3.saeweb.others.Principal" %>
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
    <%
Principal principal = (Principal)session.getAttribute("principal");
String workdays=(String) session.getAttribute("workdays");
if(principal.isAdmin){
    out.println("<header class=adminHeader>");
    out.println("Connexion en tant qu'Admin");
}else{
    out.println("<header>");
}
%>
        <div>
            
            <a href ='calendrier'>
            <button class='orangebutton'>Retour</button>
            </a>
            <a href="/editAccount">
            <button>Modifier son compte</button>
            </a>
            <a href="/disconnect">
            <button>Déconnexion</button>
            </a>
        </div>
    </header>
        <h1>Créneaux de réservations</h1>
        <%
        String rawDatetest = (String) request.getAttribute("id");
        boolean dateclosed = (boolean) request.getAttribute("dateclosed");
        boolean isED = (boolean) request.getAttribute("isED");
        boolean isNonWorkingDay = (boolean) request.getAttribute("isNonWorkingDay");

        %>
        <%
            if(principal.isAdmin){
                if(!isNonWorkingDay){
                    if(dateclosed){
                    out.println("<form action='undoCloseDay' method='get'>");
                    out.println("<input type='hidden' name='date' value='" + rawDatetest + "'>");
                    out.println("<button type='submit'>Remettre la journée</button>");
                    out.println("</form>");
                    }else{
                        out.println("<form action='closeDay' method='get'>");
                        out.println("<input type='hidden' name='date' value='" + rawDatetest + "'>");
                        out.println("<button type='submit'>Annuler la journée</button>");
                        out.println("</form>");
                        
                    }
                }else{
                    if(isED){
                        out.println("<form action='/undoExeptionalDay' method='get'>");
                        out.println("<input type='hidden' name='date' value='" + rawDatetest + "'>");
                        out.println("<button type='submit'>Fermer la journée exceptionnelle</button>");
                        out.println("</form>");
                    }else{
                        out.println("<form action='/doExeptionalDay' method='get'>");
                        out.println("<input type='hidden' name='date' value='" + rawDatetest + "'>");
                        out.println("<button type='submit'>Ouvrir la journée exceptionnelle</button>");
                        out.println("</form>");
                        
                    }
                }
            }

        %>
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



    if(principal.isAdmin){
        out.println("<form action='gestionAdmin' method='get' class='redbutton'>");
        out.println("<input type='hidden' name='start' value='"+timestampStart+"'>");
        out.println("<input type='hidden' name='end' value='"+timestampEnd+"'>");
        out.println("<input type='hidden' name='date' value='"+rawDate+"'>");
        out.println("<button class='redbutton' type='submit'>Gérer</button>");
        out.println("</form>");
    }

    if(!principal.isAdmin){
        LocalDate currentDate = LocalDate.now();
        Client client = principal.getClient();
        Reservation target = new Reservation();
        target.setStartTime(timestampStart);
        target.setEndTime(timestampEnd);
        target.setDate(Date.valueOf(dateTitle));
        target.setEmail(client.getEmail());
        target.setClient(client);

        boolean printbutton = true;
        if(currentDate.equals(localDate)){
            LocalDateTime currentLocalDateTime = LocalDateTime.now();
            LocalDateTime currentDateTime = LocalDateTime.now();
            Timestamp currentTimestamp = Timestamp.valueOf(currentDateTime);
            if(currentTimestamp.after(timestampStart)){
                printbutton=false;
            }
        }

        if(howManyReservations<maxReservations&&printbutton&&reservations==null){
            out.println("<form action='doReservation' method='get'>");
            out.println("<input type='hidden' name='start' value='"+timestampStart+"'>");
            out.println("<input type='hidden' name='end' value='"+timestampEnd+"'>");
            out.println("<input type='hidden' name='date' value='"+rawDate+"'>");
            out.println("<input type='hidden' name='login' value='"+principal.getLogin()+"'>");
            out.println("<input type='hidden' name='id' value='"+rawDate+"'>");
            out.println("<button type='submit'>Prendre rendez-vous</button>");
            out.println("</form>");
        }

        else{
            boolean reservationFound = false;
            for(Reservation reservation:reservations){
                if(reservation.getStartTime().equals(target.getStartTime()) && reservation.getEndTime().equals(target.getEndTime()) && 
                    reservation.getDate().equals(target.getDate()) && reservation.getEmail().equals(target.getEmail())){
                    reservationFound = true;
                    break;
                }
            }
            int fordatelimite=(int)request.getAttribute("limiteDate");

            if(reservationFound && !currentDate.isAfter(dateTitle.minusDays(fordatelimite))){
                out.println("<form method='get' action='deleteReservation'>");
                out.println("<input type='hidden' name='start' value='" + timestampStart + "'>");
                out.println("<input type='hidden' name='end' value='" + timestampEnd + "'>");
                out.println("<input type='hidden' name='date' value='" + rawDate + "'>");
                out.println("<button class='orangebutton' type='submit'>Annuler le rendez vous</button>");
                out.println("</form>");
            }

            if(!reservationFound&&howManyReservations<maxReservations&&printbutton){
                out.println("<form action='doReservation' method='get'>");
                out.println("<input type='hidden' name='start' value='"+timestampStart+"'>");
                out.println("<input type='hidden' name='end' value='"+timestampEnd+"'>");
                out.println("<input type='hidden' name='date' value='"+rawDate+"'>");
                out.println("<input type='hidden' name='login' value='"+principal.getLogin()+"'>");
                out.println("<input type='hidden' name='id' value='"+rawDate+"'>");
                out.println("<button type='submit'>Prendre rendez-vous</button>");
                out.println("</form>"); 
            }
        }
    }

    out.println("</td>");
    out.println("</tr>");
}
%>


</table>
<body>


