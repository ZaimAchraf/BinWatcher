package com.binwatcher.binservice.repository;

import com.binwatcher.binservice.entity.Bin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BinRepository extends MongoRepository<Bin, String> {
}
