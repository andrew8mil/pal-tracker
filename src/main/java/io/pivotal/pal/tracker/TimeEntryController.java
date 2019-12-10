package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/time-entries", produces = { MediaType.APPLICATION_JSON_VALUE })
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry created = timeEntryRepository.create(timeEntryToCreate);
        if (created != null)
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        TimeEntry found = timeEntryRepository.find(timeEntryId);
        if (found != null)
            return ResponseEntity.ok(found);

        return ResponseEntity.notFound().build();
    }

    @GetMapping("")
    public ResponseEntity<List<TimeEntry>> list() {
        return ResponseEntity.ok(timeEntryRepository.list());
    }

    @PutMapping("/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry updated = timeEntryRepository.update(timeEntryId, expected);

        if (updated != null)
            return ResponseEntity.ok(updated);

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);

        return ResponseEntity.noContent().build();
    }
}
