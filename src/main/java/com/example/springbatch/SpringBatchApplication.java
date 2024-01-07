package com.example.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication
public class SpringBatchApplication {

	@Bean
	public Tasklet testTasklet(){
		return ((contribution, chunkContext) -> {
			System.out.println("Execute passStep");;
			return RepeatStatus.FINISHED;
		});
	}

	@Bean
	public Step passStep(JobRepository jobRepository, Tasklet testTasklet, PlatformTransactionManager platformTransactionManager){
		return new StepBuilder("passStep", jobRepository)
				.tasklet(testTasklet, platformTransactionManager)
				.build();
	}

	@Bean
	public Job passJob(JobRepository jobRepository, Step passStep) {
		return new JobBuilder("passJob", jobRepository)
				.start(passStep)
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

}
