package com.albertolewis.pcpartsinventory;

import com.albertolewis.pcpartsinventory.model.PartsCategory;
import com.albertolewis.pcpartsinventory.model.PcPart;
import com.albertolewis.pcpartsinventory.model.User;
import com.albertolewis.pcpartsinventory.repository.PcPartRepository;
import com.albertolewis.pcpartsinventory.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PcPartSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PcPartRepository pcPartRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    @Test
    void getAllPcParts_unauthenticatedUser_isRejected() throws Exception {


        mockMvc.perform(get("/api/pcparts"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @Transactional
    void login_validCredentials_authenticatedUser() throws Exception {
        User user1 = new User(
                "user1",
                "user1@example.com",
                passwordEncoder.encode("password1")
        );

        userRepository.save(user1);


        MvcResult loginResult = mockMvc.perform(
                        post("/api/auth/login")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "username": "user1",
                                            "password": "password1"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("user1"))
                .andExpect(jsonPath("$.username").value("user1"))
                .andReturn();

        MockHttpSession session =
                (MockHttpSession) loginResult.getRequest().getSession(false);

        mockMvc.perform(
                        get("/api/auth/me")
                                .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"));
    }

    @Test
    @Transactional
    void deletePcPart_otherUsersPart_returnsNotFound() throws Exception {
        User user1 = new User(
                "user1",
                "user1@example.com",
                passwordEncoder.encode("password1")
        );
        User user2 = new User(
                "user2",
                "user2@example.com",
                passwordEncoder.encode("password2")
        );

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        PcPart pcPart = new PcPart(
                "RTX 3080",
                PartsCategory.GPU,
                "Nvidia",
                new BigDecimal("699.99"),
                2
        );

        pcPart.setOwner(user2);

        pcPart = pcPartRepository.save(pcPart);

        mockMvc.perform(
                        delete("/api/pcparts/{id}", pcPart.getId())
                                .with(user("user1"))
                                .with(csrf())
                )
                .andExpect(status().isNotFound());

        entityManager.flush();
        entityManager.clear();

        PcPart savedPart = pcPartRepository.findById(pcPart.getId())
                .orElseThrow();

        assertEquals(user2.getId(), savedPart.getOwner().getId());
        assertTrue(pcPartRepository.existsById(pcPart.getId()));


    }

    @Test
    @Transactional
    void updatePcPart_otherUsersPart_returnsNotFound() throws Exception {
        User user1 = new User(
                "user1",
                "user1@example.com",
                passwordEncoder.encode("password1")
        );
        User user2 = new User(
                "user2",
                "user2@example.com",
                passwordEncoder.encode("password2")
        );

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        PcPart pcPart = new PcPart(
                "RTX 3080",
                PartsCategory.GPU,
                "Nvidia",
                new BigDecimal("699.99"),
                2
        );

        pcPart.setOwner(user2);

        pcPart = pcPartRepository.save(pcPart);

        mockMvc.perform(
                        put(("/api/pcparts/{id}"), pcPart.getId())
                                .with(user("user1"))
                                .with(csrf())
                                .contentType(String.valueOf(APPLICATION_JSON))
                                .content("""
                                        {
                                          "name": "RTX 3080",
                                          "category": "GPU",
                                          "manufacturer": "Nvidia",
                                          "price": 499.99,
                                          "quantity": 2
                                        }
                                        """)

                )
                .andExpect(status().isNotFound());
        entityManager.flush();
        entityManager.clear();

        PcPart savedPart = pcPartRepository.findById(pcPart.getId())
                .orElseThrow();

        //Assert
        assertEquals(user2.getId(), savedPart.getOwner().getId());
        assertEquals(new BigDecimal("699.99"), savedPart.getPrice());
    }

    @Test
    @Transactional
    void restockPcPart_otherUsersPart_returnsNotFound() throws Exception {

        User user1 = new User(
                "user1",
                "user1@example.com",
                passwordEncoder.encode("password1")
        );
        User user2 = new User(
                "user2",
                "user2@example.com",
                passwordEncoder.encode("password2")
        );

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        PcPart pcPart = new PcPart(
                "RTX 3080",
                PartsCategory.GPU,
                "Nvidia",
                new BigDecimal("699.99"),
                2
        );


        pcPart.setOwner(user2);

        pcPart = pcPartRepository.save(pcPart);


        mockMvc.perform(
                        patch("/api/pcparts/{id}/restock", pcPart.getId())
                                .with(user("user1"))
                                .with(csrf())
                                .contentType(String.valueOf(APPLICATION_JSON))
                                .content("{\"quantity\":5}")
                )
                .andExpect(status().isNotFound());

        entityManager.flush();
        entityManager.clear();

        PcPart savedPart = pcPartRepository.findById(pcPart.getId())
                .orElseThrow();

        assertEquals(2, savedPart.getQuantity());

    }

    @Test
    @Transactional
    void getPcPartById_ownerCanAccess_returnsOk() throws Exception {

        User user1 = new User(
                "user1",
                "user1@example.com",
                passwordEncoder.encode("password1")
        );

        user1 = userRepository.save(user1);

        PcPart pcPart = new PcPart(
                "RTX 3080",
                PartsCategory.GPU,
                "Nvidia",
                new BigDecimal("699.99"),
                2
        );

        pcPart.setOwner(user1);

        pcPart = pcPartRepository.save(pcPart);

        mockMvc.perform(
                        get("/api/pcparts/{id}", pcPart.getId())
                                .with(user("user1"))

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("RTX 3080"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.owner").doesNotExist());


    }

    @Test
    @Transactional
    void getPcPartById_otherUsersPart_returnsNotFound() throws Exception {


        User user1 = new User(
                "user1",
                "user1@example.com",
                passwordEncoder.encode("password1")
        );
        User user2 = new User(
                "user2",
                "user2@example.com",
                passwordEncoder.encode("password2")
        );

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        PcPart pcPart = new PcPart(
                "RTX 3080",
                PartsCategory.GPU,
                "Nvidia",
                new BigDecimal("699.99"),
                2
        );

        //Set the pcPart to user2
        pcPart.setOwner(user2);

        pcPart = pcPartRepository.save(pcPart);

        mockMvc.perform(
                        get("/api/pcparts/{id}", pcPart.getId())
                                .with(user("user1"))
                )
                .andExpect(status().isNotFound());
    }
}