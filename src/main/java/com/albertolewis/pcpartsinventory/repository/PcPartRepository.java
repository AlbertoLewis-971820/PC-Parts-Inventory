package com.albertolewis.pcpartsinventory.repository;

import com.albertolewis.pcpartsinventory.model.PcPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PcPartRepository extends JpaRepository<PcPart, Long> {

    //Grab pc parts that are below a certain threshold
    List<PcPart> findAllByQuantityLessThan(int threshold);

    //Grab all pc parts by manufacturer name
    List<PcPart> findAllByManufacturerIgnoreCase(String manufacturer);

}
