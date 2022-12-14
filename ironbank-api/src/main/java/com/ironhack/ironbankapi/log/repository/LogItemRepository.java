package com.ironhack.ironbankapi.log.repository;

import com.ironhack.ironbankapi.log.model.LogItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogItemRepository extends JpaRepository<LogItem, UUID> {

}
