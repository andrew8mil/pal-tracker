package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/time-entries", produces = { MediaType.APPLICATION_JSON_VALUE })
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;
    private DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(
            TimeEntryRepository timeEntryRepository,
            MeterRegistry meterRegistry
    ) {
        this.timeEntryRepository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {

        TimeEntry created = timeEntryRepository.create(timeEntryToCreate);
        if (created != null) {
            actionCounter.increment();
            timeEntrySummary.record(timeEntryRepository.list().size());

            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        TimeEntry found = timeEntryRepository.find(timeEntryId);
        if (found != null) {
            actionCounter.increment();

            return ResponseEntity.ok(found);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("")
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @PutMapping("/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry updated = timeEntryRepository.update(timeEntryId, expected);

        if (updated != null) {
            actionCounter.increment();

            return ResponseEntity.ok(updated);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId) {
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());

        timeEntryRepository.delete(timeEntryId);

        return ResponseEntity.noContent().build();
    }
}
