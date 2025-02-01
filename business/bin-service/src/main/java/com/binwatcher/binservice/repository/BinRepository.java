package com.binwatcher.binservice.repository;

import com.binwatcher.binservice.entity.Bin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BinRepository extends MongoRepository<Bin, String> {
}
