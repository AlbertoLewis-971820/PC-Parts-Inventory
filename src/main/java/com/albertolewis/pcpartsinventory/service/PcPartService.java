package com.albertolewis.pcpartsinventory.service;

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
