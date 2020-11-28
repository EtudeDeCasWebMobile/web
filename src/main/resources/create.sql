CREATE TABLE if not EXISTS User(
  username VARCHAR(255),
  password varchar(255) not null,

  PRIMARY KEY (username),
  );

CREATE TABLE if not EXISTS SerieTemporelle(
  id AUTOINCREMENT,
  titre VARCHAR(255) not null,
  description varchar(255) not null,
  owner Varchar(255),

  PRIMARY KEY (id),
  FOREIGN KEY (owner) REFERENCES User(username)
  );

CREATE TABLE if not EXISTS Partage(
  id AUTOINCREMENT,
  user VARCHAR(255),
  id_SerieTemporelle varchar(255),
  type char(1) not null,

  PRIMARY KEY (id),
  FOREIGN KEY (user) REFERENCES User(username),
  FOREIGN KEY (id_SerieTemporelle) REFERENCES SerieTemporelle(id)
  );

CREATE TABLE if not EXISTS Evenement(
  id AUTOINCREMENT,
  id_SerieTemporelle int,
  date_Evenement DATE not null,
  valeur float not null,
  commentaire VARCHAR(255),

  PRIMARY KEY (id),
  FOREIGN KEY (id_SerieTemporelle) REFERENCES SerieTemporelle(id)
  );

CREATE TABLE if not EXISTS Tag(
  name varchar(255),
  id_Evenement int,

  PRIMARY KEY (name),
  FOREIGN KEY (id_Evenement) REFERENCES Evenement(id)
  );


