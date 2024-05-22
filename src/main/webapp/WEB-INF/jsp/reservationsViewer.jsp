<%@ page import="fr.but3.saeweb.entities.Reservation" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.io.*, java.time.LocalDate" %>
<%@ page import="fr.but3.saeweb.others.Principal" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="/css/styleReservationViewer.css">
    <title>Horaire des réservations disponible</title>
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
            
            <a href ='calendrier'>
            <button class="orangebutton">Retour</button>
            </a>
            <a href="/editAccount">
            <button class="header-button">Modifier son compte</button>
            </a>
            <a href="/disconnect">
            <button class="header-button">Déconnexion</button>
            </a>
        </div>
    </header>
<h1>Vos réservations</h1>
<table>
<tr>
    <th>Date</th>
    <th>Début</th>
    <th>Fin</th>
    <th>Action</th>
</tr>
<%
List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservationList");
SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"); // Format for extracting time
SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Format for the date
LocalDate localDate = LocalDate.now();


if(reservations.isEmpty()){
    out.println("Vous n'avez pas de réservations pour le moment");
}else{
    for(Reservation reservation : reservations){
    out.println("<tr>");
    out.println("<td>");
    out.println(dateFormat.format(new Date(reservation.getDate().getTime())));
    out.println("</td>");
    out.println("<td>");
    out.println(timeFormat.format(new Date(reservation.getStartTime().getTime())));
    out.println("</td>");
    out.println("<td>");
    out.println(timeFormat.format(new Date(reservation.getEndTime().getTime())));
    out.println("</td>");
    out.println("<td>");

    int fordatelimite=(int)request.getAttribute("limiteDate");

    if(localDate.isBefore(reservation.getDate().toLocalDate()) && !localDate.isAfter(reservation.getDate().toLocalDate().minusDays(fordatelimite))){
        out.println("<form method='get' action='deleteReservation'>");
        out.println("<input type='hidden' name='start' value='" + reservation.getStartTime() + "'>");
        out.println("<input type='hidden' name='end' value='" + reservation.getEndTime() + "'>");
        out.println("<input type='hidden' name='date' value='" + reservation.getDate() + "'>");
        out.println("<button class='cancelbutton' type='submit'>Annuler</button>");
        out.println("</form>");    out.println("</td>");
    }
    out.println("<tr>");
}
}


%>

