package com.scheduler.Quartz.jobs;

import com.scheduler.Quartz.info.TimerInfo;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HelloJobs implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloJobs.class);
    //보통 클래스명 기준으로 로거를 만들어서, 로그 출력 시 어떤 클래스에서 찍힌 로그인지 자동으로 표시됨.
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        TimerInfo timerInfo = (TimerInfo) jobDataMap.get(HelloJobs.class.getSimpleName());
        LOGGER.info("helloJobs : {}",timerInfo.getRemainingFireCount());
    }
}
