/*package fr.but3.saeweb.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.but3.saeweb.entities.Client;
import fr.but3.saeweb.repositories.ClientRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ClientRepo clientRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Tentative de connexion de l'utilisateur: " + email);
        Client user = clientRepo.findByEmail(email);

        if (user != null) {
            System.out.println("Utilisateur trouvé: " + email);
            return user;
        } else {
            System.out.println("Utilisateur non trouvé avec le nom d'utilisateur: " + email);
            throw new UsernameNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur: " + email);
        }
    }

    public String getRole(String email) {
        Client user = clientRepo.findByEmail(email);

        if (user != null) {
            return user.getRole().toString();
        } else {
            return null;
        }
    }
}
*/