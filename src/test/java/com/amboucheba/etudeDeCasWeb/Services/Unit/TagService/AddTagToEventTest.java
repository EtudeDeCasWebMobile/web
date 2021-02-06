package com.amboucheba.etudeDeCasWeb.Services.Unit.TagService;

import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Event;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Tag;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.TagRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
import com.amboucheba.etudeDeCasWeb.Services.*;
import com.amboucheba.etudeDeCasWeb.Util.JwtUtil;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TagService.class)
public class AddTagToEventTest {


    @MockBean
    private TagRepository tagRepository;

    @MockBean
    EventService eventService;

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
    public void eventExists__addTagToEvent(){

        Users users = new Users(1L, "user", "pass");
        SerieTemporelle st = new SerieTemporelle(1L,"event", "pass", users);
        Event event = new Event(1L, new Date(), 5.0f,"comment", st);

        Tag toSave = new Tag( "tag", event);
        Tag saved = new Tag(1L, "tag", event);

        // Suppose user is authenticated
        Mockito.when(userService.initiatorIsOwner(1L, 1L)).thenReturn(true);
        Mockito.when(eventService.find(1L)).thenReturn(event);
        Mockito.when(tagRepository.save(toSave)).thenReturn(saved);

        Tag returned = tagService.addTagToEvent(1L, toSave, 1L);

        assertEquals(saved, returned);
    }

    @Test
    public void eventDoesNotExist__ThrowNotFoundException(){

        Mockito.when(eventService.find(1L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> {
            tagService.addTagToEvent(1L, new Tag(), 1L );
        });
    }

}
