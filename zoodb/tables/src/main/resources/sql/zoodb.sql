create type mammalia_order as enum (
    'rodentia',
    'chiroptera',
    'soricomorpha',
    'primates',
    'carnivora',
    'artiodactyla',
    'diprotodontia',
    'lagomorpha',
    'didelphimorphia',
    'cetacea',
    'dasyuromorphia',
    'afrosoricida',
    'erinaceomorpha',
    'cingulata',
    'peramelemorphia',
    'scandentia',
    'perissodactyla',
    'macroscelidea',
    'pilosa',
    'monotremata',
    'proboscidea'
    );

create table animals
(
    id             serial         not null primary key,
    name           text           not null,
    mammalia_order mammalia_order not null
);

insert into animals(name, mammalia_order)
select upper(substr(md5((row_number() over (partition by e.enumtypid))::text), 10)), e.enumlabel::mammalia_order
from pg_enum e,
     pg_type t
where e.enumtypid = t.oid
offset floor(random() * 10) limit 10;