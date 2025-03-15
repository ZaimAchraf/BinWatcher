package com.binwatcher.sensorservice.service;

import com.binwatcher.apimodule.model.FillMessage;
import com.binwatcher.sensorservice.client.BinClient;
import com.binwatcher.sensorservice.model.Bin;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledService {

    private final BinClient binClient;
    private final KafkaProducerService kafkaProducerService;
    private List<Bin> listBins;

    public ScheduledService(BinClient binClient, KafkaProducerService kafkaProducerService) {
        this.binClient = binClient;
        this.kafkaProducerService = kafkaProducerService;
        initBins();
    }

    @Scheduled(cron = "0 0 */2 *  * ?")
    public void initBins() {
        listBins = binClient.getAll().getBody();
        System.out.println("got " + listBins.size() + " bins from bin-service");
    }

    @Scheduled(cron = "*/30 * * *  * ?")
    public void mockFill() {
        int index = (int) (Math.random() * listBins.size());
        Bin bin = listBins.get(index);
        kafkaProducerService.sendMockFill(new FillMessage(bin.getId(), (short) (Math.random() * 100)));
    }
}
