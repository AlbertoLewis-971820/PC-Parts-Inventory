package com.albertolewis.pcpartsinventory.repository;

import com.albertolewis.pcpartsinventory.model.PcPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PcPartRepository extends JpaRepository<PcPart, Long> {
}
