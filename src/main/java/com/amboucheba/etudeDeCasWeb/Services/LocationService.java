package com.amboucheba.etudeDeCasWeb.Services;

import com.amboucheba.etudeDeCasWeb.Exceptions.ForbiddenActionException;
import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.LocationShareDetails;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Location;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Repositories.LocationRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
import com.amboucheba.etudeDeCasWeb.Util.JwtUtil;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    JwtUtil jwtUtil;

    public String generateShareableToken(long userId) {
    	User user = find(userId);

        Optional<Location> tmp = locationRepository.findByOwner(user);
        if (tmp.isEmpty()){
            throw new NotFoundException("CurrentLocation of user " + userId + " not found.");
        }
        Location location = tmp.get();
//        if (!location.getOwner().getId().equals(userId)){
//            throw new ForbiddenActionException("Permission denied: cannot request to share another user's collection");
//        }

        LocationShareDetails locationShare = new LocationShareDetails(location.getId());
        return jwtUtil.generateToken(locationShare);
    }
    public User find(long userId){
        Optional<User> result = userRepository.findById(userId);
        // check that targeted user exists
        if (result.isEmpty()){
            throw new NotFoundException("'User' with id " + userId + " not found");
        }
        return result.get();
    }

    public Location find(long userId, String token){

    	User user = find(userId);

        Optional<Location> tmp = locationRepository.findByOwner(user);
        if (tmp.isEmpty()){
            throw new NotFoundException("Location with id " + userId + " not found.");
        }

        Location currentLocation = tmp.get();
        

        if (currentLocation.getOwner().getId().equals(userId)){
           
            return currentLocation;
        }
     
        if (token == null || token.isEmpty()){
            throw new ForbiddenActionException("Permission denied: cannot access another user's collection(token required)");
        }
        
        long allowedCollectionId = jwtUtil.extractUserId(token);
        if (userId != allowedCollectionId){
            throw new ForbiddenActionException("Permission denied: cannot access another user's collection(wrong/invalid token)");
        }

        return currentLocation;
    }
    
    public Location updateCurrentLocation(long userId,  @Valid Location newLocation) {

        // handles not found
    	User user = find(userId);

        Optional<Location> tmp = locationRepository.findByOwner(user);
        if (tmp.isEmpty()){
        	Location loca = new Location();
     	   	return updateLocation( userId, loca, newLocation);
        }else {
        Location loc = tmp.get();
        return updateLocation( userId,loc, newLocation);
        }
    }
    Location updateLocation(long userId, Location loc, @Valid Location newLocation) {
    	Optional<User> user = userRepository.findById(userId);
    	loc.setDescription(newLocation.getDescription());
    	loc.setTitle(newLocation.getTitle());
    	loc.setId(newLocation.getId());
    	loc.setLatitude(newLocation.getLatitude());
    	loc.setLongitude(newLocation.getLongitude());    	
    	loc.setOwner(user.get());
        return locationRepository.save(loc);
    }
}
