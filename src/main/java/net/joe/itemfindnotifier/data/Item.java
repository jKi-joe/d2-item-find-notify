package net.joe.itemfindnotifier.data;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Item {

    private String id;

    private String name;

    private Timestamp timestamp;

}
