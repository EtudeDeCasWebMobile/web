package com.amboucheba.etudeDeCasWeb.Models.ToDelete.ModelLists;

import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;

import java.util.List;

public class PartagesByUser {

    private Long userId;
    private List<SerieTemporelle> serieTemporelles;

    public PartagesByUser(Long userId, List<SerieTemporelle> serieTemporelles) {
        this.userId = userId;
        this.serieTemporelles = serieTemporelles;
    }

    public PartagesByUser() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<SerieTemporelle> getSerieTemporelles() {
        return serieTemporelles;
    }

    public void setSerieTemporelles(List<SerieTemporelle> serieTemporelles) {
        this.serieTemporelles = serieTemporelles;
    }
}
