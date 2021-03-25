package com.amboucheba.etudeDeCasWeb.Services;

import com.amboucheba.etudeDeCasWeb.Exceptions.DuplicateResourceException;
import com.amboucheba.etudeDeCasWeb.Exceptions.ForbiddenActionException;
import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.CollectionShareDetails;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Collection;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.CollectionInput;
import com.amboucheba.etudeDeCasWeb.Models.Outputs.CollectionShare;
import com.amboucheba.etudeDeCasWeb.Repositories.CollectionRepository;
import com.amboucheba.etudeDeCasWeb.Util.JwtUtil;
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

    @Autowired
    JwtUtil jwtUtil;

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

    protected Optional<Collection> findByByTagAndOwner(String tag, User owner){

        return collectionRepository.findByTagAndOwnerId(tag, owner.getId());
    }

    //TODO: add verify if collection is shared with user
    public Collection find(long collectionId, String token, long userId){

        Optional<Collection> tmp = collectionRepository.findById(collectionId);
        if (tmp.isEmpty()){
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

        if (token != null) {
            long allowedCollectionId = jwtUtil.extractCollectionId(token);
            if (collectionId == allowedCollectionId){
                return collection;
            }
        }

        throw new ForbiddenActionException("Permission denied: cannot access another user's collection(missing/invalid token)");
    }

    //TODO: add verify if collection is shared with user (with write access)
    public Collection updateCollection(CollectionInput newCollection, long collectionId, Long userId) {

        Optional<Collection> tmp = collectionRepository.findById(collectionId);
        if (tmp.isEmpty()){
            throw new NotFoundException("Collection with id " + collectionId + " not found.");
        }

        // there already another collection with this tag
        String tag = newCollection.getTag();
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
        if (tmp.isEmpty()){
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

    public String generateShareableToken(long collectionId, Long userId) {

        Optional<Collection> tmp = collectionRepository.findById(collectionId);
        if (tmp.isEmpty()){
            throw new NotFoundException("Collection with id " + collectionId + " not found.");
        }
        Collection collection = tmp.get();
        if (!collection.getOwner().getId().equals(userId)){
            throw new ForbiddenActionException("Permission denied: cannot request to share another user's collection");
        }

        CollectionShareDetails collectionShare = new CollectionShareDetails(collectionId);
        return jwtUtil.generateToken(collectionShare);
    }
}
