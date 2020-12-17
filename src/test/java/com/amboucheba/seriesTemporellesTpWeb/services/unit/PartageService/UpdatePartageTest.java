package com.amboucheba.seriesTemporellesTpWeb.services.unit.PartageService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.Event;
import com.amboucheba.seriesTemporellesTpWeb.models.Partage;
import com.amboucheba.seriesTemporellesTpWeb.models.PartageRequest;
import com.amboucheba.seriesTemporellesTpWeb.repositories.EventRepository;
import com.amboucheba.seriesTemporellesTpWeb.repositories.PartageRepository;
import com.amboucheba.seriesTemporellesTpWeb.services.EventService;
import com.amboucheba.seriesTemporellesTpWeb.services.PartageService;
import com.amboucheba.seriesTemporellesTpWeb.services.SerieTemporelleService;
import com.amboucheba.seriesTemporellesTpWeb.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@WebMvcTest(PartageService.class)
public class UpdatePartageTest {

    @MockBean
    private PartageRepository partageRepository;

    @MockBean
    SerieTemporelleService serieTemporelleService;

    @MockBean
    UserService userService;

    @Autowired
    private PartageService partageService;

    @TestConfiguration
    static class Config{

        @Bean
        public PartageService getService(){
            return new PartageService();
        }
    }


//    @Test
//    public void partageExists__returnUpdatedPartage() {
//        Partage partage = new Partage(1L, null, null, "r");
//        Mockito.when(partageRepository.findById(1L)).thenReturn(Optional.of(partage));
//
//        Partage newPartage = new Partage(1L, null, null, "w");
//        Mockito.when(partageRepository.save(newPartage)).thenReturn(newPartage);
//
//        PartageRequest pr = new PartageRequest( 1L, 1L, "w");
//        Partage updatedPartage = partageService.updatePartage(pr, 1L);
//
//        assertEquals(newPartage, updatedPartage);
//    }
//
//    @Test
//    public void partageDoesNotExist__ThrowNotFoundException(){
//
//        Mockito.when(partageRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> {
//            partageService.updatePartage( new PartageRequest(), 1L);
//        });
//    }

}
