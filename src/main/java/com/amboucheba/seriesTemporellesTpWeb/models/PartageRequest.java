package com.amboucheba.seriesTemporellesTpWeb.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PartageRequest {

    @Id
    private long id;

    private long userId;
    private long serieTemporelleId;
    private String type;

    public PartageRequest() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getSerieTemporelleId() {
        return serieTemporelleId;
    }

    public void setSerieTemporelleId(long serieTemporelleId) {
        this.serieTemporelleId = serieTemporelleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
