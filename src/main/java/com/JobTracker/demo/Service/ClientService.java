package com.JobTracker.demo.Service;

import com.JobTracker.demo.Entity.Client;
import com.JobTracker.demo.Repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Client found with id " + id));
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public void deleteById(Long id) {
        if(!clientRepository.existsById(id)) {
            throw new RuntimeException("No Client found with id " + id);
        }
        clientRepository.deleteById(id);
    }

    public Client update(Long id, Client client) {
        if(!clientRepository.existsById(id)) {
            throw new RuntimeException("No Client found with id " + id);
        }

        Client currentClient = findById(id);

        currentClient.setAddress(client.getAddress());
        currentClient.setFirstName(client.getFirstName());
        currentClient.setLastName(client.getLastName());
        currentClient.setPhoneNumber(client.getPhoneNumber());
        return clientRepository.save(currentClient);
    }





}
