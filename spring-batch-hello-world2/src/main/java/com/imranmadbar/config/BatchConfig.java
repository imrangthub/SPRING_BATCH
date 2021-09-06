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

import com.imranmadbar.tasks.MyTaskOne;
import com.imranmadbar.tasks.MyTaskTwo;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Step stepOne() {
		return stepBuilderFactory.get("stepOne")
				.tasklet(new MyTaskOne())
				.build();
	}

	@Bean
	public Step stepTwo() {
		return stepBuilderFactory.get("stepTwo")
				.tasklet(new MyTaskTwo())
				.build();
	}
	
	@Bean
	public Step stepThree() {
		return stepBuilderFactory.get("stepThree").tasklet(new Tasklet() {
			public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
				System.out.println("This is from Step Three (Final Step)");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}

	@Bean
	public Job simpleJob() {
		return jobBuilderFactory.get("simpleJob")
				.incrementer(new RunIdIncrementer())
				.start(stepOne())
				.next(stepTwo())
				.next(stepThree())
				.build();
	}
	
	
	
	
}