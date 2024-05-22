<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate, java.time.*" %>
<%@ page import="java.time.LocalDateTime, java.time.*" %>
<%@ page import="java.time.LocalTime, java.time.*" %>
<%@ page import="java.sql.Timestamp, java.time.*" %>
<%@ page import="java.util.List"%>
<%@ page import="java.text.SimpleDateFormat, java.util.Date"%>
<%@ page import="fr.but3.saeweb.others.Principal" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="fr.but3.saeweb.entities.Reservation" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="/css/styleReservation.css">
    <title>Créneaux reservés</title>
</head>
<body>
<%
Principal principal = (Principal)session.getAttribute("principal");
String rawDate = (String) request.getAttribute("date");
if(principal.isAdmin){
    out.println("<header class=adminHeader>");
    out.println("Connexion en tant qu'Admin");
}else{
    out.println("<header>");
}
%>
        <div>
            <%
            out.println("<a href ='/reservation?id="+rawDate+"'>");
            %>
            <button>Retour</button>
            </a>
            <a href="/editAccount">
            <button>Modifier son compte</button>
            </a>
            <a href="/disconnect">
            <button>Déconnexion</button>
            </a>
        </div>
    </header>
    <%
    
    List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
    Timestamp start = (Timestamp) request.getAttribute("start");
    Timestamp end = (Timestamp) request.getAttribute("end");
    out.println("<h1>Créneaux de réservations au "+rawDate+"</h1>");
    %>
        
        <%String rawDatetest = (String) request.getAttribute("id");%>
        <%
            if(principal.isAdmin){
                if(request.getAttribute("dateclosed") == null){
                    out.println("<form action='closeCreneau' method='get'>");
                    out.println("<input type='hidden' name='rawDate' value='" + rawDate + "'>");
                    out.println("<input type='hidden' name='start' value='" + start + "'>");
                    out.println("<input type='hidden' name='end' value='" + end + "'>");
                    out.println("<button type='submit'>Vider le créneau</button>");
                    out.println("</form>");
                }
            }
        %>

<table>
<tr>
    <th>Début</th>
    <th>Fin</th>
    <th>Utilisateur-Email</th>
    <th>Nom</th>
    <th>Action</th>
</tr>
<%
for (Reservation r : reservations){
    out.println("<tr>");
    out.println("<td>");
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    String timeStart = dateFormat.format(new Date(r.getStartTime().getTime()));
    out.println(timeStart);
    out.println("</td>");
    out.println("<td>");
    String timeEnd = dateFormat.format(new Date(r.getEndTime().getTime()));
    out.println(timeEnd);
    out.println("</td>");
    out.println("<td>");
    out.println(r.getClient().getEmail());
    out.println("</td>");
    out.println("<td>");
    out.println(r.getClient().getName());
    out.println("</td>");
    out.println("<td>");
    out.println("<form method='get' action='deleteReservation'>");
    out.println("<input type='hidden' name='start' value='" + r.getStartTime() + "'>");
    out.println("<input type='hidden' name='end' value='" + r.getEndTime() + "'>");
    out.println("<input type='hidden' name='date' value='" + r.getDate() + "'>");
    out.println("<input type='hidden' name='email' value='" + r.getClient().getEmail() + "'>");
    out.println("<button class='redbutton' type='submit'>Annuler</button>");
    out.println("</form>");    out.println("</td>");
    out.println("</td>");
}


%>