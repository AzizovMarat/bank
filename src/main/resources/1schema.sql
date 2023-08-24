create table accounts
(
    id                  int not null auto_increment,
    name                varchar(50) not null,
    pin                 smallint(4) not null,
    primary key (id)
);