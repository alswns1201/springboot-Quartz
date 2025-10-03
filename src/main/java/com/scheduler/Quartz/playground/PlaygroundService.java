package com.scheduler.Quartz.playground;

import com.scheduler.Quartz.info.TimerInfo;
import com.scheduler.Quartz.jobs.HelloJobs;
import com.scheduler.Quartz.timeservices.SchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaygroundService {

    private final SchedulerService scheduler;


    public void runHelloJob() {
        final TimerInfo info = new TimerInfo();
        info.setTotalFireCount(5);
        info.setRemainingFireCount(info.getTotalFireCount());
        info.setRepeatIntervalMs(5000);
        info.setInitialOffsetMs(1000);
        info.setCallbackData("My callback data");

        scheduler.schedule(HelloJobs.class, info);
    }

    public Boolean deleteTimer(final String timerId) {
        return scheduler.deleteTimer(timerId);
    }


    public List<TimerInfo> getAllRunningTimers() {
        return scheduler.getAllRunningTimers();
    }

    public TimerInfo getRunningTimers(String timerId){
        return scheduler.getRunningTimer(timerId);
    }

}
