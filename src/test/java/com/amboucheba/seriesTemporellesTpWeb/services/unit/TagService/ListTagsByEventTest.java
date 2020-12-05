package com.amboucheba.seriesTemporellesTpWeb.services.unit.TagService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.Event;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.models.Tag;
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

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TagService.class)
public class ListTagsByEventTest {


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
    public void eventExists__returnTagsOfEvent() {
        Event event = new Event(1L, new Date(), 5.0f, "comment", null);
        List<Tag> toBeReturned = Collections.singletonList(
                new Tag(1L, "tag", event)
        );
        Mockito.when(eventService.find(1L)).thenReturn(event);
        Mockito.when(tagRepository.findByEventId(1L)).thenReturn(toBeReturned);

        List<Tag> returned = tagService.listTagsByEvent(1L);

        assertEquals(toBeReturned, returned);
    }

    @Test
    public void eventDoesNotExist__ThrowNotFoundException(){

        Mockito.when(eventService.find(1L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> {
            tagService.listTagsByEvent(1L);
        });
    }
}
