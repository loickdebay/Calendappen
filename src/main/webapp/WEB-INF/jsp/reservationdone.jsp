<%@ page import="fr.but3.saeweb.entities.Reservation" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="fr.but3.saeweb.others.Principal" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="/css/styleReservationViewer.css">
    <title>Récapitulatif de votre réservation</title>
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
            <a href ='reservationsViewer'>
            <button>Mes rendez-vous</button>
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

Reservation reservation = (Reservation) request.getAttribute("reservation");

%>
<h1>Réservation effectuée :</h1>

<table>
<tr>
    <th>Date</th>
    <th>Début</th>
    <th>Fin</th>
</tr>
<%
SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"); // Format for extracting time
SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Format for the date

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
out.println("<tr>");

            

%>
<table>
<a href="calendrier">
<button class='greenbutton'>Ok</button>
</a>
</body>
</html>