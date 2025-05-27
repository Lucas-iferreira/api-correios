package io.github.lucasferreira.correios.repository;

import io.github.lucasferreira.correios.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, String> {
}
