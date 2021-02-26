CREATE TABLE if not EXISTS users(
    id  integer GENERATED ALWAYS AS IDENTITY,
    email VARCHAR(255) not null UNIQUE,
    password varchar(255) not null,
    position int,

    PRIMARY KEY (id)
);
--
CREATE TABLE if not EXISTS locations(
    id  integer GENERATED ALWAYS AS IDENTITY,
    title VARCHAR(255) not null,
    description varchar(255) not null,
    owner_id varchar(255) ,

    PRIMARY KEY (id)
);
--
CREATE TABLE if not EXISTS collections(
    id  integer GENERATED ALWAYS AS IDENTITY,
    tag VARCHAR(255) not null,
    owner_id int,
    PRIMARY KEY (id)
);

ALTER TABLE users
ADD CONSTRAINT fk_position
FOREIGN KEY (position) REFERENCES locations(id) ON DELETE SET NULL;

ALTER TABLE locations
ADD CONSTRAINT fk_location_owner
FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE SET NULL;

ALTER TABLE collections
ADD CONSTRAINT fk_collection_owner
FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE SET NULL;

--
--CREATE TABLE if not EXISTS location_collection(
--    id  integer GENERATED ALWAYS AS IDENTITY,
--    location_id VARCHAR(255) not null,
--    collection_id varchar(255) not null,
--
--    PRIMARY KEY (id),
--    FOREIGN KEY (location_id) REFERENCES locations(id),
--    FOREIGN KEY (collection_id) REFERENCES collections(id)
--);
--
--CREATE TABLE if not EXISTS sharings(
--    id  integer GENERATED ALWAYS AS IDENTITY,
--    user_id VARCHAR(255) not null,
--    collection_id varchar(255) not null,
--
--    PRIMARY KEY (id),
--    FOREIGN KEY (location_id) REFERENCES locations(id),
--    FOREIGN KEY (collection_id) REFERENCES collections(id)
--);
--
--
-- CREATE TABLE if not EXISTS SeriesTemporelles(
--   id integer GENERATED ALWAYS AS IDENTITY,
--   titre VARCHAR(255) not null,
--   description varchar(255) not null,
--   owner int,
--
--   PRIMARY KEY (id),
--   FOREIGN KEY (owner) REFERENCES users(id)
--   );
--
--
-- CREATE TABLE if not EXISTS Partages(
--   id integer GENERATED ALWAYS AS IDENTITY,
--   id_user int,
--   id_SerieTemporelle int,
--   type char(1) not null,
--
--   PRIMARY KEY (id),
--   FOREIGN KEY (id_user) REFERENCES users(id),
--   FOREIGN KEY (id_SerieTemporelle) REFERENCES SeriesTemporelles(id)
--   );
--
--
-- CREATE TABLE if not EXISTS Evenements(
--   id integer GENERATED ALWAYS AS IDENTITY,
--   id_SerieTemporelle int,
--   date_Evenement DATE not null,
--   valeur float not null,
--   commentaire VARCHAR(255),
--
--   PRIMARY KEY (id),
--   FOREIGN KEY (id_SerieTemporelle) REFERENCES SeriesTemporelles(id)
--   );
--
--  CREATE TABLE if not EXISTS Tags(
--  id integer GENERATED ALWAYS AS IDENTITY,
--  name varchar(255) UNIQUE not null,
--  id_Evenement int,
--
--  PRIMARY KEY (id),
--  FOREIGN KEY (id_Evenement) REFERENCES Evenements(id)
--  );
