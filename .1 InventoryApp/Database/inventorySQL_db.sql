START TRANSACTION;


DROP TABLE IF EXISTS latest_inventory;
DROP TABLE IF EXISTS item;


CREATE TABLE item (
    item_id serial,
    item_name varchar,
    target_quantity int NOT NULL,
    price_per_item numeric NOT NULL,
    CONSTRAINT pk_item PRIMARY KEY (item_id)
);

CREATE TABLE latest_inventory (
    item_id int NOT NULL,
    current_quantity int NOT NULL,
    CONSTRAINT pk_latest_inventory PRIMARY KEY (item_id, current_quantity),
    CONSTRAINT fk_latest_inventory_item FOREIGN KEY (item_id) REFERENCES item (item_id)
);

COMMIT;

SELECT * FROM item;