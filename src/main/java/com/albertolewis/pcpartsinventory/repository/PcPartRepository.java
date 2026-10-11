package com.albertolewis.pcpartsinventory.repository;

import com.albertolewis.pcpartsinventory.model.PartsCategory;
import com.albertolewis.pcpartsinventory.model.PcPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PcPartRepository extends JpaRepository<PcPart, Long> {

    //Grab pc parts that are below a certain threshold
    List<PcPart> findAllByOwner_IdAndQuantityLessThan(Long ownerId, Integer threshold);

    //Grab all pc parts by manufacturer name
    List<PcPart> findAllByOwner_IdAndManufacturerIgnoreCase(Long ownerId, String manufacturer);

    //Grab all pc parts by category
    List<PcPart> findAllByOwner_IdAndCategory(Long ownerId, PartsCategory category);

    //Grab a pc part by id and owner id
    Optional<PcPart> findByIdAndOwner_Id(Long id, Long ownerId);

    //Grab all pc parts by owner id
    List<PcPart> findAllByOwner_Id(Long ownerId);

}
