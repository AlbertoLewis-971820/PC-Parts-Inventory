package com.albertolewis.pcpartsinventory.service;

import com.albertolewis.pcpartsinventory.dto.RestockRequest;
import com.albertolewis.pcpartsinventory.model.PartsCategory;
import com.albertolewis.pcpartsinventory.model.PcPart;
import com.albertolewis.pcpartsinventory.repository.PcPartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PcPartService {

    private final PcPartRepository pcPartRepository;

    public PcPartService(PcPartRepository pcPartRepository) {
        this.pcPartRepository = pcPartRepository;
    }

    //Return all pc parts
    public List<PcPart> getAllPcParts() {
        return pcPartRepository.findAll();
    }

    //Return a pc part by id
    public Optional<PcPart> getPcPartById(Long id) {
        return pcPartRepository.findById(id);
    }

    //Accept a Pc part sae and return the saved Pc part
    public PcPart savePcPart(PcPart pcPart) {
        return pcPartRepository.save(pcPart);
    }

    //Delete a pc part by id
    public boolean deletePcPart(Long id) {
        if (pcPartRepository.existsById(id)) {
            pcPartRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
    //Find all pc parts below threshold
    public List<PcPart> getAllByQuantityLessThan(int threshold){
        if(threshold < 0){
            throw new IllegalArgumentException("Threshold must be a non-negative integer.");
        }
        return pcPartRepository.findAllByQuantityLessThan(threshold);
    }

    //Find all pc parts by manufacturer name
    public List<PcPart> getAllByManufacturer(String name) {
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("Manufacturer cannot be null or empty.");
        }
        return pcPartRepository.findAllByManufacturerIgnoreCase(name);
    }

    //Find all pc parts by category
    public List<PcPart> getAllByCategory(String name){

        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }
        String normalizedName = name.strip().toUpperCase();
        PartsCategory category;
        try{
            category = PartsCategory.valueOf(normalizedName);
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + normalizedName);
        }
        return pcPartRepository.findAllByCategory(category);
    }

    //Restock a pc part
    public PcPart restockPcPart(Long id, RestockRequest restockRequest){

        PcPart pcPart =  pcPartRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PcPart with id " + id + " not found."));

        Integer newQuantity = restockRequest.getQuantity() +  pcPart.getQuantity();
        pcPart.setQuantity(newQuantity);
        return pcPartRepository.save(pcPart);
    }


    //Update a pc part by id
    public Optional<PcPart> updatePcPart(Long id, PcPart pcPart) {
        return pcPartRepository.findById(id)
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
