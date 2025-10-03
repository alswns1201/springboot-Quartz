package com.scheduler.Quartz.playground;

import com.scheduler.Quartz.info.TimerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timer")
public class PlaygroundController {

    private final PlaygroundService service;

    @Autowired
    public PlaygroundController(PlaygroundService service) {
        this.service = service;
    }

    @PostMapping("/runHelloJob")
    public void runHelloJob() {
        service.runHelloJob();
    }

    @GetMapping
    public List<TimerInfo> getAllRunningTimers() {
        return service.getAllRunningTimers();
    }

    @GetMapping("/{timerId}")
    public TimerInfo getRunningTimer(@PathVariable String timerId) {
        return service.getRunningTimers(timerId);
    }

    @DeleteMapping("/{timerId}")
    public Boolean deleteTimer(@PathVariable String timerId) {
        return service.deleteTimer(timerId);
    }
}