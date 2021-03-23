package com.amboucheba.etudeDeCasWeb.Services;

import com.amboucheba.etudeDeCasWeb.Exceptions.DuplicateResourceException;
import com.amboucheba.etudeDeCasWeb.Exceptions.ForbiddenActionException;
import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Location;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.LocationInput;
import com.amboucheba.etudeDeCasWeb.Repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    UserService userService;

    public Location createLocation(String title,String description, long userId){

        User user = userService.find(userId);

        Optional<Location> tmp = locationRepository.findByTitleAndOwnerId(title,userId);
        if (tmp.isPresent()){
            throw new DuplicateResourceException("'Location' with title " + title + " already exists for user " + user.getEmail());
        }

        Location location = new Location(title,description, user);
        return locationRepository.save(location);
    }

    public List<Location> listLocationsByUser(long userId) {


        User user = userService.find(userId);
        return locationRepository.findByOwnerId(userId);
    }

    public Location find(long locationId, long userId){

        Optional<Location> tmp = locationRepository.findById(locationId);
        if (tmp.isEmpty()){
            throw new NotFoundException("Location with id " + locationId + " not found.");
        }

        Location location = tmp.get();


        if (location.getOwner().getId().equals(userId)){
            // user is owner --> return thelocation
            return location;
        }

        return null;
    }





    public void deleteLocatiion(long locationId, Long userId) {

        Optional<Location> tmp = locationRepository.findById(locationId);
        if (!tmp.isPresent()){
            throw new NotFoundException("location with id " + locationId + " not found.");
        }

        Location location = tmp.get();

        if (!location.getOwner().getId().equals(userId)){
            throw new ForbiddenActionException("Permission denied: cannot delete another user's location");
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


        if (location.getOwner().getId().equals(userId)){
            // user is owner --> update the location
            location.setTitle(newLocation.getTitle());
            location.setDescription(newLocation.getDescription());
            return locationRepository.save(location);
        }
        return null;

    }

}
