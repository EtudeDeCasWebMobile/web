package com.amboucheba.etudeDeCasWeb.Models.ToDelete.ModelLists;

import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;

import java.util.List;

public class SerieTemplorelleList {
    private List<SerieTemporelle> seriestemporelles;
    private int count;

    public List<SerieTemporelle> getSeriestemporelles() {
        return seriestemporelles;
    }

    public void setSeriestemporelles(List<SerieTemporelle> seriestemporelles) {
        this.seriestemporelles = seriestemporelles;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public SerieTemplorelleList(List<SerieTemporelle> seriestemporelles) {
        this.seriestemporelles = seriestemporelles;
        this.count = seriestemporelles.size();
    }

    public SerieTemplorelleList() {
    }
}
