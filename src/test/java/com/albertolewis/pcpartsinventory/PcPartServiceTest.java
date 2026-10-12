package com.albertolewis.pcpartsinventory;

import com.albertolewis.pcpartsinventory.dto.PcPartResponse;
import com.albertolewis.pcpartsinventory.dto.RestockRequest;
import com.albertolewis.pcpartsinventory.exception.PcPartNotFoundException;
import com.albertolewis.pcpartsinventory.model.PartsCategory;
import com.albertolewis.pcpartsinventory.model.PcPart;
import com.albertolewis.pcpartsinventory.model.User;
import com.albertolewis.pcpartsinventory.repository.PcPartRepository;
import com.albertolewis.pcpartsinventory.service.CustomUserDetailsService;
import com.albertolewis.pcpartsinventory.service.PcPartService;
import com.albertolewis.pcpartsinventory.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PcPartServiceTest {

    @Mock
    private PcPartRepository pcPartRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PcPartService pcPartService;


    @Test
    void getAllQuantityLessThan_returnsLowStockParts() {
        //Arrange

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        PcPart[] pcParts = {
                new PcPart("7600X", PartsCategory.CPU, "AMD", new BigDecimal(120), 4),
                new PcPart("RTX 3080", PartsCategory.GPU, "NVIDIA", new BigDecimal(500), 2),
                new PcPart("16GB DDR4", PartsCategory.RAM, "Corsair", new BigDecimal(100), 10)
        };

        when(userService.getUserByUsername("testuser"))
                .thenReturn(user);

        when(pcPartRepository.findAllByOwner_IdAndQuantityLessThan(1L, 5))
                .thenReturn(Arrays.asList(pcParts[0], pcParts[1]));

        //Act
        List<PcPartResponse> result = pcPartService.getAllByQuantityLessThan(5, user.getUsername());

        //Assert
        assertEquals("7600X", result.getFirst().getName());
        assertEquals("RTX 3080", result.get(1).getName());
        assertEquals(4, result.getFirst().getQuantity());
        assertEquals(2, result.get(1).getQuantity());
        assertEquals(2, result.size());

        //Verify
        verify(pcPartRepository).findAllByOwner_IdAndQuantityLessThan(1L, 5);
    }

    @Test
    void getAllByCategory_returnsMatchingParts() {

        //Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        PcPart pcParts = new PcPart("RTX 3080", PartsCategory.GPU, "NVIDIA", new BigDecimal(500), 2);

        when(userService.getUserByUsername("testuser"))
                .thenReturn(user);

        when(pcPartRepository.findAllByOwner_IdAndCategory(user.getId(), PartsCategory.GPU))
                .thenReturn(List.of(pcParts));

        //Act
        List<PcPartResponse> result = pcPartService.getAllByCategory("  gpu  ", "testuser");

        //Assert
        // Assert
        assertEquals(1, result.size());
        assertEquals("RTX 3080", result.getFirst().getName());
        assertEquals(PartsCategory.GPU, result.getFirst().getCategory());
        assertEquals("NVIDIA", result.getFirst().getManufacturer());
        assertEquals(2, result.getFirst().getQuantity());


        //Verify
        verify(pcPartRepository).findAllByOwner_IdAndCategory(1L, PartsCategory.GPU);

    }

    @Test
    void getAllByManufacturer_returnsMatchingParts() {

        //Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");


        PcPart amdPart = new PcPart("7600X", PartsCategory.CPU, "AMD", new BigDecimal(120), 4);

        when(userService.getUserByUsername("testuser"))
                .thenReturn(user);

        when(pcPartRepository.findAllByOwner_IdAndManufacturerIgnoreCase(1L, "AMD"))
                .thenReturn(List.of(amdPart));

        //Act
        List<PcPartResponse> result = pcPartService.getAllByManufacturer("AMD", "testuser");

        //Assert
        assertEquals(1, result.size());
        assertEquals("7600X", result.getFirst().getName());
        assertEquals(PartsCategory.CPU, result.getFirst().getCategory());
        assertEquals("AMD", result.getFirst().getManufacturer());
        assertEquals(4, result.getFirst().getQuantity());

        //Verify
        verify(pcPartRepository).findAllByOwner_IdAndManufacturerIgnoreCase(1L, "AMD");

    }

    @Test
    void getAllByQuantityLessThan_negativeThreshold_throwsException() {
        //Arrange
        int negativeThreshold = -1;


        //Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pcPartService.getAllByQuantityLessThan(negativeThreshold, "testuser");
        });

        assertEquals("Threshold must be a non-negative integer.", exception.getMessage());

        //Verify that the repository method was never called
        verifyNoInteractions(pcPartRepository);
        verifyNoInteractions(userService);
    }

    @Test
    void getAllByManufacturer_blankName_throwsException() {

        //Arrange
        String manufacturer = "  ";

        //Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pcPartService.getAllByManufacturer(manufacturer, "testuser");
        });

        assertEquals("Manufacturer cannot be null or empty.", exception.getMessage());

        //Verify
        verifyNoInteractions(pcPartRepository);
        verifyNoInteractions(userService);

    }

    @Test
    void getAllByCategory_blankName_throwsException() {
        //Arrange
        String category = " ";

        //Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pcPartService.getAllByCategory(category, "testuser");
        });

        assertEquals("Category cannot be null or empty.", exception.getMessage());

        //Verify
        verifyNoInteractions(pcPartRepository);
        verifyNoInteractions(userService);
    }

    @Test
    void getAllByCategory_invalidCategory_throwsException() {
        //Arrange
        String category = "KEYBOARD";

        //Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pcPartService.getAllByCategory(category, "testuser");
        });

        assertEquals("Invalid category: KEYBOARD", exception.getMessage());

        //Verify
        verifyNoInteractions(pcPartRepository);
        verify(userService).getUserByUsername("testuser");
    }


    @Test
    void getAllByManufacturer_nullName_throwsException() {

        //Arrange
        String manufacturer = null;

        //Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pcPartService.getAllByManufacturer(manufacturer, "testuser");
        });

        assertEquals("Manufacturer cannot be null or empty.", exception.getMessage());

        //Verify
        verifyNoInteractions(pcPartRepository);
        verifyNoInteractions(userService);
    }

    @Test
    void restockPcPartQuantity() {
        //Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        RestockRequest restockQuantity = new RestockRequest();
        restockQuantity.setQuantity(5);

        PcPart pcPart = new PcPart("9800X3D", PartsCategory.CPU, "AMD", new BigDecimal("250.00"), 2);

        when(pcPartRepository.findByIdAndOwner_Id(1L, 1L))
                .thenReturn(Optional.of(pcPart));

        when(userService.getUserByUsername("testuser"))
                .thenReturn(user);

        when(pcPartRepository.save(pcPart))
                .thenReturn(pcPart);

        //Act
        PcPartResponse result = pcPartService.restockPcPart(1L, restockQuantity, "testuser");


        //Assert
        assertEquals(7, result.getQuantity());
        assertEquals("9800X3D", result.getName());
        assertEquals(PartsCategory.CPU, result.getCategory());


        //Verify
        verify(pcPartRepository).findByIdAndOwner_Id(1L, 1L);
        verify(pcPartRepository).save(pcPart);


    }

    @Test
    void restockPcPart_notOwnedByUser_throwsException() {
        //Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        RestockRequest restockQuantity = new RestockRequest();
        restockQuantity.setQuantity(5);

        when(pcPartRepository.findByIdAndOwner_Id(1L, 1L))
                .thenReturn(Optional.empty());

        when(userService.getUserByUsername("testuser"))
                .thenReturn(user);

        //Act & Assert
        PcPartNotFoundException exception = assertThrows(PcPartNotFoundException.class, () -> {
            pcPartService.restockPcPart(1L, restockQuantity, "testuser");
        });

        assertEquals("Pc Part id: 1 not found", exception.getMessage());

        //Verify
        verify(pcPartRepository).findByIdAndOwner_Id(1L, 1L);
        verify(pcPartRepository, never()).save(any(PcPart.class));
    }

    @Test
    void restockPcPartQuantity_notFound_throwsException() {

        //Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        RestockRequest restockRequest = new RestockRequest();
        restockRequest.setQuantity(5);

        when(userService.getUserByUsername("testuser"))
                .thenReturn(user);

        when(pcPartRepository.findByIdAndOwner_Id(2L, 1L))
                .thenReturn(Optional.empty());

        PcPartNotFoundException exception = assertThrows(PcPartNotFoundException.class, () -> {
            pcPartService.restockPcPart(2L, restockRequest, "testuser");
        });

        assertEquals("Pc Part id: 2 not found", exception.getMessage());

        //Verify
        verify(pcPartRepository).findByIdAndOwner_Id(2L, 1L);
        verify(pcPartRepository, never()).save(any(PcPart.class));

    }


}
