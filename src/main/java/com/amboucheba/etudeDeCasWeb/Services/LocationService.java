package com.amboucheba.etudeDeCasWeb.Services;

import com.amboucheba.etudeDeCasWeb.Exceptions.ForbiddenActionException;
import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Position;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.PositionInput;
import com.amboucheba.etudeDeCasWeb.Models.LocationShareDetails;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Location;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Repositories.LocationRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.PositionRepository;
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
    PositionRepository positionRepository;
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;
    
    @Autowired
    JwtUtil jwtUtil;

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

        if (token == null || token.isEmpty()){
            throw new ForbiddenActionException("Permission denied: cannot access another user's collection(token required)");
        }

        // invalid token --> throws exception --> handled in ApiExceptionHandler
        long allowedCollectionId = jwtUtil.extractPositionId(token);
        if (current.getId() != allowedCollectionId){
            throw new ForbiddenActionException("Permission denied: cannot access another user's position(wrong/invalid token)");
        }
        return current;
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

}
