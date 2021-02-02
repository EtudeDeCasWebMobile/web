package com.amboucheba.etudeDeCasWeb.Services;

import com.amboucheba.etudeDeCasWeb.Exceptions.ForbiddenActionException;
import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;

import com.amboucheba.etudeDeCasWeb.Models.ModelLists.SerieTemplorelleList;
import com.amboucheba.etudeDeCasWeb.Models.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.User;
import com.amboucheba.etudeDeCasWeb.Repositories.SerieTemporelleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SerieTemporelleService {

    @Autowired
    SerieTemporelleRepository serieTemporelleRepository;

    @Autowired
    UserService userService;

    @Autowired
    PartageService partageService;

    // We shouldn't implement this one: it is here for development purpose only
    public SerieTemplorelleList listSerieTemporelle(){
        List<SerieTemporelle> liste = StreamSupport.stream(serieTemporelleRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return new SerieTemplorelleList(liste);
    }

    public SerieTemporelle find(long serieTemporelleId){
        Optional<SerieTemporelle> result = serieTemporelleRepository.findById(serieTemporelleId);

        if (result.isEmpty()){
            throw new NotFoundException("Serie temporelle Not Found");
        }
        return result.get();
    }
    public SerieTemporelle find(long serieTemporelleId, Long initiatorId){

        // handles not found
        SerieTemporelle st = find(serieTemporelleId);

        // initiator is the owner of the targeted serie temporelle
        boolean isOwner = userService.initiatorIsOwner(st.getOwner().getId(), initiatorId);
        // Owner shared serie temporelle with initiator with read(r) access
        boolean hasAccess = partageService.hasAccess(initiatorId, serieTemporelleId, "r");

        if (!isOwner && !hasAccess){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        // Either initiator is the owner of the serie temporelle -> he has access to it
        // Or owner of serie temporelle shared it with the initiator -> he has access to it
        // in these cases allow access to serie temporelle
        return st;
    }

    // Does the creation job
    SerieTemporelle createSerieTemporelle(SerieTemporelle serieTemporelle, User user){
        serieTemporelle.setOwner(user);
        return serieTemporelleRepository.save(serieTemporelle);
    }
    public SerieTemporelle createSerieTemporelle(SerieTemporelle serieTemporelle, long userId, Long initiatorId){
        // Initiator can only add series temporelles to himself
        if (!userService.initiatorIsOwner(userId, initiatorId)){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        // this handles : user not found
        User user = userService.find(userId);

        return createSerieTemporelle(serieTemporelle, user);
    }

    // Does the finding job
    List<SerieTemporelle> listSerieTemporelleOfOwner(long userId){
        return serieTemporelleRepository.findByOwnerId(userId);
    }
    public List<SerieTemporelle> listSerieTemporelleOfOwner(long userId, Long initiatorId){

        // Initiator can only add series temporelles to himself
        if (!userService.initiatorIsOwner(userId, initiatorId)){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        // this handles : user not found
        User user = userService.find(userId);

        return listSerieTemporelleOfOwner(userId);
    }

    // Does the update job
    SerieTemporelle updateSerieTemporelle(SerieTemporelle serieTemporelle, SerieTemporelle newSerieTemporelle){

        serieTemporelle.setTitre(newSerieTemporelle.getTitre());
        serieTemporelle.setDescription(newSerieTemporelle.getDescription());
        return serieTemporelleRepository.save(serieTemporelle);
    }
    public SerieTemporelle updateSerieTemporelle(SerieTemporelle newSerieTemporelle, long serieTemporelleId, Long initiatorId){

        SerieTemporelle st = find(serieTemporelleId);

        // initiator is the owner of the targeted serie temporelle
        boolean isOwner = userService.initiatorIsOwner(st.getOwner().getId(), initiatorId);
        // Owner shared serie temporelle with initiator with write(w) access
        boolean hasAccess = partageService.hasAccess(initiatorId, serieTemporelleId, "w");

        if (!isOwner && !hasAccess){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        // Either initiator is the owner of the serie temporelle -> he has access to it
        // Or owner of serie temporelle shared it (with write access) with the initiator -> he has access to it
        // in these cases allow access to serie temporelle -> update it
        return updateSerieTemporelle(st, newSerieTemporelle);
    }

    // Does the remove job
    void removeSerieTemporelle(long serieTemporelleId){
        serieTemporelleRepository.deleteById(serieTemporelleId);
    }
    public void removeSerieTemporelle(long serieTemporelleId, Long initiator){

        SerieTemporelle st = find(serieTemporelleId);
        // initiator of the request must the owner of the serie temporelle
        if (!userService.initiatorIsOwner(st.getOwner().getId(), initiator)){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        removeSerieTemporelle(serieTemporelleId);
    }


}
