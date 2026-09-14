package com.albertolewis.pcpartsinventory.controller;

import com.albertolewis.pcpartsinventory.dto.RestockRequest;
import com.albertolewis.pcpartsinventory.model.PartsCategory;
import com.albertolewis.pcpartsinventory.model.PcPart;
import com.albertolewis.pcpartsinventory.service.PcPartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pcparts")
@CrossOrigin(origins = "http://localhost:4200")
public class PcPartController {

    private final PcPartService pcPartService;

    public PcPartController(PcPartService pcPartService) {
        this.pcPartService = pcPartService;
    }

    //Show all pc parts
    @GetMapping
    public List<PcPart> getAllPcParts() {
        return pcPartService.getAllPcParts();
    }

    //Get a pc part by id
    @GetMapping("/{id}")
    public ResponseEntity<PcPart> getPcPartById(@PathVariable Long id) {
        return pcPartService.getPcPartById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Delete a pc part
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePcPart(@PathVariable Long id) {

        boolean deleted = pcPartService.deletePcPart(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Return a list of low stock pc parts
    @GetMapping("/low-stock")
    public List<PcPart> getAllByQuantityLessThan(@RequestParam Integer threshold) {
        return pcPartService.getAllByQuantityLessThan(threshold);
    }

    //Find all pc parts by manufacturer name
    @GetMapping("/manufacturer")
    public List<PcPart> getAllByManufacturer(@RequestParam String name) {
        return pcPartService.getAllByManufacturer(name);
    }

    //Find all pc parts by category
    @GetMapping("/category")
    public List<PcPart> getAllByCategory(@RequestParam String name){
        return pcPartService.getAllByCategory(name);
    }


    //Update a pc part
    @PutMapping("/{id}")
    public ResponseEntity<PcPart> updatePcPart(@PathVariable Long id, @Valid @RequestBody PcPart pcPart) {
        Optional<PcPart> updatedPcPart = pcPartService.updatePcPart(id, pcPart);
        return updatedPcPart
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Update the quantity of a pc part
    @PatchMapping("/{id}/restock")
    public ResponseEntity<PcPart> restockPcPart(@PathVariable Long id, @Valid @RequestBody RestockRequest restockRequest) {

        PcPart updatedPcPart = pcPartService.restockPcPart(id, restockRequest);
        return ResponseEntity.ok(updatedPcPart);
    }

    //Add a pc part
    @PostMapping
    public PcPart addPcPart(@Valid @RequestBody PcPart pcPart) {
        return pcPartService.savePcPart(pcPart);
    }

}
