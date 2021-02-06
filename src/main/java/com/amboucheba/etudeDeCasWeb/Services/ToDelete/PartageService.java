package com.amboucheba.etudeDeCasWeb.Services.ToDelete;

import com.amboucheba.etudeDeCasWeb.Exceptions.ForbiddenActionException;
import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Partage;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.PartageRequest;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.PartageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartageService {

    @Autowired
    PartageRepository partageRepository;

    @Autowired
    UsersService usersService;

    @Autowired
    SerieTemporelleService serieTemporelleService;

    public boolean hasAccess(long userId, long stId, String type){
        Optional<Partage> partage = partageRepository.findByUsersIdAndSerieTemporelleIdAndType(userId, stId, type);

        if(partage.isEmpty()) return false;
        if (type.equals("r")) return true;

        return partage.get().getType().equals(type);
    }

    // Does the find job
    Partage find(long partageId){
        Optional<Partage> result = partageRepository.findById(partageId);

        if(result.isEmpty()) {
            throw new NotFoundException("Partage with id " + partageId + " not found");
        }
        return result.get();
    }
    public Partage find(long partageId, Long initiatorId){

        Partage partage = find(partageId);
        SerieTemporelle st = partage.getSerieTemporelle();

        if (!usersService.initiatorIsOwner(st.getOwner().getId(), initiatorId) ){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        return partage;
    }

    List<Partage> listPartageByUserId(long userId)  {
        return partageRepository.findByUsersId(userId);
    }
    public List<Partage> listPartageByUserId(long userId, Long initiatorId)  {

        // Initiator can only add series temporelles to himself
        if (!usersService.initiatorIsOwner(userId, initiatorId)){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        // handles: user not found
        Users users = usersService.find(userId);
        return listPartageByUserId(userId);
    }

    //
    List<Partage> listPartageBySerieTemporelleId(long serieTemporelleId) throws NotFoundException {
        return partageRepository.findBySerieTemporelleId(serieTemporelleId);
    }
    public List<Partage> listPartageBySerieTemporelleId(long serieTemporelleId, Long initiatorId) {

        // handles not found
        SerieTemporelle st = serieTemporelleService.find(serieTemporelleId);

        // initiator must the owner of the serie temporelle to access its partages
        if (!usersService.initiatorIsOwner(st.getOwner().getId(), initiatorId)){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        return listPartageBySerieTemporelleId(serieTemporelleId);
    }

    //
    Partage createPartage(Users users, SerieTemporelle st, String type){
        Partage savedPartage = new Partage(users, st, type);
        return partageRepository.save(savedPartage);
    }
    public Partage createPartage(PartageRequest partage, Long initiatorId){
        // Not found is handled inside service call
        Users users = usersService.find(partage.getUserId());
        // owner cannot share serie temporelle with himself
        if (usersService.initiatorIsOwner(users.getId(), initiatorId)){
            throw new ForbiddenActionException("Permission denied: owner cannot share with himself.");
        }
        // initiator must be the owner of serie temporelle to be able to share it
        SerieTemporelle st = serieTemporelleService.find(partage.getSerieTemporelleId());
        if (!usersService.initiatorIsOwner(st.getOwner().getId(), initiatorId)){
            throw new ForbiddenActionException("Permission denied: cannot share other users' serie temporelle");
        }

        return createPartage(users, st, partage.getType());
    }

    Partage updatePartage(Partage partage, String type){
        partage.setType(type);
        return partageRepository.save(partage);
    }
    public Partage updatePartage(PartageRequest newPartage, long partageId, Long initiatorId){

        Partage partage = find(partageId);
        SerieTemporelle st = partage.getSerieTemporelle();

        if (!usersService.initiatorIsOwner(st.getOwner().getId(), initiatorId)){
            throw new ForbiddenActionException("Permission denied: cannot update other users' partages");
        }

        return updatePartage(partage, newPartage.getType());
    }

    void removePartage(long partageId){
        partageRepository.deleteById(partageId);
    }
    public void removePartage(long partageId, Long initiatorId) {
        Partage partage = find(partageId);
        SerieTemporelle st = partage.getSerieTemporelle();

        if (!usersService.initiatorIsOwner(st.getOwner().getId(), initiatorId)){
            throw new ForbiddenActionException("Permission denied: cannot delete other users' partages");
        }
        removePartage(partageId);
    }
}
