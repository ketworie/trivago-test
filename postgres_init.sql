-- Adminer 4.8.1 PostgreSQL 15.4 dump

\connect "trivago";

DROP TABLE IF EXISTS "accommodation";
DROP SEQUENCE IF EXISTS accommodation_id_seq;
CREATE SEQUENCE accommodation_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

CREATE TABLE "public"."accommodation" (
                                          "id" bigint DEFAULT nextval('accommodation_id_seq') NOT NULL,
                                          "name" text NOT NULL,
                                          "rating" integer NOT NULL,
                                          "category" text NOT NULL,
                                          "location_id" bigint NOT NULL,
                                          "image" text NOT NULL,
                                          "reputation" integer NOT NULL,
                                          "price" integer NOT NULL,
                                          "availability" integer NOT NULL,
                                          "version" bigint NOT NULL,
                                          "hotelier_id" bigint NOT NULL,
                                          CONSTRAINT "accommodation_pkey" PRIMARY KEY ("id")
) WITH (oids = false);


DROP TABLE IF EXISTS "location";
DROP SEQUENCE IF EXISTS location_id_seq;
CREATE SEQUENCE location_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

CREATE TABLE "public"."location" (
                                     "id" bigint DEFAULT nextval('location_id_seq') NOT NULL,
                                     "city" text NOT NULL,
                                     "state" text NOT NULL,
                                     "country" text NOT NULL,
                                     "zip_code" integer NOT NULL,
                                     "address" text NOT NULL,
                                     CONSTRAINT "location_pkey" PRIMARY KEY ("id")
) WITH (oids = false);


ALTER TABLE ONLY "public"."accommodation" ADD CONSTRAINT "accommodation_location_id_fkey" FOREIGN KEY (location_id) REFERENCES location(id) NOT DEFERRABLE;

-- 2023-08-17 19:44:03.820716+00