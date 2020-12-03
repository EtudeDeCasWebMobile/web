package com.amboucheba.seriesTemporellesTpWeb.services;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.Evenement;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemplorelleList;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
import com.amboucheba.seriesTemporellesTpWeb.repositories.SerieTemporelleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SerieTemporelleService {

    @Autowired
    SerieTemporelleRepository serieTemporelleRepository;


    public SerieTemporelle getSerieTemporelleById(long serieTemporelleId) throws NotFoundException {

        Optional<SerieTemporelle> serieTemporelle = serieTemporelleRepository.findById(serieTemporelleId);
        if(serieTemporelle.isPresent()){
            return serieTemporelle.get();
        }
        throw new NotFoundException("Serie temporelle Not Found");
    }

    public long createSerieTemporelle(SerieTemporelle serieTemporelle){
        return serieTemporelleRepository.save(serieTemporelle).getId();
    }

    public SerieTemplorelleList listSerieTemporelle(){
        List<SerieTemporelle> liste = StreamSupport.stream(serieTemporelleRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        return new SerieTemplorelleList(liste, liste.size());
    }

    public SerieTemporelle updateSerieTemporelle(SerieTemporelle newSerieTemporelle, long serieTemporelleId){

        Optional<SerieTemporelle> serieTemporelle = serieTemporelleRepository.findById(serieTemporelleId);

        if (serieTemporelle.isPresent()){
            SerieTemporelle actualSerieTemporelle = serieTemporelle.get();
            actualSerieTemporelle.setTitre(newSerieTemporelle.getTitre());
            actualSerieTemporelle.setDescription(newSerieTemporelle.getDescription());
            SerieTemporelle savedSerieTemporelle = serieTemporelleRepository.save(actualSerieTemporelle);
            return savedSerieTemporelle;
        }

        throw new NotFoundException("SerieTemporelle with id " + serieTemporelleId + " not found");
    }

    public void removeSerieTemporelle(long serieTemporelleId) throws NotFoundException{
        if (!serieTemporelleRepository.existsById(serieTemporelleId)){
            throw new NotFoundException("SerieTemporelle with id " + serieTemporelleId + " not found");
        }
        serieTemporelleRepository.deleteById(serieTemporelleId);
    }


}
