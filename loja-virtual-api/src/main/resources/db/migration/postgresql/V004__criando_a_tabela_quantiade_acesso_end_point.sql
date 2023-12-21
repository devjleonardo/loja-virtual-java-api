CREATE TABLE quantidade_acesso_end_point(
  end_point character varying,
  quantidade_acesso integer
);

ALTER TABLE quantidade_acesso_end_point ADD CONSTRAINT end_point_unique UNIQUE (end_point);