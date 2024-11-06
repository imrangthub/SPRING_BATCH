package com.imranmadbar;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduledBatchJob {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job secondJob;

    @Scheduled(fixedRate = 6000) // Run every 1 seconds
    public void runBatchJob() {
        try {
        	System.out.println("Batch job started at############: " + System.currentTimeMillis());
            JobParameters params = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
            jobLauncher.run(secondJob, params);
            System.out.println("Batch job done at###############: " + System.currentTimeMillis());
        } catch (Exception e) {
            System.err.println("Batch job failed to start: " + e.getMessage());
        }
    }
}
