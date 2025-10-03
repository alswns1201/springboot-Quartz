package com.scheduler.Quartz.timeservices;

import com.scheduler.Quartz.info.TimerInfo;
import com.scheduler.Quartz.util.TimerUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerService.class);
    private final Scheduler scheduler;

    /**
     * 실제 서비스에서 Utils의 함수를 이용해서  JobDetail 적용 및 트리거 적용
     * @param jobClass
     * @param info
     * @param <T>
     */
    public <T extends Job> void schedule(final Class<T> jobClass, final TimerInfo info) {
        final JobDetail jobDetail = TimerUtils.buildJobDetail(jobClass, info);
        final Trigger trigger = TimerUtils.buildTrigger(jobClass, info);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


    /**
     * 현재 등록된 모든 Job 정보 조회
     * @return 실행중인 TimerInfo 리스트
     */
    public List<TimerInfo> getAllRunningTimers() {
        try {

            // 스케줄러에서 모든 Job에 적용된 key를 가져와서 Job 정보를 조회한다.
            return scheduler.getJobKeys(GroupMatcher.anyGroup())
                    .stream()
                    .map(jobKey -> {
                        try {
                            final JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                            return (TimerInfo) jobDetail.getJobDataMap().get(jobKey.getName());
                        } catch (final SchedulerException e) {
                            LOGGER.error(e.getMessage(), e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)  // null은 필터처리 나머지 리스트화.
                    .collect(Collectors.toList());
        } catch (final SchedulerException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public TimerInfo getRunningTimer(final String timerId) {
        try {
            final JobDetail jobDetail = scheduler.getJobDetail(new JobKey(timerId));
            if (jobDetail == null) {
                LOGGER.error("Failed to find timer with ID '{}'", timerId);
                return null;
            }

            return (TimerInfo) jobDetail.getJobDataMap().get(timerId);
        } catch (final SchedulerException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public void updateTimer(final String timerId, final TimerInfo info) {
        try {
            final JobDetail jobDetail = scheduler.getJobDetail(new JobKey(timerId));
            if (jobDetail == null) {
                LOGGER.error("Failed to find timer with ID '{}'", timerId);
                return;
            }

            jobDetail.getJobDataMap().put(timerId, info);

            scheduler.addJob(jobDetail, true, true);
        } catch (final SchedulerException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


    public Boolean deleteTimer(final String timerId) {
        try {
            return scheduler.deleteJob(new JobKey(timerId));
        } catch (SchedulerException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }


    @PostConstruct
    public void init(){
        try {
            scheduler.start();
            scheduler.getListenerManager().addTriggerListener(new SimpleTriggerListener(this));
        }catch (SchedulerException e){
            LOGGER.error(e.getMessage());
        }

    }

    @PreDestroy
    public void preDestroy(){
        try {
            scheduler.shutdown();
        }catch (SchedulerException e){

        }
    }
}
