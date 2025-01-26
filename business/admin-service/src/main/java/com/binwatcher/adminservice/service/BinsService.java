package com.binwatcher.adminservice.service;

import com.binwatcher.adminservice.clients.BinsClient;
import com.binwatcher.adminservice.model.Bin;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BinsService {

    private final BinsClient binsClient;

    public List<Bin> getAll() {
        return binsClient.getAll().getBody();
    }
    public Bin create(Bin bin) {
        return binsClient.createBin(bin).getBody();
    }

    public Bin update(String id, Bin updatedBin) {
        return binsClient.updateBin(id, updatedBin).getBody();
    }

    public Bin updateFillLevel(String binId, Integer level) {
        return binsClient.updateFillLevel(binId, level).getBody();
    }

    public void delete(String binId) {
        binsClient.deleteBin(binId).getBody();
    }
}
