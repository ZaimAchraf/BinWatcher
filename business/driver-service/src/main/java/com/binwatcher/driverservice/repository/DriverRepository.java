package com.binwatcher.driverservice.repository;

import com.binwatcher.driverservice.entity.Driver;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends MongoRepository<Driver,String> {
}
