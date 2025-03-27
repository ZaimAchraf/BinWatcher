package com.binwatcher.sensorservice.service;

import com.binwatcher.apimodule.model.FillMessage;
import com.binwatcher.sensorservice.client.BinClient;
import com.binwatcher.sensorservice.config.ScheduledConfigProperties;
import com.binwatcher.sensorservice.model.Bin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduledService {

    private final BinClient binClient;
    private final KafkaProducerService kafkaProducerService;
    private final ScheduledConfigProperties scheduledConfigProperties;
    private List<Bin> listBins;
    private final Logger LOG = LoggerFactory.getLogger(ScheduledService.class);

    public ScheduledService(BinClient binClient,
                            KafkaProducerService kafkaProducerService,
                            ScheduledConfigProperties scheduledConfigProperties ) {
        this.binClient = binClient;
        this.kafkaProducerService = kafkaProducerService;
        this.scheduledConfigProperties = scheduledConfigProperties;
        initBins();
    }

    @Scheduled(cron = "#{@scheduledConfigProperties.getInitBinsCron()}")
    public void initBins() {
        try {
            listBins = binClient.getAll().getBody();
            if (listBins.isEmpty()) {
                LOG.warn("No bins were fetched from bin-service.");
            } else {
                LOG.debug("{} bins fetched from bin-service.", listBins.size());
            }
        } catch (Exception e) {
            LOG.error("Error fetching bins from bin-service", e);
        }
    }

    @Scheduled(cron = "#{@scheduledConfigProperties.getMockFillCron()}") // Every 2 minutes
    public void mockFill() {
        if (listBins != null && !listBins.isEmpty()) {
            int index = (int) (Math.random() * listBins.size());
            Bin bin = listBins.get(index);
            short fillLevel = (short) (Math.random() * 100);
            kafkaProducerService.sendMockFill(new FillMessage(bin.getId(), fillLevel));
            LOG.info("Sent mock fill for bin with ID: {}, fill level: {}", bin.getId(), fillLevel);
        } else {
            LOG.warn("No bins available to process mock fill.");
        }
    }
}
