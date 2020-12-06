CREATE TABLE IF NOT EXISTS messages
(
    id integer auto_increment,
    username character varying(255)  NOT NULL,
    text character varying(255)  NOT NULL,
    created_at timestamp without time zone DEFAULT now()
);

 CREATE TABLE if not EXISTS users(
   id  integer GENERATED ALWAYS AS IDENTITY,
   username VARCHAR(255) not null UNIQUE,
   password varchar(255) not null,

   PRIMARY KEY (id)
   );

 CREATE TABLE if not EXISTS SeriesTemporelles(
   id integer GENERATED ALWAYS AS IDENTITY,
   titre VARCHAR(255) not null,
   description varchar(255) not null,
   owner integer,

   PRIMARY KEY (id),
   FOREIGN KEY (owner) REFERENCES users(id)
   );


 CREATE TABLE if not EXISTS Partages(
   id integer GENERATED ALWAYS AS IDENTITY,
   id_user int,
   id_SerieTemporelle int,
   type char(1) not null,

   PRIMARY KEY (id),
   FOREIGN KEY (id_user) REFERENCES users(id),
   FOREIGN KEY (id_SerieTemporelle) REFERENCES SeriesTemporelles(id)
   );


 CREATE TABLE if not EXISTS Evenements(
   id integer GENERATED ALWAYS AS IDENTITY,
   id_SerieTemporelle int,
   date_Evenement DATE not null,
   valeur float not null,
   commentaire VARCHAR(255),

   PRIMARY KEY (id),
   FOREIGN KEY (id_SerieTemporelle) REFERENCES SeriesTemporelles(id)
   );

  CREATE TABLE if not EXISTS Tags(
  id integer GENERATED ALWAYS AS IDENTITY,
  name varchar(255) UNIQUE not null,
  id_Evenement int,

  PRIMARY KEY (id),
  FOREIGN KEY (id_Evenement) REFERENCES Evenements(id)
  );


