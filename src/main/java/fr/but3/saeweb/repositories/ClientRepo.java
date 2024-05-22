package fr.but3.saeweb.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.but3.saeweb.entities.Client;

@Repository
public interface ClientRepo extends CrudRepository<Client,Long>{

    Client findByEmail(String email);
    Client findByEmailAndPassword(String email,String password);
    Client findByPhoneNumber(String phoneNumber);
  
}