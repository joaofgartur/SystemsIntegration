create table countries
(
    country_id   integer default nextval('countries_seq'::regclass) not null
        primary key,
    country_name varchar(40)                                        not null
);

alter table countries
    owner to postgres;

INSERT INTO public.countries (country_id, country_name) VALUES (1, 'Germany');
INSERT INTO public.countries (country_id, country_name) VALUES (2, 'Poland');
INSERT INTO public.countries (country_id, country_name) VALUES (3, 'United Kingdom');
INSERT INTO public.countries (country_id, country_name) VALUES (4, 'Italy');
INSERT INTO public.countries (country_id, country_name) VALUES (5, 'Portugal');
