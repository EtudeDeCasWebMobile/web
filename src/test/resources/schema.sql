CREATE TABLE IF NOT EXISTS messages
(
    id integer auto_increment,
    username character varying(255)  NOT NULL,
    text character varying(255)  NOT NULL,
    created_at timestamp without time zone DEFAULT now()
);