insert into guest(id, name) values(null, 'Roger Federer');
insert into guest(id, name) values(null, 'Rafael Nadal');

insert into tennis_court(id, name) values(null, 'Roland Garros - Court Philippe-Chatrier');
insert into tennis_court(id, name) values(null, 'US Open');
insert into tennis_court(id, name) values(null, 'Madrid Open');

insert
    into
        schedule
        (id, start_date_time, end_date_time, tennis_court_id)
    values
        (null, '2022-04-20T20:00:00.0', '2022-04-20T21:00:00.0', 1);

insert
    into
         schedule
        (id, start_date_time, end_date_time, tennis_court_id)
    values
          (null, '2022-05-20T20:00:00.0', '2022-05-20T21:00:00.0', 1);


insert
    into
         schedule
        (id, start_date_time, end_date_time, tennis_court_id)
    values
        (null, '2022-06-20T20:00:00.0', '2022-06-20T21:00:00.0', 2);