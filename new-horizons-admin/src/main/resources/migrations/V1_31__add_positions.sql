CREATE TABLE jobposition (
  id bigint NOT NULL,
  name character varying(255),
  company_id bigint,
  employee_id bigint,

  CONSTRAINT position_pkey PRIMARY KEY (id),
  CONSTRAINT company_fkey FOREIGN KEY (company_id)
      REFERENCES public.juridicalclient (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT employee_fkey FOREIGN KEY (employee_id)
      REFERENCES public.naturalclient (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT const unique (company_id, employee_id, name)
);