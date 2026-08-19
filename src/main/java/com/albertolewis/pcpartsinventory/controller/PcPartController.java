package com.albertolewis.pcpartsinventory.controller;

import com.albertolewis.pcpartsinventory.model.PcPart;
import com.albertolewis.pcpartsinventory.service.PcPartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pcparts")
public class PcPartController {

    private final PcPartService pcPartService;

    public PcPartController(PcPartService pcPartService) {
        this.pcPartService = pcPartService;
    }

    @GetMapping
    public List<PcPart> getAllPcParts() {
        return pcPartService.getAllPcParts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PcPart> getPcPartById(@PathVariable Long id) {
        return pcPartService.getPcPartById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePcPart(@PathVariable Long id) {

        boolean deleted = pcPartService.deletePcPart(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PcPart> updatePcPart(@PathVariable Long id, @Valid @RequestBody PcPart pcPart) {
        Optional<PcPart> updatedPcPart = pcPartService.updatePcPart(id, pcPart);
        return updatedPcPart
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PcPart addPcPart(@Valid @RequestBody PcPart pcPart) {
        return pcPartService.savePcPart(pcPart);
    }

}
