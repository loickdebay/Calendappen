package fr.but3.saeweb.entities;

import java.util.List;


import fr.but3.saeweb.others.CreationClientRules;
import fr.but3.saeweb.others.ModificationClientRules;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class Client {

    private static final int MAX_LENGTH_PHONENUMBER = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long idClient;
    @NotBlank(message = "is required", groups = {CreationClientRules.class})
    private String email;
    @NotBlank(message = "is required", groups = {CreationClientRules.class})
    private String password;
    private String role="user";
    @NotBlank(message = "is required", groups = {CreationClientRules.class})
    private String name;
    @NotBlank(message = "is required", groups = {CreationClientRules.class})
    private String firstName;
    @Size(min=MAX_LENGTH_PHONENUMBER,max=MAX_LENGTH_PHONENUMBER, groups = {CreationClientRules.class,ModificationClientRules.class})
    @NotBlank(message = "is required", groups = {CreationClientRules.class})
    private String phoneNumber;
    String fileName = "default";

    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Reservation> reservations;

    @Override
    public String toString() {
        return "Client [email=" + email + ", password=" + password + ", role=" + role + ", name=" + name
                + ", firstName=" + firstName + ", phoneNumber=" + phoneNumber + "]";
    }


}
