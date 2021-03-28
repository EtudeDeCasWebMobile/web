package com.amboucheba.etudeDeCasWeb.Services;

import com.amboucheba.etudeDeCasWeb.Exceptions.DuplicateResourceException;
import com.amboucheba.etudeDeCasWeb.Exceptions.ForbiddenActionException;
import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Collection;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Position;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.LocationInput;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.PositionInput;
import com.amboucheba.etudeDeCasWeb.Models.LocationShareDetails;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Location;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Repositories.CollectionRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.LocationRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.PositionRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
import com.amboucheba.etudeDeCasWeb.Util.JwtUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    CollectionService collectionService;

    @Autowired
    CollectionRepository collectionRepository;

    @Autowired
    PositionRepository positionRepository;
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;
    
    @Autowired
    JwtUtil jwtUtil;

    public Location createLocation(LocationInput locationInput, long userId){

        User user = userService.find(userId);

        Optional<Location> tmp = locationRepository.findByTitleAndOwnerId(locationInput.getTitle(), userId);
        if (tmp.isPresent()){
            throw new DuplicateResourceException("'Location' with title " + locationInput.getTitle() + " already exists for user " + user.getEmail());
        }

        Location location = new Location();
        location.setTitle(locationInput.getTitle());
        location.setDescription(locationInput.getDescription());
        location.setOwner(user);
        location.setLatitude(locationInput.getLatitude());
        location.setLongitude(locationInput.getLongitude());
        location.setImage(locationInput.getImage());


        List<Collection> collections = new ArrayList<>();
        for (String tag: locationInput.getTags()) {
            Optional<Collection> tmp_c = collectionService.findByByTagAndOwner(tag, user);
            if (tmp_c.isEmpty()){
                collections.add(collectionService.createCollection(tag, userId));
            }
            else {
                collections.add(tmp_c.get());
            }
        }

        location.setCollections(collections);
        location = locationRepository.save(location);

        //update collections --> add location to them --> necessary

        for (Collection c : collections) {
            c.getLocations().add(location);
            collectionRepository.save(c);
        }

        return location;
    }

    public List<Location> listLocationsByUser(long userId) {

        // this verifies if user exists
        User user = userService.find(userId);
        return locationRepository.findByOwnerId(userId);
    }

    public String generateShareableToken(long userId, long initiatorId) {

        if (userId != initiatorId){
            throw new ForbiddenActionException("Only concerned user can share his position");
        }

    	User user = userService.find(userId);

    	Position current = user.getPosition();
    	if (current == null){
            throw new NotFoundException("User " + userId + " 's position is not set.");
        }
        LocationShareDetails locationShare = new LocationShareDetails(current.getId());
        return jwtUtil.generateToken(locationShare);

//        Optional<Location> tmp = locationRepository.findByOwner(user);
//        if (tmp.isEmpty()){
//            throw new NotFoundException("CurrentLocation of user " + userId + " not found.");
//        }
//        Location location = tmp.get();
//        if (!location.getOwner().getId().equals(userId)){
//            throw new ForbiddenActionException("Permission denied: cannot request to share another user's collection");
//        }

//        LocationShareDetails locationShare = new LocationShareDetails(location.getId());
//        return jwtUtil.generateToken(locationShare);
    }

    public Position find(long userId, String token, long initiatorId){

    	User user = userService.find(userId);
    	Position current = user.getPosition();

        if (current == null){
            throw new NotFoundException("User " + userId + " 's position is not set.");
        }

        if (userId == initiatorId){
            return current;
        }

        if (token != null ){
            // invalid token --> throws exception --> handled in ApiExceptionHandler
            long allowedPositionId = jwtUtil.extractPositionId(token);
            if (current.getId() == allowedPositionId){
                return current;
            }
        }

        throw new ForbiddenActionException("Permission denied: cannot access another user's position(missing/invalid token)");
    }

    public void deleteLocation(long locationId, Long userId) {

        Optional<Location> tmp = locationRepository.findById(locationId);
        if (tmp.isEmpty()){
            throw new NotFoundException("location with id " + locationId + " not found.");
        }

        Location location = tmp.get();

        if (!location.getOwner().getId().equals(userId)){
            throw new ForbiddenActionException("Permission denied: cannot delete another user's location");
        }

        // first remove location from other collections
        for (Collection c: location.getCollections()) {
            c.getLocations().remove(location);
            collectionRepository.save(c);
        }

        locationRepository.deleteById(locationId);
    }

    public Location updateLocation(LocationInput newLocation, long locationId, Long userId) {

        String title = newLocation.getTitle();

        Optional<Location> tmp = locationRepository.findById(locationId);
        if (tmp.isEmpty()){
            throw new NotFoundException("Location with id " + locationId + " not found.");
        }

        Optional<Location> tmp2 = locationRepository.findByTitleAndOwnerId(title, userId);
        if (tmp2.isPresent() && tmp2.get().getId() != locationId){
            throw new DuplicateResourceException("'location' with title " + title + " already exists for user with id " + userId);
        }

        Location location = tmp.get();

        if (!location.getOwner().getId().equals(userId)){
            throw new ForbiddenActionException("Permission denied: cannot update another user's location");
        }

        // user is owner --> update the location
        location.setTitle(newLocation.getTitle());
        location.setDescription(newLocation.getDescription());
        if (newLocation.getLatitude() != null) location.setLatitude(newLocation.getLatitude());
        if (newLocation.getLongitude() != null) location.setLongitude(newLocation.getLongitude());
        if (newLocation.getImage() != null) location.setImage(newLocation.getImage());
        return locationRepository.save(location);
    }

    public Position updateCurrentLocation(long userId, PositionInput newLocation, long initiatorId) {

        if (userId != initiatorId){
            throw new ForbiddenActionException("Only concerned user can update his position");
        }

        // handles not found
    	User user = userService.find(userId);
    	Position current = user.getPosition();

    	// has no current position
    	if (current == null){
            current = new Position(newLocation.getLatitude(), newLocation.getLongitude());
            current = positionRepository.save(current);
            user.setPosition(current);
            userRepository.save(user);
        }
    	else{
            current.setLatitude(newLocation.getLatitude());
            current.setLongitude(newLocation.getLongitude());
            positionRepository.save(current);
        }

    	return current;
    }

    public Collection addToCollection(long locationId, String tag, Long userId) {

        Optional<Location> tmp = locationRepository.findById(locationId);
        if (tmp.isEmpty()){
            throw new NotFoundException("Location with id " + locationId + " not found.");
        }

        Location location = tmp.get();
        if (!location.getOwner().getId().equals(userId)){
            throw new ForbiddenActionException("Permission denied: cannot update another user's location");
        }

        Collection collection;
        Optional<Collection> tmp2 = collectionRepository.findByTagAndOwnerId(tag, userId);
        if (tmp2.isEmpty()) collection = collectionService.createCollection(tag, userId);
        else collection = tmp2.get();

        if(!location.getCollections().contains(collection)){
            location.getCollections().add(collection);
        }
        if (!collection.getLocations().contains(location)){
            collection.getLocations().add(location);
        }

        locationRepository.save(location);
        collectionRepository.save(collection);

        return collection;
    }

    public void deleteFromCollection(long locationId, String tag, Long userId) {

        Optional<Location> tmp = locationRepository.findById(locationId);
        if (tmp.isEmpty()){
            throw new NotFoundException("Location with id " + locationId + " not found.");
        }

        Location location = tmp.get();
        if (!location.getOwner().getId().equals(userId)){
            throw new ForbiddenActionException("Permission denied: cannot update another user's location");
        }

        Optional<Collection> tmp2 = collectionRepository.findByTagAndOwnerId(tag, userId);
        if (tmp2.isEmpty()){
            throw new NotFoundException("Collection with tag " + tag + " not found.");
        }
        Collection collection = tmp2.get();

        collection.getLocations().remove(location);
        location.getCollections().remove(collection);

        locationRepository.save(location);
        collectionRepository.save(collection);

    }
}
