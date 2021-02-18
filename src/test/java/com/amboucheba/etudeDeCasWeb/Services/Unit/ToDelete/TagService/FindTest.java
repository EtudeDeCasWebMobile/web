package com.amboucheba.etudeDeCasWeb.Services.Unit.ToDelete.TagService;

import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Event;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Tag;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.TagRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.UsersRepository;
import com.amboucheba.etudeDeCasWeb.Services.AuthService;
import com.amboucheba.etudeDeCasWeb.Services.ToDelete.*;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TagService.class)
public class FindTest {

    @MockBean
    private TagRepository tagRepository;

    @MockBean
    EventService eventService;

    @MockBean
    UsersService usersService;

    @MockBean
    PartageService partageService;

    @Autowired
    TagService tagService;

    @TestConfiguration
    static class Config{

        @MockBean
        public UsersRepository usersRepository;

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
    public void tagExists__returnTag() throws Exception {

        Users users = new Users(1L, "user", "pass");
        SerieTemporelle st = new SerieTemporelle(1L,"event", "pass", users);
        Event event = new Event(1L, new Date(), 5.0f,"comment", st);
        long tagId = 1L;
        Tag tag = new Tag(1L, "tag", event);

        // Suppose user is authenticated
        Mockito.when(usersService.initiatorIsOwner(1L, 1L)).thenReturn(true);
        Mockito.when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag));

        Tag found = tagService.find(tagId, 1L);

        assertEquals(found, tag);
    }

    @Test
    public void tagNotFound__throwNotFoundException() {

        Mockito.when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            tagService.find(1L, 1L);
        });
    }

}