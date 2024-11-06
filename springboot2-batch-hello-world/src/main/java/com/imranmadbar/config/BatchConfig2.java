package com.imranmadbar.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig2 {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public Step helloWorldStep1() {
		return stepBuilderFactory.get("helloWorldStep1").tasklet(new Tasklet() {

			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				System.out.println("Hello World Step One");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean
	public Step helloWorldStep2() {
		return stepBuilderFactory.get("helloWorldStep2").tasklet(new Tasklet() {

			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				System.out.println("Hello World Step Two");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Job helloWorldJob() {
		return jobBuilderFactory
				.get("helloWorldJob")
				 .incrementer(new RunIdIncrementer())
				.start(helloWorldStep1())
				.next(helloWorldStep2())
				.build();

	}
	
	
}