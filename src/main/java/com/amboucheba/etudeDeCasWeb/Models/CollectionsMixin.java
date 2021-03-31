package com.amboucheba.etudeDeCasWeb.Models;

import java.util.List;
import java.util.Objects;

// FOr testing purpose: avoid Managed/BackReference in Collection/location list
public class CollectionsMixin {

    private List<CollectionsMixin> collections;


    public CollectionsMixin(List<CollectionsMixin> collections) {
        this.collections = collections;
    }

    public CollectionsMixin() {
    }

    public List<CollectionsMixin> getCollections() {
        return collections;
    }

    public void setCollections(List<CollectionsMixin> collections) {
        this.collections = collections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectionsMixin that = (CollectionsMixin) o;
        return Objects.equals(collections, that.collections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collections);
    }
}
