package ru.alfabank.skillbox.examples.moduletests.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JsonRepository extends JpaRepository<JsonEntity, Long> {

    Optional<JsonEntity> findById(Long id);
}
