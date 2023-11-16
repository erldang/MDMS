package com.example.backend.batch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.JobParameter;
@Slf4j
@Component
public class BatchScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private BatchConfig batchConfig;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    Job simpleJob1;
    @Scheduled(cron = "0 */5 * * * *")
    public void runExampleJob() {


        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time" , System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(simpleJob1 , jobParameters);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
   // 출처: https://fvor001.tistory.com/110 [Dev Log:티스토리]
}