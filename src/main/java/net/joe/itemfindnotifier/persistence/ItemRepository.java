package net.joe.itemfindnotifier.persistence;

import net.joe.itemfindnotifier.persistence.data.ItemDto;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<ItemDto, String> {

    List<ItemDto> findAllByTimestampAndFoundBy(Timestamp timestamp, String foundBy);

}
