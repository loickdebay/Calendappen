<%@ page import="fr.but3.saeweb.entities.Client" %>
<%@ page import="fr.but3.saeweb.others.Principal" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="/css/styleInfo.css">

    <title>Compte</title>
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
            <a href="/calendrier">
            <button class='orangebutton'>Retour</button>
            </a>
            <a href="/disconnect">
            <button>Déconnexion</button>
            </a>
        </div>
    </header>
<div>
<br>
    <img src="download/${client.fileName}" alt="Description of the image" width="100" height="100">

</div>
    <br>
<a href="upload" class="button">Modifier la photo de profil</a>
<div>
<form action="/editAccount" method="post">

    <p>Nom: ${client.name}</p>
    <label for="name">Modifier nom:</label>
    <input type="text" id="name" name="name" placeholder="Nom"/>

    <p>Prénom: ${client.firstName}</p>
    <label for="firstName">Modifier Prénom:</label>
    <input type="text" id="firstName" name="firstName" placeholder="Prénom" />

    <p>Numéro téléphone: ${client.phoneNumber}</p>
    <label for="phoneNumber">Modifier Numéro de téléphone:</label>
    <input type="text" id="phoneNumber" name="phoneNumber" value="${client.phoneNumber}"/>

    <p>Email: ${client.email}</p>
    <label for="mail">Modifier l'Email:</label>
    <input type="email" id="email" name="email" placeholder="Email"/>

    <label for="pass">Modifier Mot de passe (8 characters minimum)</label>
    <input type="password" id="password" name="password" minlength="8" placeholder="Mot de passe"/>

    <button type="submit">Modifier les informations du compte</button>
</div>
    <div>
        <%
            Object warningAccount = request.getAttribute("WarningAccount");
            Object successAccount = request.getAttribute("SuccessAccount");
            Object nothingAppend = request.getAttribute("NothingAppend");
            if (successAccount != null) {
                out.println("<p style='color: green;'>" + successAccount + "</p>");
            }

            if (warningAccount != null) {
                out.println("<p style='color: red;'>" + warningAccount + "</p>");
            }

            if(nothingAppend != null){
                out.println("<p style='color: blue;'>" + nothingAppend + "</p>");
            }

            if (warningAccount == null && successAccount == null && nothingAppend == null) {
                out.println("<!-- Nothing -->");
            }
        %>
    </div>

</form>

</body>
</html>
