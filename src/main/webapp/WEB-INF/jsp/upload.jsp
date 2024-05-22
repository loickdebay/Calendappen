<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.nio.file.Files" %>
<%@ page import="java.nio.file.Path" %>
<%@ page import="java.nio.file.Paths" %>
<%@ page import="java.io.IOException" %>
<%@ page import="fr.but3.saeweb.others.Principal" %>

<!DOCTYPE html>
<link rel="stylesheet" type="text/css" href="/css/styleUpload.css">

<html>
<head>
    <meta charset="UTF-8">
    <title>Modification photo de profil</title>
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
            <a href="/editAccount">
            <button class='orangebutton'>Retour</button>
            </a>
        </div>
    </header>
    <h1>Modification photo de profil</h1>

    <% if (request.getAttribute("requestImg") != null) { %>
        <img src="<%= request.getAttribute("requestImg") %>" alt="Image Téléchargée" width="300" height="300" />
    <% } else { %>
        <p>Aucune image téléchargée.</p>
    <% } %>


    <form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
        Select File: <input type="file" name="file" required><br>
        <input type="submit" value="Upload">
    </form>
</body>
</html>
