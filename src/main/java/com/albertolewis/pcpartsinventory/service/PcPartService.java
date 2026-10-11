package com.albertolewis.pcpartsinventory.service;

import com.albertolewis.pcpartsinventory.dto.CreatePcPartRequest;
import com.albertolewis.pcpartsinventory.dto.PcPartResponse;
import com.albertolewis.pcpartsinventory.dto.RestockRequest;
import com.albertolewis.pcpartsinventory.exception.PcPartNotFoundException;
import com.albertolewis.pcpartsinventory.model.PartsCategory;
import com.albertolewis.pcpartsinventory.model.PcPart;
import com.albertolewis.pcpartsinventory.model.User;
import com.albertolewis.pcpartsinventory.repository.PcPartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class PcPartService {

    private final PcPartRepository pcPartRepository;

    private final UserService userService;

    public PcPartService(PcPartRepository pcPartRepository, UserService userService) {
        this.pcPartRepository = pcPartRepository;
        this.userService = userService;
    }

    //Response method
    public PcPartResponse toResponse(PcPart pcPart) {
        return new PcPartResponse(
                pcPart.getId(),
                pcPart.getName(),
                pcPart.getCategory(),
                pcPart.getManufacturer(),
                pcPart.getPrice(),
                pcPart.getQuantity()
        );
    }

    //Return all pc parts
    public List<PcPart> getAllPcParts(String username) {
        User user = userService.getUserByUsername(username);

        return pcPartRepository.findAllByOwner_Id(user.getId());
    }

    //Return a pc part by id
    public Optional<PcPart> getPcPartById(Long id, String username) {
        User user = userService.getUserByUsername(username);

        return pcPartRepository.findByIdAndOwner_Id(id, user.getId());
    }

    // Create a new pc part without id and owner, and save it to the database
    public PcPart savePcPart(CreatePcPartRequest pcPartRequest, String username) {

        User user = userService.getUserByUsername(username);
        PcPart pcPart = new PcPart();
        pcPart.setName(pcPartRequest.getName());
        pcPart.setCategory(pcPartRequest.getCategory());
        pcPart.setManufacturer(pcPartRequest.getManufacturer());
        pcPart.setPrice(pcPartRequest.getPrice());
        pcPart.setQuantity(pcPartRequest.getQuantity());
        pcPart.setOwner(user);

        return pcPartRepository.save(pcPart);
    }

    //Delete a pc part by id
    public boolean deletePcPart(Long id, String username) {
        User user = userService.getUserByUsername(username);
        Optional<PcPart> pcPart = pcPartRepository.findByIdAndOwner_Id(id, user.getId());
        if (pcPart.isPresent()) {
            pcPartRepository.delete(pcPart.get());
            return true;
        } else {
            return false;
        }
    }

    //Find all pc parts below threshold
    public List<PcPart> getAllByQuantityLessThan(Integer threshold, String username) {
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must be a non-negative integer.");
        }
        User user = userService.getUserByUsername(username);
        return pcPartRepository.findAllByOwner_IdAndQuantityLessThan(user.getId(), threshold);
    }

    //Find all pc parts by manufacturer name
    public List<PcPart> getAllByManufacturer(String name, String username) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Manufacturer cannot be null or empty.");
        }
        User user = userService.getUserByUsername(username);

        return pcPartRepository.findAllByOwner_IdAndManufacturerIgnoreCase(user.getId(), name);
    }

    //Find all pc parts by category
    public List<PcPart> getAllByCategory(String name, String username) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }
        User user = userService.getUserByUsername(username);

        String normalizedName = name.strip().toUpperCase(Locale.ROOT);
        PartsCategory category;
        try {
            category = PartsCategory.valueOf(normalizedName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + normalizedName);
        }
        return pcPartRepository.findAllByOwner_IdAndCategory(user.getId(), category);
    }

    //Restock a pc part
    public PcPart restockPcPart(Long id, RestockRequest restockRequest, String username) {
        User owner = userService.getUserByUsername(username);

        PcPart pcPart = pcPartRepository.findByIdAndOwner_Id(id, owner.getId())
                .orElseThrow(() -> new PcPartNotFoundException("Pc Part id: " + id + " not found"));

        Integer newQuantity = restockRequest.getQuantity() + pcPart.getQuantity();
        pcPart.setQuantity(newQuantity);
        return pcPartRepository.save(pcPart);
    }


    //Update a pc part by id
    public Optional<PcPart> updatePcPart(Long id, PcPart pcPart, String username) {
        User owner = userService.getUserByUsername(username);
        return pcPartRepository.findByIdAndOwner_Id(id, owner.getId())
                .map(existingPcPart -> {
                    existingPcPart.setName(pcPart.getName());
                    existingPcPart.setCategory(pcPart.getCategory());
                    existingPcPart.setManufacturer(pcPart.getManufacturer());
                    existingPcPart.setPrice(pcPart.getPrice());
                    existingPcPart.setQuantity(pcPart.getQuantity());
                    return pcPartRepository.save(existingPcPart);
                });
    }

}
