package com.amboucheba.etudeDeCasWeb.Models.Outputs;

import com.amboucheba.etudeDeCasWeb.Models.Entities.Collection;

import java.util.List;
import java.util.Objects;

public class Collections {

    private List<Collection> collections;

    public Collections(List<Collection> collections) {
        this.collections = collections;
    }

    public Collections() {
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collections that = (Collections) o;
        return collections.equals(that.collections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collections);
    }

}
