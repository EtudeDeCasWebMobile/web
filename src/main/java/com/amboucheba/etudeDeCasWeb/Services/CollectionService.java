package com.amboucheba.etudeDeCasWeb.Services;

import com.amboucheba.etudeDeCasWeb.Exceptions.DuplicateResourceException;
import com.amboucheba.etudeDeCasWeb.Exceptions.ForbiddenActionException;
import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Collection;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.CollectionInput;
import com.amboucheba.etudeDeCasWeb.Repositories.CollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CollectionService {

    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    UserService userService;

    public Collection createCollection(String tag, long userId){
        // this handles user not found
        User user = userService.find(userId);

        Optional<Collection> tmp = collectionRepository.findByTagAndOwnerId(tag, userId);
        if (tmp.isPresent()){
            throw new DuplicateResourceException("'Collection' with tag " + tag + " already exists for user " + user.getEmail());
        }

        Collection collection = new Collection(tag, user);
        return collectionRepository.save(collection);
    }

    public List<Collection> listCollectionsByUser(long userId) {

        // this handles user not found
        User user = userService.find(userId);
        return collectionRepository.findByOwnerId(userId);
    }

    //TODO: add verify if collection is shared with user
    public Collection find(long collectionId, long userId){

        Optional<Collection> tmp = collectionRepository.findById(collectionId);
        if (!tmp.isPresent()){
            throw new NotFoundException("Collection with id " + collectionId + " not found.");
        }

        Collection collection = tmp.get();
        // collection exists --> verify that user has access to it
        // either user is the owner of the collection
        // or he has been granted access to it through sharing

        if (collection.getOwner().getId().equals(userId)){
            // user is owner --> return the collection
            return collection;
        }

        // user is not owner --> check if the collection is shared with him
        //...
        return null;
    }

    //TODO: add verify if collection is shared with user (with write access)
    public Collection updateCollection(CollectionInput newCollection, long collectionId, Long userId) {

        String tag = newCollection.getTag();

        Optional<Collection> tmp = collectionRepository.findById(collectionId);
        if (!tmp.isPresent()){
            throw new NotFoundException("Collection with id " + collectionId + " not found.");
        }

        // there already another collection with this tag
        Optional<Collection> tmp2 = collectionRepository.findByTagAndOwnerId(tag, userId);
        if (tmp2.isPresent() && tmp2.get().getId() != collectionId){
            throw new DuplicateResourceException("'Collection' with tag " + tag + " already exists for user with id " + userId);
        }

        Collection collection = tmp.get();
        // collection exists --> verify that user has "write" access to it
        // either user is the owner of the collection
        // or he has been granted "write" access to it through sharing

        if (collection.getOwner().getId().equals(userId)){
            // user is owner --> update the collection
            collection.setTag(newCollection.getTag());
            return collectionRepository.save(collection);
        }

        // user is not owner --> check if the collection is shared with him (with write access)
        //...
        return null;

    }

    // Assumes only owner of a collection can delete it
    public void deleteCollection(long collectionId, Long userId) {

        Optional<Collection> tmp = collectionRepository.findById(collectionId);
        if (!tmp.isPresent()){
            throw new NotFoundException("Collection with id " + collectionId + " not found.");
        }

        Collection collection = tmp.get();
        // collection exists --> verify that user is its owner
        // if user is not the owner -->  access denied

        if (!collection.getOwner().getId().equals(userId)){
            throw new ForbiddenActionException("Permission denied: cannot delete another user's collection");
        }

        collectionRepository.deleteById(collectionId);
    }
}
