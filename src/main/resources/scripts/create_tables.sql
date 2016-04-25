CREATE DATABASE structuredproducts OWNER postgres;

\c structuredproducts;

CREATE SCHEMA INSTRUMENT;

CREATE TABLE INSTRUMENT.PRODUCT_TYPE (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL
);


CREATE TABLE INSTRUMENT.UNDERLAYING_TYPE (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL
);

CREATE TABLE INSTRUMENT.UNDERLAYING (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  officialName TEXT,
  type INTEGER REFERENCES INSTRUMENT.UNDERLAYING_TYPE (id)
);

CREATE TABLE INSTRUMENT.ISSUER (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL
);

CREATE TABLE INSTRUMENT.STRATEGY (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL
);

CREATE TABLE INSTRUMENT.LEGAL_TYPE (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL
);

CREATE TABLE INSTRUMENT.PAYOFF (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL
);

CREATE TABLE INSTRUMENT.RISKS (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL
);

CREATE TABLE INSTRUMENT.CURRENCY (
  id SERIAL PRIMARY KEY,
  name VARCHAR (3) NOT NULL
);

CREATE TABLE INSTRUMENT.PAYMENT_PERIODICITY (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL
);

CREATE TABLE INSTRUMENT.BROKER (
  id SERIAL PRIMARY KEY,
  name TEXT UNIQUE,
  logo TEXT
);

CREATE TABLE INSTRUMENT.PRODUCT (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  productType INTEGER REFERENCES INSTRUMENT.PRODUCT_TYPE (id) NOT NULL ,
  minTerm smallint NOT NULL,
  maxTerm smallInt NOT NULL,
  minInvest integer NOT NULL,
  maxinvest integer NOT NULL,
  broker INTEGER REFERENCES INSTRUMENT.BROKER (id) NOT NULL,
  return REAL NOT NULL,
  strategy INTEGER REFERENCES INSTRUMENT.STRATEGY (id),
  legalType INTEGER REFERENCES INSTRUMENT.LEGAL_TYPE (id),
  payoff INTEGER REFERENCES INSTRUMENT.PAYOFF (id),
  risks INTEGER REFERENCES INSTRUMENT.RISKS (id),
  currency INTEGER REFERENCES INSTRUMENT.CURRENCY (id),
  paymentPeriodicity INTEGER REFERENCES INSTRUMENT.PAYMENT_PERIODICITY (id),
  description TEXT
);

CREATE TABLE INSTRUMENT.TOP_PRODUCT (
  id SERIAL PRIMARY KEY,
  product INTEGER REFERENCES INSTRUMENT.PRODUCT (id),
  time TEXT NOT NULL
);


CREATE TABLE INSTRUMENT.INVEST_IDEA (
  id SERIAL PRIMARY KEY,
  title TEXT NOT NULL,
  addDate DATE,
  content TEXT NOT NULL,
  mainPage BOOLEAN NOT NULL,
  broker INTEGER REFERENCES INSTRUMENT.BROKER(id)
);

CREATE TABLE INSTRUMENT.PRODUCT_PARAMS(
  id SERIAL PRIMARY KEY,
  product INTEGER REFERENCES INSTRUMENT.PRODUCT(id) NOT NULL,
  chart TEXT,
  img TEXT,
  forecast TEXT
);

CREATE TABLE INSTRUMENT.UNDERLAYINGS (
  product INTEGER REFERENCES INSTRUMENT.PRODUCT(id) NOT NULL,
  underlaying INTEGER REFERENCES INSTRUMENT.UNDERLAYING(id) NOT NULL
);

ALTER TABLE INSTRUMENT.PRODUCT_PARAMS ADD COLUMN showChart boolean;