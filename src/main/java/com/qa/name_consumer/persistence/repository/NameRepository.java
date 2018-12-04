package com.qa.name_consumer.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.qa.name_crud_api.persistence.domain.BabyNamePOJO;

@Repository
public interface NameRepository extends MongoRepository<BabyNamePOJO, String> {
}
