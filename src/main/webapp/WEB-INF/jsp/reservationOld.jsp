<%@ page import="java.time.LocalDate, java.time.*" %>
<%@ page import="java.time.LocalDateTime, java.time.*" %>
<%@ page import="java.time.LocalTime, java.time.*" %>
<%@ page import="java.sql.Timestamp, java.time.*" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>

        body {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
            flex-direction: column; /* Ajout pour aligner les éléments en colonne */
        }

    </style>
    <title>Horraire des réservations disponible</title>
</head>
<body>

    <%

    LocalTime start = ${start}
    LocalTime end = ${end}
    LocalTime timeSlotValue = ${timeSlotValue}
    LocalDate localDate = ${localDate}


    LocalTime basedTime = LocalTime.of(start.getHour(),start.getMinute());

    while( basedTime.isBefore(${end}) || basedTime.equals(${end})){

        out.println("Début: "+basedTime);

        LocalTime startTime = basedTime;

        basedTime = basedTime.plusHours(timeSlotValue.getHour());
        basedTime = basedTime.plusMinutes(timeSlotValue.getMinute());

        LocalTime endTime = basedTime;

        ReservationDAO reservationDAO = new ReservationDAO();

        String[] date = rawDate.split("-");

        // Crée une instance de LocalDate
        LocalDate localDate = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        
        LocalDateTime startTimeLocalDate = localDate.atTime(startTime);
        Timestamp timestampStart = Timestamp.valueOf(startTimeLocalDate);

        LocalDateTime endTimeLocalDate = localDate.atTime(endTime);
        Timestamp timestampEnd = Timestamp.valueOf(endTimeLocalDate);

        out.println("    Fin: "+(basedTime));
        ArrayList<Reservation> reservations = new ArrayList<>();
        try {
            reservations = reservationDAO.findAllForOneDateAndSameStartEnd(timestampStart, timestampEnd);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        out.println("  "+reservations.size()+"/"+parameters.getMaxPeople());

        if(reservations.size() < parameters.getMaxPeople()){
            out.println("<a href='controler-Reservation?start="+timestampStart+"&end="+timestampEnd+"&date="+rawDate+"'> Réserver </a>");
            out.println("<br>");
        }else{
            out.println("<br>");
        }
        
    }

    %>
    

</body>
</html>
