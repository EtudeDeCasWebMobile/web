package com.amboucheba.etudeDeCasWeb.Models;

import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import java.util.Objects;

// FOr testing purpose: avoid Managed/BackReference in Collection/location list
public class CollectionMixin {

    private Long id;
    private String tag;
    private User owner;

    public CollectionMixin(Long id, String tag, User owner) {
        this.id = id;
        this.tag = tag;
        this.owner = owner;
    }

    public CollectionMixin() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectionMixin that = (CollectionMixin) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(tag, that.tag) &&
                Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tag, owner);
    }
}
