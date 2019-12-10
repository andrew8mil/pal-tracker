package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private List<TimeEntry> entries;
    private long idCounter = 1L;

    public InMemoryTimeEntryRepository() {
        entries = new ArrayList<>();
    }

    public TimeEntry create(TimeEntry timeEntry) {
        TimeEntry timeEntrySaved = new TimeEntry(idCounter++, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        entries.add(timeEntrySaved);
        return timeEntrySaved;
    }

    public TimeEntry find(long id) {
        if (entries.isEmpty()) return null;
        for (TimeEntry e : entries) {
            if (e.getId() == id)
                return e;
        }
        return null;
    }


    public TimeEntry update(long id, TimeEntry timeEntry) {
        if (entries.isEmpty()) return null;
        for (TimeEntry e : entries) {
            if (e.getId() == id)
                return updateEntry(e, timeEntry);
        }
        return null;
    }

    private TimeEntry updateEntry(TimeEntry e, TimeEntry timeEntry) {
        e.setProjectId(timeEntry.getProjectId());
        e.setDate(timeEntry.getDate());
        e.setHours(timeEntry.getHours());
        e.setUserId(timeEntry.getUserId());
        return e;
    }

    public void delete(long id) {
        TimeEntry d = null;
        for (TimeEntry e : entries) {
            if (e.getId() == id) {
                d = e;
                break;
            }
        }
        if (d != null)
            entries.remove(d);
    }

    public List<TimeEntry> list() {
        return entries;
    }
}
