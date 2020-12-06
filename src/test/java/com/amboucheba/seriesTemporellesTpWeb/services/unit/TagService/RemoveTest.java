package com.amboucheba.seriesTemporellesTpWeb.services.unit.TagService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.repositories.EventRepository;
import com.amboucheba.seriesTemporellesTpWeb.repositories.TagRepository;
import com.amboucheba.seriesTemporellesTpWeb.services.EventService;
import com.amboucheba.seriesTemporellesTpWeb.services.SerieTemporelleService;
import com.amboucheba.seriesTemporellesTpWeb.services.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TagService.class)
public class RemoveTest {

    @MockBean
    private TagRepository tagRepository;

    @MockBean
    private EventService eventService;


    @Autowired
    TagService tagService;

    @TestConfiguration
    static class Config{

        @Bean
        public TagService getService(){
            return new TagService();
        }
    }


    @Test
    public void tagExists__removeTag() {

        Mockito.when(tagRepository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(tagRepository).deleteById(1L);

        tagService.remove(1L);

        Mockito.verify(tagRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void tagDoesNotExist__ThrowNotFoundException(){

        Mockito.when(tagRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            tagService.remove(1L);
        });
    }
}
