package com.amboucheba.seriesTemporellesTpWeb.services;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.PartageList;
import com.amboucheba.seriesTemporellesTpWeb.models.Partage;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.SerieTemplorelleList;
import com.amboucheba.seriesTemporellesTpWeb.models.PartageRequest;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
import com.amboucheba.seriesTemporellesTpWeb.repositories.PartageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Part;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PartageService {

    @Autowired
    PartageRepository partageRepository;

    @Autowired
    UserService userService;

    @Autowired
    SerieTemporelleService serieTemporelleService;

    public List<Partage> listPartageByUserId(long userId) throws NotFoundException {

        User user = userService.find(userId);
        return partageRepository.findByUserId(userId);
    }

    public List<Partage> listPartageBySerieTemporelleId(long serieTemporelleId) throws NotFoundException {

        SerieTemporelle serieTemporelle = serieTemporelleService.find(serieTemporelleId);
        return partageRepository.findBySerieTemporelleId(serieTemporelleId);
    }

    public Partage createPartage(PartageRequest partage){
        // Not found is handled inside service call
        User user = userService.find(partage.getUserId());
        SerieTemporelle serieTemporelle = serieTemporelleService.find(partage.getSerieTemporelleId());

        Partage savedPartage = new Partage(user, serieTemporelle, partage.getType());
        return partageRepository.save(savedPartage);
    }

    public Partage updatePartage(PartageRequest newPartage, long partageId){

        Optional<Partage> partage = partageRepository.findById(partageId);

        if (partage.isPresent()){
            Partage actualPartage = partage.get();
            actualPartage.setType(newPartage.getType());
            return partageRepository.save(actualPartage);
        }

        throw new NotFoundException("Partage with id " + partageId + " not found");
    }

    public void removePartage(long partageId) throws NotFoundException{
        if (!partageRepository.existsById(partageId)){
            throw new NotFoundException("Partage with id " + partageId + " not found");
        }
        partageRepository.deleteById(partageId);
    }

}
