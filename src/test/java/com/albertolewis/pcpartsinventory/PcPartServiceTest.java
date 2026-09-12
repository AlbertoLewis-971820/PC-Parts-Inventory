package com.albertolewis.pcpartsinventory;

import com.albertolewis.pcpartsinventory.model.PcPart;
import com.albertolewis.pcpartsinventory.repository.PcPartRepository;
import com.albertolewis.pcpartsinventory.service.PcPartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;

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

    @InjectMocks
    private PcPartService pcPartService;

    @Test
    void getAllQuantityLessThan_returnsLowStockParts(){
        //Arrange
        PcPart [] pcParts = {
                new PcPart("7600X", "CPU",  "AMD", new BigDecimal(120), 4),
                new PcPart("RTX 3080", "GPU", "NVIDIA", new BigDecimal(500), 2),
                new PcPart("16GB DDR4", "RAM", "Corsair", new BigDecimal(100), 10)
        };

        when(pcPartRepository.findAllByQuantityLessThan(5))
                .thenReturn(Arrays.asList(pcParts[0], pcParts[1]));

        //Act
        List<PcPart> result = pcPartService.getAllByQuantityLessThan(5);

        //Assert
        assertTrue(result.contains(pcParts[0]));
        assertTrue(result.contains(pcParts[1]));
        assertEquals(2, result.size());

        //Verify
        verify(pcPartRepository).findAllByQuantityLessThan(5);
    }

    @Test
    void getAllByManufacturer_returnsMatchingParts(){

        //Arrange
        PcPart [] pcParts = {
                new PcPart("7600X", "CPU",  "AMD", new BigDecimal(120), 4),
                new PcPart("RTX 3080", "GPU", "AMD", new BigDecimal(500), 2),
                new PcPart("16GB DDR4", "RAM", "Corsair", new BigDecimal(100), 10)
        };

        when(pcPartRepository.findAllByManufacturerIgnoreCase("AMD"))
                .thenReturn(Arrays.asList(pcParts[0],pcParts[1]));

        //Act
        List<PcPart> result = pcPartService.getAllByManufacturer("AMD");

        //Assert
        assertTrue(result.contains(pcParts[0]));
        assertTrue(result.contains(pcParts[1]));
        assertEquals(2, result.size());

        //Verify
        verify(pcPartRepository).findAllByManufacturerIgnoreCase("AMD");

    }

    @Test
    void getAllByQuantityLessThan_negativeThreshold_throwsException(){
        //Arrange
        int negativeThreshold = -1;

        //Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pcPartService.getAllByQuantityLessThan(negativeThreshold);
        });

        assertEquals("Threshold must be a non-negative integer.", exception.getMessage());

        //Verify that the repository method was never called
        verify(pcPartRepository, never()).findAllByQuantityLessThan(anyInt());
    }

    @Test
    void getAllByManufacturer_blankName_throwsException(){

        //Arrange
        String manufacturer = "  ";

        //Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pcPartService.getAllByManufacturer(manufacturer);
        });

        assertEquals("Manufacturer cannot be null or empty.", exception.getMessage());

        //Verify
        verify(pcPartRepository, never()).findAllByManufacturerIgnoreCase(anyString());

    }

    @Test
    void getAllByManufacturer_nullName_throwsException(){

        //Arrange
        String manufacturer = null;

        //Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            pcPartService.getAllByManufacturer(manufacturer);
        });

        assertEquals("Manufacturer cannot be null or empty.", exception.getMessage());

        //Verify
        verifyNoInteractions(pcPartRepository);
        //verify(pcPartRepository, never()).findAllByManufacturerIgnoreCase(isNull());

    }

}
