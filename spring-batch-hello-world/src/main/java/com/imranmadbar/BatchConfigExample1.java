package com.imranmadbar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import java.util.Arrays;
import java.util.List;

@Configuration
public class BatchConfigExample1 {

	
    final JobRepository jobRepository;
    private static final int BATCH_SIZE = 3;
    final PlatformTransactionManager batchTransactionManager;
    public static final Logger logger = LoggerFactory.getLogger(BatchConfigExample1.class);
    

    public BatchConfigExample1(JobRepository jobRepository, PlatformTransactionManager batchTransactionManager) {
        this.jobRepository = jobRepository;
        this.batchTransactionManager = batchTransactionManager;
    }

    
    

    @Bean
    public Job secondJob() {
        return new JobBuilder("MySecondJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(myJobInitStep())
                .next(myJobMainStep())
                .next(myJobFinalStep())
                .build();
    }



    @Bean
    public Step myJobInitStep () {
        return new StepBuilder("myJobInitStep", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    logger.info("================================Init step done====================================");
                    return RepeatStatus.FINISHED;
                }, batchTransactionManager).build();
    }
    
    @Bean
    public Step myJobMainStep () {
        return new StepBuilder("myJobMainStep", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    logger.info("================================Main step done======================================");
                    return RepeatStatus.FINISHED;
                }, batchTransactionManager).build();
    }
    
    
    @Bean
    public Step myJobFinalStep () {
        return new StepBuilder("myJobFinalStep", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    logger.info("================================All Done=============================================");
                    return RepeatStatus.FINISHED;
                }, batchTransactionManager).build();
    }



}