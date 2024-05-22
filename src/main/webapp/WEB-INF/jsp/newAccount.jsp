<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="/css/styleAccountCreation.css">

    <title>Se connecter</title>
</head>
<body>
<header>
        <div>
            
            <a href ='login'>
            <button class='orangebutton'>Retour</button>
            </a>
            <a href="/public">
            <button>Acceuil</button>
            </a>
        </div>
    </header>
<form action="/createAnAccount" method="post">

    <label for="name">Name:</label>
    <input type="text" id="name" name="name" />

    <label for="firstName">FirstName:</label>
    <input type="text" id="firstName" name="firstName" />

    <label for="phoneNumber">PhoneNumber:</label>
    <input type="text" id="phoneNumber" name="phoneNumber" />

    <label for="mail">Email</label>
    <input type="email" id="email" name="email" />

    <label for="pass">Password (8 characters minimum)</label>
    <input type="password" id="password" name="password" minlength="8" required />

    <button type="submit">Enregistrer le compte</button>

    <div>
        <p style="color: rgb(174, 16, 16);">${WarningAccount}</p>
    </div>

</form>

<a href="/login" class="connect-button">
    <button>Se connecter</button>
</a>

</body>
</html>
