<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> <!-- Ajout de cet attribut -->
    <link rel="stylesheet" type="text/css" href="/css/styleLogin.css">
    <title>Se connecter</title>
</head>
<body>
<header>
<h2>Comme Max Verstappen, soyez toujours dans les temps</h2>
<h1>Avec Calendappen !</h1>
</header>


<div class="container">
<img src="https://static.thenounproject.com/png/297850-200.png" alt="formule1Logo" >
    <form action="/login" method="post">
        <label for="mail">Email</label>
        <input type="email" id="email" name="email" />

        <label for="pass">Password</label>
        <input type="password" id="password" name="password" minlength="8" required />
          
        <button type="submit">Se connecter</button>
    </form>

    <div>
        <p style="color: rgb(174, 16, 16);">${warning}</p>
    </div>

    <div>
        <p>Pas de compte ?</p>
        <a href="/createAnAccount">
            <button class="button">Enregistrer son compte</button>
        </a>
        <a href="/public">
            <button class="button">Voir le calendrier public</button>
        </a>
    </div>
</div>

</body>
</html>
