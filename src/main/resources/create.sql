CREATE TABLE if not EXISTS User(
  id integer auto_increment,
  username VARCHAR(255) not null UNIQUE,
  password varchar(255) not null,

  PRIMARY KEY (id),
  );

CREATE TABLE if not EXISTS SerieTemporelle(
  id integer auto_increment,
  titre VARCHAR(255) not null,
  description varchar(255) not null,
  owner Varchar(255),

  PRIMARY KEY (id),
  FOREIGN KEY (owner) REFERENCES User(id)
  );

CREATE TABLE if not EXISTS Partage(
  id integer auto_increment,
  user VARCHAR(255),
  id_SerieTemporelle varchar(255),
  type char(1) not null,

  PRIMARY KEY (id),
  FOREIGN KEY (user) REFERENCES User(id),
  FOREIGN KEY (id_SerieTemporelle) REFERENCES SerieTemporelle(id)
  );

CREATE TABLE if not EXISTS Evenement(
  id integer auto_increment,
  id_SerieTemporelle int,
  date_Evenement DATE not null,
  valeur float not null,
  commentaire VARCHAR(255),

  PRIMARY KEY (id),
  FOREIGN KEY (id_SerieTemporelle) REFERENCES SerieTemporelle(id)
  );

CREATE TABLE if not EXISTS Tag(
  id integer SERIAL,
  name varchar(255) UNIQUE not null,
  id_Evenement int,

  PRIMARY KEY (name),
  FOREIGN KEY (id_Evenement) REFERENCES Evenement(id)
  );


