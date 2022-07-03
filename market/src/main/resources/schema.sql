create table if not exists transaction (
    id bigserial primary key not null,
    broker_id bigint not null,
    market_id bigint not null,
    instrument varchar(100) not null,
    quantity int not null,
    status varchar(20) not null,
    type int not null,
    order_id varchar(10) not null
);

create table if not exists stocks(
    id bigserial primary key not null,
    name varchar(100) not null,
    quantity int not null,
    price float not null
);