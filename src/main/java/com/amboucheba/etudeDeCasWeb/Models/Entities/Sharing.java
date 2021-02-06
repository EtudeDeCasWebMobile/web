package com.amboucheba.etudeDeCasWeb.Models.Entities;

import javax.persistence.*;

@Entity
@Table(name = "sharings")
public class Sharing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shared_with_id", nullable = false)
    private User sharedWith;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;

    @Column(name = "access_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccessType accessType;


    public Sharing(Long id, User sharedWith, Collection collection, AccessType accessType) {
        this.id = id;
        this.sharedWith = sharedWith;
        this.collection = collection;
        this.accessType = accessType;
    }

    public Sharing() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSharedWith() {
        return sharedWith;
    }

    public void setSharedWith(User sharedWith) {
        this.sharedWith = sharedWith;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }
}
