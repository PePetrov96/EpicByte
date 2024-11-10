package com.project.EpicByte.web.RESTControllers;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.project.EpicByte.model.entity.enums.MovieCarrierEnum;
import com.project.EpicByte.model.entity.enums.MusicCarrierEnum;
import com.project.EpicByte.model.entity.enums.ProductTypeEnum;
import com.project.EpicByte.model.entity.productEntities.Music;
import com.project.EpicByte.repository.productRepositories.MusicRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MusicRESTControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MusicRepository musicRepository;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    private GreenMail greenMail;

    private UUID productID;

    @BeforeEach
    void setUp() {
        greenMail = new GreenMail(new ServerSetup(port, host, "smtp"));
        greenMail.start();
        greenMail.setUser(username, password);

        if (this.musicRepository.count() == 0) {
            musicRepository.saveAndFlush(returnMusic("Name One"));
            musicRepository.saveAndFlush(returnMusic("Name Two"));
        }

        productID = returnMusicId();
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
        musicRepository.deleteAll();
    }

    @Test
    void getAllMusic() throws Exception {
        List<Music> music = musicRepository.findAll();

        mockMvc.perform(get("/api/user/music")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productName", is(music.get(0).getProductName())))
                .andExpect(jsonPath("$[1].productName", is(music.get(1).getProductName())));
    }

    @Test
    void getMusicById() throws Exception {
        Music music = musicRepository.findMusicById(productID);

        mockMvc.perform(get("/api/user/music/" + music.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("productName", is(music.getProductName())));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteMusic() throws Exception {
        mockMvc.perform(delete("/api/admin/music/" + productID)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    private Music returnMusic(String name) {
        Music music = new Music();

        music.setDateCreated(LocalDate.now());
        music.setNewProduct(true);
        music.setProductType(ProductTypeEnum.BOOK);
        music.setProductImageUrl("path/to/image.jpg");
        music.setProductName(name);
        music.setProductPrice(BigDecimal.valueOf(25.99));
        music.setDescription("This is a book description.");
        music.setArtistName("Artist Name");
        music.setPublisher("Publisher Name");
        music.setPublicationDate(LocalDate.now());
        music.setCarrier(MusicCarrierEnum.Vinyl);
        music.setGenre("Genre");

        return music;
    }

    private UUID returnMusicId() {
        return this.musicRepository.findAll().get(0).getId();
    }
}