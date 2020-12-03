package com.amboucheba.seriesTemporellesTpWeb.models.ModelLists;

import com.amboucheba.seriesTemporellesTpWeb.models.Partage;

import java.util.List;

public class PartageList {


    private List<Partage> partages;
    private int count;

    public PartageList(List<Partage> partages) {
        this.partages = partages;
        this.count = partages.size();
    }

    public PartageList() {
    }

    public List<Partage> getEvents() {
        return partages;
    }

    public void setEvents(List<Partage> events) {
        this.partages = events;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
