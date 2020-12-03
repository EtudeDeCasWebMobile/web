package com.amboucheba.seriesTemporellesTpWeb.services;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.PartageList;
import com.amboucheba.seriesTemporellesTpWeb.models.Partage;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.SerieTemplorelleList;
import com.amboucheba.seriesTemporellesTpWeb.repositories.PartageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PartageService {

    @Autowired
    PartageRepository partageRepository;


    public PartageList listPartageByUserId(long userId) throws NotFoundException {

        List<Partage> liste = partageRepository.findByUserId(userId);
        return new PartageList(liste);

    }

    public PartageList listPartageBySerieTemporelleId(long serieTemporelleId) throws NotFoundException {

        List<Partage> liste = partageRepository.findBySerieTemporelleId(serieTemporelleId);
        return new PartageList(liste);

    }

    public long createPartage(Partage partage){
        return partageRepository.save(partage).getId();
    }

    public Partage updatePartage(Partage newPartage, long partageId){

        Optional<Partage> partage = partageRepository.findById(partageId);

        if (partage.isPresent()){
            Partage actualPartage = partage.get();
            actualPartage.setType(newPartage.getType());
            Partage savedPartage = partageRepository.save(actualPartage);
            return savedPartage;
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
