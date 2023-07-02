CREATE SCHEMA IF NOT EXISTS cms;

CREATE TABLE IF NOT EXISTS cms.categories
(   
    id serial primary key,
    name varchar ( 255 )
);

CREATE TABLE IF NOT EXISTS cms.cars
(   
    id serial primary key,
    object_id varchar ( 255 ),
    make varchar ( 255 ),
    year int,
    model varchar ( 255 ),
    category_id int,
    FOREIGN KEY (category_id)
    REFERENCES cms.categories (id)
);
