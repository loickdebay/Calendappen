package fr.but3.saeweb.others;


import fr.but3.saeweb.entities.Client;
import lombok.Data;

@Data
public class Principal {
    private String login;
    public boolean isAdmin;
    private String workday;
    private Client client;
}
