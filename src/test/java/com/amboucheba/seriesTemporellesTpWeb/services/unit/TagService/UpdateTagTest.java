package com.amboucheba.seriesTemporellesTpWeb.services.unit.TagService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.Event;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.models.Tag;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
import com.amboucheba.seriesTemporellesTpWeb.repositories.EventRepository;
import com.amboucheba.seriesTemporellesTpWeb.repositories.TagRepository;
import com.amboucheba.seriesTemporellesTpWeb.repositories.UserRepository;
import com.amboucheba.seriesTemporellesTpWeb.services.*;
import com.amboucheba.seriesTemporellesTpWeb.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@WebMvcTest(TagService.class)
public class UpdateTagTest {

    @MockBean
    private TagRepository tagRepository;

    @MockBean
    private EventService eventService;

    @MockBean
    UserService userService;

    @MockBean
    PartageService partageService;

    @Autowired
    TagService tagService;

    @TestConfiguration
    static class Config{

        @MockBean
        public UserRepository userRepository;

        @Bean
        public JwtUtil getUtil(){
            return new JwtUtil();
        }

        @Bean
        public AuthService getAuth(){
            return new AuthService();
        }

        @Bean
        public TagService getService(){
            return new TagService();
        }
    }

    @Test
    public void eventExists__returnUpdatedEvent() {
        User user = new User(1L, "user", "pass");
        SerieTemporelle st = new SerieTemporelle(1L,"event", "pass", user);
        Event event = new Event(1L, new Date(), 5.0f,"comment", st);
        Tag toUpdate = new Tag(1L, "tag", event);
        Mockito.when(tagRepository.findById(1L)).thenReturn(Optional.of(toUpdate));

        Tag newTag = new Tag(1L, "newtag", event);
        // Suppose user is authenticated
        Mockito.when(userService.initiatorIsOwner(1L, 1L)).thenReturn(true);
        Mockito.when(tagRepository.save(newTag)).thenReturn(newTag);

        Tag updatedTag = tagService.updateTag(1L, newTag, 1L);

        assertEquals(newTag, updatedTag);
    }

    @Test
    public void tagDoesNotExist__ThrowNotFoundException(){

        Mockito.when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            tagService.updateTag(1L,new Tag(), 1L);
        });
    }

}
