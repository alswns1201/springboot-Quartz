package com.scheduler.Quartz.util;

import com.scheduler.Quartz.info.TimerInfo;
import org.quartz.*;

import java.util.Date;

public class TimerUtils {

    private TimerUtils() {}

    public static JobDetail buildJobDetail(final Class jobClass, final TimerInfo info) {
        final JobDataMap jobDataMap = new JobDataMap();
        // JobDataMap에 class SimpleName 을 key로 잡고 TimerInfo 내용을 담는다.
        jobDataMap.put(jobClass.getSimpleName(), info);

        return JobBuilder
                .newJob(jobClass)
                .withIdentity(jobClass.getSimpleName())
                .setJobData(jobDataMap)
                .build();
    }

    public static Trigger buildTrigger(final Class jobClass, final TimerInfo info) {

        // 타이머 시간 정보 설정.
        SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(info.getRepeatIntervalMs());

        // 반복 수행 여부 설정 및 횟수 설정
        if (info.isRunForever()) {
            builder = builder.repeatForever();
        } else {
            builder = builder.withRepeatCount(info.getTotalFireCount() - 1);
        }

        // Trigger 적용.
        return TriggerBuilder
                .newTrigger()
                .withIdentity(jobClass.getSimpleName())
                .withSchedule(builder)
                .startAt(new Date(System.currentTimeMillis() + info.getInitialOffsetMs()))
                .build();
    }
}
