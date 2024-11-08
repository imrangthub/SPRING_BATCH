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
public class BatchConfigExample2 {

    final JobRepository jobRepository;
    final PlatformTransactionManager batchTransactionManager;
    public static final Logger logger = LoggerFactory.getLogger(BatchConfigExample2.class);
    
    private static final int BATCH_SIZE = 3;

    public BatchConfigExample2(JobRepository jobRepository, PlatformTransactionManager batchTransactionManager) {
        this.jobRepository = jobRepository;
        this.batchTransactionManager = batchTransactionManager;
    }

    /**
     * Job which contains multiple steps
     */
    @Bean
    public Job firstJob() {
        return new JobBuilder("MyFirstJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(chunkStep())
                .next(taskFinishingStep())
                .build();
    }

    @Bean
    public Step chunkStep() {
        return new StepBuilder("MyFirstStep", jobRepository)
                .<String, String>chunk(BATCH_SIZE, batchTransactionManager)
                .reader(reader())
                .writer(writer())
                .build();
    }
    

    @Bean
    public Step taskFinishingStep () {
        return new StepBuilder("MyTaskFinishingStep", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    logger.info("============================All task done, Release All Resource==============================");
                    return RepeatStatus.FINISHED;
                }, batchTransactionManager).build();
    }


    @Bean
    public ItemReader<String> reader() {
        List<String> data = Arrays.asList("Byte", "Code", "Data", "Disk", "File", "Input", "Loop", "Logic", "Mode", "Node");
        return new ListItemReader<>(data);
    }

    @Bean
    public ItemWriter<String> writer() {
        return items -> {
            for (String item : items) {
                logger.info("MyItemWriter: {}", item);
            }
            logger.info("------------ BATCH_SIZE: 3  ------------");
        };
    }

}