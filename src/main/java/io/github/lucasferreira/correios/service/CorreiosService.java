package io.github.lucasferreira.correios.service;

import io.github.lucasferreira.correios.exception.NoContentException;
import io.github.lucasferreira.correios.exception.NotReadyException;
import io.github.lucasferreira.correios.model.Address;
import io.github.lucasferreira.correios.model.AddressStatus;
import io.github.lucasferreira.correios.model.Status;
import io.github.lucasferreira.correios.repository.AddressRepository;
import io.github.lucasferreira.correios.repository.AddressStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CorreiosService {

    @Autowired
    private AddressStatusRepository addressStatusRepository;
    @Autowired
    private AddressRepository addressRepository;

    public Status getStatus() {
        return addressStatusRepository.findById(AddressStatus.DEFAULT_ID).
                orElse(AddressStatus.builder().status(Status.NEED_SETUP).build())
                .getStatus();
    }

    public Address getAddressByZipcode(String zipcode) {
        if (!getStatus().equals(Status.READY)) {
            throw new NotReadyException("Não está pronta ainda!");
        }
        return addressRepository.findById(zipcode).
                orElseThrow(() -> new NoContentException("Endereço"));
    }

    private void saveStatus(Status status) {
        addressStatusRepository.save(AddressStatus
                .builder().
                id(AddressStatus.DEFAULT_ID).
                status(status).build());
    }


    public void setUp() {   
    }

}
