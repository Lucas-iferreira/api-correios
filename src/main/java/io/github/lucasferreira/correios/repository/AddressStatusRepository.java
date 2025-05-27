package io.github.lucasferreira.correios.repository;

import io.github.lucasferreira.correios.model.AddressStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressStatusRepository extends JpaRepository<AddressStatus, Integer> {
}
