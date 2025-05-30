package io.github.lucasferreira.correios.service;

import io.github.lucasferreira.correios.CorreiosApplication;
import io.github.lucasferreira.correios.exception.NoContentException;
import io.github.lucasferreira.correios.exception.NotReadyException;
import io.github.lucasferreira.correios.model.Address;
import io.github.lucasferreira.correios.model.AddressStatus;
import io.github.lucasferreira.correios.model.Status;
import io.github.lucasferreira.correios.repository.AddressRepository;
import io.github.lucasferreira.correios.repository.AddressStatusRepository;
import io.github.lucasferreira.correios.repository.SetupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CorreiosService {

   private static Logger logger = LoggerFactory.getLogger(CorreiosService.class);

    @Autowired
    private AddressStatusRepository addressStatusRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private SetupRepository setupRepository;

//    Tirar os comentários para realizar o teste
//    @Value("${setup.on.startup}")
//    private boolean setupOnStartup;

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

    @EventListener(ApplicationStartedEvent.class)
    protected void setupOnStartup() {
//       Tirar os comentários para realizar o teste
//        if(!setupOnStartup){
//            return;
//        }
        try {
            setUp();
        } catch (Exception e) {
            CorreiosApplication.close(999);
            logger.error(".setuoOnStartup - Exception",e);
        }
    }

    public void setUp() throws Exception {
        logger.info("-----");
        logger.info("-----");
        logger.info("----- SETUP RUNNING");
        logger.info("-----");
        logger.info("-----");
        if (getStatus().equals(Status.NEED_SETUP)) {
            saveStatus(Status.SETUP_RUNNING);
            try {
                addressRepository.saveAll(setupRepository.getFromOrigin());
            } catch (Exception e) {
                saveStatus(Status.NEED_SETUP);
                throw e;
            }
            saveStatus(Status.READY);
        }
        logger.info("-----");
        logger.info("-----");
        logger.info("----- SERVICE RUNNING");
        logger.info("-----");
        logger.info("-----");

    }

}
