--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.0
-- Dumped by pg_dump version 9.5.0

-- Started on 2023-12-18 02:18:19

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2369 (class 1262 OID 49781)
-- Name: loja-virtual-teste; Type: DATABASE; Schema: -; Owner: postgres
--

-- CREATE DATABASE "loja-virtual-teste" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Portuguese_Brazil.1252' LC_CTYPE = 'Portuguese_Brazil.1252';


ALTER DATABASE "loja-virtual-teste" OWNER TO postgres;

-- \connect "loja-virtual-teste"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 220 (class 3079 OID 12355)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2372 (class 0 OID 0)
-- Dependencies: 220
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 221 (class 1255 OID 50160)
-- Name: validarchavepessoa(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION validarchavepessoa() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE existe INTEGER;

BEGIN
  existe = (SELECT COUNT(1) FROM pessoa_fisica WHERE id = NEW.pessoa_id);
  
  IF (existe <= 0) THEN
    existe = (SELECT COUNT(1) FROM pessoa_juridica WHERE id = NEW.pessoa_id);
  END IF;
  
  IF (existe <= 0) THEN
    RAISE EXCEPTION 'Não foi econtrado o ID ou a PK da pessoa para realizar a associação';
  END IF;
RETURN NEW;
END;
$$;


ALTER FUNCTION public.validarchavepessoa() OWNER TO postgres;

--
-- TOC entry 222 (class 1255 OID 50173)
-- Name: validarchavepessoafornecedora(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION validarchavepessoafornecedora() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE existe INTEGER;

BEGIN
  existe = (SELECT COUNT(1) FROM pessoa_fisica WHERE id = NEW.pessoa_fornecedora_id);
  
  IF (existe <= 0) THEN
    existe = (SELECT COUNT(1) FROM pessoa_juridica WHERE id = NEW.pessoa_fornecedora_id);
  END IF;
  
  IF (existe <= 0) THEN
    RAISE EXCEPTION 'Não foi econtrado o ID ou a PK da pessoa para realizar a associação';
  END IF;
RETURN NEW;
END;
$$;


ALTER FUNCTION public.validarchavepessoafornecedora() OWNER TO postgres;

--
-- TOC entry 235 (class 1255 OID 50176)
-- Name: validarchavepessoapagadora(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION validarchavepessoapagadora() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE existe INTEGER;

BEGIN
  existe = (SELECT COUNT(1) FROM pessoa_fisica WHERE id = NEW.pessoa_pagadora_id);
  
  IF (existe <= 0) THEN
    existe = (SELECT COUNT(1) FROM pessoa_juridica WHERE id = NEW.pessoa_pagadora_id);
  END IF;
  
  IF (existe <= 0) THEN
    RAISE EXCEPTION 'Não foi econtrado o ID ou a PK da pessoa para realizar a associação';
  END IF;
RETURN NEW;
END;
$$;


ALTER FUNCTION public.validarchavepessoapagadora() OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 180 (class 1259 OID 49782)
-- Name: acesso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE acesso (
    id bigint NOT NULL,
    nome character varying(255) NOT NULL
);


ALTER TABLE acesso OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 49787)
-- Name: avaliacao_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE avaliacao_produto (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL,
    nota integer NOT NULL,
    empresa_id bigint,
    pessoa_id bigint NOT NULL,
    produto_id bigint NOT NULL
);


ALTER TABLE avaliacao_produto OWNER TO postgres;

--
-- TOC entry 182 (class 1259 OID 49792)
-- Name: categoria_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE categoria_produto (
    id bigint NOT NULL,
    nome character varying(255) NOT NULL,
    empresa_id bigint
);


ALTER TABLE categoria_produto OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 49797)
-- Name: conta_pagar; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE conta_pagar (
    id bigint NOT NULL,
    data_pagamento date,
    data_vencimento date NOT NULL,
    descricao character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    valor_desconto numeric(19,2),
    valor_total numeric(19,2) NOT NULL,
    empresa_id bigint,
    pessoa_fornecedora_id bigint NOT NULL,
    pessoa_pagadora_id bigint NOT NULL
);


ALTER TABLE conta_pagar OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 49805)
-- Name: conta_receber; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE conta_receber (
    id bigint NOT NULL,
    data_pagamento date,
    data_vencimento date NOT NULL,
    descricao character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    valor_desconto numeric(19,2),
    valor_total numeric(19,2) NOT NULL,
    empresa_id bigint,
    pessoa_id bigint NOT NULL
);


ALTER TABLE conta_receber OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 49813)
-- Name: cupom_desconto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE cupom_desconto (
    id bigint NOT NULL,
    codigo_descricao character varying(255) NOT NULL,
    data_validade date NOT NULL,
    valor_em_porcentagem numeric(19,2),
    valor_em_reais numeric(19,2),
    empresa_id bigint
);


ALTER TABLE cupom_desconto OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 49818)
-- Name: endereco; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE endereco (
    id bigint NOT NULL,
    bairro character varying(255) NOT NULL,
    cep character varying(255) NOT NULL,
    cidade character varying(255) NOT NULL,
    complemento character varying(255),
    logradouro character varying(255) NOT NULL,
    numero character varying(255) NOT NULL,
    tipo_endereco character varying(255) NOT NULL,
    uf character varying(255) NOT NULL,
    empresa_id bigint,
    pessoa_id bigint NOT NULL
);


ALTER TABLE endereco OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 49826)
-- Name: forma_pagamento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE forma_pagamento (
    id bigint NOT NULL,
    nome character varying(255) NOT NULL,
    empresa_id bigint
);


ALTER TABLE forma_pagamento OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 49831)
-- Name: imagem_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE imagem_produto (
    id bigint NOT NULL,
    imagem_miniatura text NOT NULL,
    imagem_original text NOT NULL,
    empresa_id bigint,
    produto_id bigint NOT NULL
);


ALTER TABLE imagem_produto OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 49839)
-- Name: item_venda_loja; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE item_venda_loja (
    id bigint NOT NULL,
    quantidade double precision NOT NULL,
    empresa_id bigint,
    produto_id bigint NOT NULL,
    venda_compra_loja_virtual_id bigint NOT NULL
);


ALTER TABLE item_venda_loja OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 49844)
-- Name: marca_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE marca_produto (
    id bigint NOT NULL,
    nome character varying(255) NOT NULL,
    empresa_id bigint
);


ALTER TABLE marca_produto OWNER TO postgres;

--
-- TOC entry 191 (class 1259 OID 49849)
-- Name: nota_fiscal_compra; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE nota_fiscal_compra (
    id bigint NOT NULL,
    data_compra date NOT NULL,
    descricao_observacao character varying(255),
    numero character varying(255) NOT NULL,
    serie character varying(255) NOT NULL,
    valor_desconto numeric(19,2),
    valor_icms numeric(19,2) NOT NULL,
    valor_total numeric(19,2) NOT NULL,
    conta_pagar_id bigint NOT NULL,
    empresa_id bigint,
    pessoa_id bigint NOT NULL
);


ALTER TABLE nota_fiscal_compra OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 49857)
-- Name: nota_fiscal_venda; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE nota_fiscal_venda (
    id bigint NOT NULL,
    numero character varying(255) NOT NULL,
    pdf text NOT NULL,
    serie character varying(255) NOT NULL,
    tipo character varying(255) NOT NULL,
    xml text NOT NULL,
    empresa_id bigint,
    venda_compra_loja_virtual_id bigint NOT NULL
);


ALTER TABLE nota_fiscal_venda OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 49865)
-- Name: nota_item_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE nota_item_produto (
    id bigint NOT NULL,
    quantidade double precision NOT NULL,
    empresa_id bigint,
    nota_fiscal_compra_id bigint NOT NULL,
    produto_id bigint NOT NULL
);


ALTER TABLE nota_item_produto OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 49870)
-- Name: pessoa_fisica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE pessoa_fisica (
    id bigint NOT NULL,
    email character varying(255) NOT NULL,
    nome character varying(255) NOT NULL,
    telefone character varying(255) NOT NULL,
    tipo_pessoa character varying(255),
    empresa_id bigint,
    cpf character varying(255) NOT NULL,
    data_nascimento date
);


ALTER TABLE pessoa_fisica OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 49878)
-- Name: pessoa_juridica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE pessoa_juridica (
    id bigint NOT NULL,
    email character varying(255) NOT NULL,
    nome character varying(255) NOT NULL,
    telefone character varying(255) NOT NULL,
    tipo_pessoa character varying(255),
    empresa_id bigint,
    categoria character varying(255),
    cnpj character varying(255) NOT NULL,
    inscricao_estadual character varying(255) NOT NULL,
    inscricao_municipal character varying(255),
    nome_fantasia character varying(255) NOT NULL,
    razao_social character varying(255) NOT NULL
);


ALTER TABLE pessoa_juridica OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 49886)
-- Name: produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE produto (
    id bigint NOT NULL,
    alerta_quantidade_estoque boolean,
    altura double precision NOT NULL,
    ativo boolean,
    descricao text NOT NULL,
    largura double precision NOT NULL,
    link_youtube character varying(255),
    nome character varying(255) NOT NULL,
    peso double precision NOT NULL,
    profundidade double precision NOT NULL,
    quantidade_alerta_estoque integer,
    quantidade_cliques integer,
    quantidade_estoque integer NOT NULL,
    tipo_unidade character varying(255) NOT NULL,
    valor_venda numeric(19,2) NOT NULL,
    categoria_produto_id bigint NOT NULL,
    empresa_id bigint,
    marca_produto_id bigint NOT NULL
);


ALTER TABLE produto OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 49922)
-- Name: seq_acesso; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_acesso
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_acesso OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 49924)
-- Name: seq_avaliacao_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_avaliacao_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_avaliacao_produto OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 49926)
-- Name: seq_categoria_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_categoria_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_categoria_produto OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 49928)
-- Name: seq_conta_pagar; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_conta_pagar
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_conta_pagar OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 49930)
-- Name: seq_conta_receber; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_conta_receber
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_conta_receber OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 49932)
-- Name: seq_cupom_desconto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_cupom_desconto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_cupom_desconto OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 49934)
-- Name: seq_endereco; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_endereco
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_endereco OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 49936)
-- Name: seq_forma_pagamento; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_forma_pagamento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_forma_pagamento OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 49938)
-- Name: seq_imagem_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_imagem_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_imagem_produto OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 49940)
-- Name: seq_item_venda_loja; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_item_venda_loja
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_item_venda_loja OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 49942)
-- Name: seq_marca_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_marca_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_marca_produto OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 49944)
-- Name: seq_nota_fiscal_compra; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_nota_fiscal_compra
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_nota_fiscal_compra OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 49946)
-- Name: seq_nota_fiscal_venda; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_nota_fiscal_venda
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_nota_fiscal_venda OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 49948)
-- Name: seq_nota_item_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_nota_item_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_nota_item_produto OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 49950)
-- Name: seq_pessoa; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_pessoa
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_pessoa OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 49952)
-- Name: seq_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_produto OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 49954)
-- Name: seq_status_rastreio; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_status_rastreio
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_status_rastreio OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 49956)
-- Name: seq_usuario; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_usuario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_usuario OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 49958)
-- Name: seq_venda_compra_loja_virtual; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_venda_compra_loja_virtual
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_venda_compra_loja_virtual OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 49894)
-- Name: status_rastreio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE status_rastreio (
    id bigint NOT NULL,
    centro_distribuicao character varying(255),
    cidade character varying(255),
    estado character varying(255),
    status character varying(255),
    empresa_id bigint,
    venda_compra_loja_virtual_id bigint NOT NULL
);


ALTER TABLE status_rastreio OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 49902)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE usuario (
    id bigint NOT NULL,
    data_atualizacao_senha date NOT NULL,
    login character varying(255) NOT NULL,
    senha character varying(255) NOT NULL,
    empresa_id bigint,
    pessoa_id bigint NOT NULL
);


ALTER TABLE usuario OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 49910)
-- Name: usuario_acesso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE usuario_acesso (
    usuario_id bigint NOT NULL,
    acesso_id bigint NOT NULL
);


ALTER TABLE usuario_acesso OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 49913)
-- Name: venda_compra_loja_virtual; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE venda_compra_loja_virtual (
    id bigint NOT NULL,
    data_entrega date NOT NULL,
    data_venda date NOT NULL,
    dias_entrega integer NOT NULL,
    valor_desconto numeric(19,2),
    valor_frete numeric(19,2) NOT NULL,
    valor_total numeric(19,2) NOT NULL,
    cupom_desconto_id bigint,
    empresa_id bigint,
    endereco_cobranca_id bigint NOT NULL,
    endereco_entrega_id bigint NOT NULL,
    forma_pagamento_id bigint NOT NULL,
    nota_fiscal_venda_id bigint NOT NULL,
    pessoa_id bigint NOT NULL
);


ALTER TABLE venda_compra_loja_virtual OWNER TO postgres;

--
-- TOC entry 2325 (class 0 OID 49782)
-- Dependencies: 180
-- Data for Name: acesso; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2326 (class 0 OID 49787)
-- Dependencies: 181
-- Data for Name: avaliacao_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2327 (class 0 OID 49792)
-- Dependencies: 182
-- Data for Name: categoria_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2328 (class 0 OID 49797)
-- Dependencies: 183
-- Data for Name: conta_pagar; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2329 (class 0 OID 49805)
-- Dependencies: 184
-- Data for Name: conta_receber; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2330 (class 0 OID 49813)
-- Dependencies: 185
-- Data for Name: cupom_desconto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2331 (class 0 OID 49818)
-- Dependencies: 186
-- Data for Name: endereco; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2332 (class 0 OID 49826)
-- Dependencies: 187
-- Data for Name: forma_pagamento; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2333 (class 0 OID 49831)
-- Dependencies: 188
-- Data for Name: imagem_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2334 (class 0 OID 49839)
-- Dependencies: 189
-- Data for Name: item_venda_loja; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2335 (class 0 OID 49844)
-- Dependencies: 190
-- Data for Name: marca_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2336 (class 0 OID 49849)
-- Dependencies: 191
-- Data for Name: nota_fiscal_compra; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2337 (class 0 OID 49857)
-- Dependencies: 192
-- Data for Name: nota_fiscal_venda; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2338 (class 0 OID 49865)
-- Dependencies: 193
-- Data for Name: nota_item_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2339 (class 0 OID 49870)
-- Dependencies: 194
-- Data for Name: pessoa_fisica; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2340 (class 0 OID 49878)
-- Dependencies: 195
-- Data for Name: pessoa_juridica; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2341 (class 0 OID 49886)
-- Dependencies: 196
-- Data for Name: produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2373 (class 0 OID 0)
-- Dependencies: 201
-- Name: seq_acesso; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_acesso', 1, false);


--
-- TOC entry 2374 (class 0 OID 0)
-- Dependencies: 202
-- Name: seq_avaliacao_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_avaliacao_produto', 1, false);


--
-- TOC entry 2375 (class 0 OID 0)
-- Dependencies: 203
-- Name: seq_categoria_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_categoria_produto', 1, false);


--
-- TOC entry 2376 (class 0 OID 0)
-- Dependencies: 204
-- Name: seq_conta_pagar; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_conta_pagar', 1, false);


--
-- TOC entry 2377 (class 0 OID 0)
-- Dependencies: 205
-- Name: seq_conta_receber; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_conta_receber', 1, false);


--
-- TOC entry 2378 (class 0 OID 0)
-- Dependencies: 206
-- Name: seq_cupom_desconto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_cupom_desconto', 1, false);


--
-- TOC entry 2379 (class 0 OID 0)
-- Dependencies: 207
-- Name: seq_endereco; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_endereco', 1, false);


--
-- TOC entry 2380 (class 0 OID 0)
-- Dependencies: 208
-- Name: seq_forma_pagamento; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_forma_pagamento', 1, false);


--
-- TOC entry 2381 (class 0 OID 0)
-- Dependencies: 209
-- Name: seq_imagem_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_imagem_produto', 1, false);


--
-- TOC entry 2382 (class 0 OID 0)
-- Dependencies: 210
-- Name: seq_item_venda_loja; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_item_venda_loja', 1, false);


--
-- TOC entry 2383 (class 0 OID 0)
-- Dependencies: 211
-- Name: seq_marca_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_marca_produto', 1, false);


--
-- TOC entry 2384 (class 0 OID 0)
-- Dependencies: 212
-- Name: seq_nota_fiscal_compra; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_nota_fiscal_compra', 1, false);


--
-- TOC entry 2385 (class 0 OID 0)
-- Dependencies: 213
-- Name: seq_nota_fiscal_venda; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_nota_fiscal_venda', 1, false);


--
-- TOC entry 2386 (class 0 OID 0)
-- Dependencies: 214
-- Name: seq_nota_item_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_nota_item_produto', 1, false);


--
-- TOC entry 2387 (class 0 OID 0)
-- Dependencies: 215
-- Name: seq_pessoa; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_pessoa', 1, false);


--
-- TOC entry 2388 (class 0 OID 0)
-- Dependencies: 216
-- Name: seq_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_produto', 1, false);


--
-- TOC entry 2389 (class 0 OID 0)
-- Dependencies: 217
-- Name: seq_status_rastreio; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_status_rastreio', 1, false);


--
-- TOC entry 2390 (class 0 OID 0)
-- Dependencies: 218
-- Name: seq_usuario; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_usuario', 1, false);


--
-- TOC entry 2391 (class 0 OID 0)
-- Dependencies: 219
-- Name: seq_venda_compra_loja_virtual; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_venda_compra_loja_virtual', 1, false);


--
-- TOC entry 2342 (class 0 OID 49894)
-- Dependencies: 197
-- Data for Name: status_rastreio; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2343 (class 0 OID 49902)
-- Dependencies: 198
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2344 (class 0 OID 49910)
-- Dependencies: 199
-- Data for Name: usuario_acesso; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2345 (class 0 OID 49913)
-- Dependencies: 200
-- Data for Name: venda_compra_loja_virtual; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2112 (class 2606 OID 49786)
-- Name: acesso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acesso
    ADD CONSTRAINT acesso_pkey PRIMARY KEY (id);


--
-- TOC entry 2114 (class 2606 OID 49791)
-- Name: avaliacao_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY avaliacao_produto
    ADD CONSTRAINT avaliacao_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2116 (class 2606 OID 49796)
-- Name: categoria_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY categoria_produto
    ADD CONSTRAINT categoria_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2118 (class 2606 OID 49804)
-- Name: conta_pagar_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conta_pagar
    ADD CONSTRAINT conta_pagar_pkey PRIMARY KEY (id);


--
-- TOC entry 2120 (class 2606 OID 49812)
-- Name: conta_receber_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conta_receber
    ADD CONSTRAINT conta_receber_pkey PRIMARY KEY (id);


--
-- TOC entry 2122 (class 2606 OID 49817)
-- Name: cupom_desconto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cupom_desconto
    ADD CONSTRAINT cupom_desconto_pkey PRIMARY KEY (id);


--
-- TOC entry 2124 (class 2606 OID 49825)
-- Name: endereco_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY endereco
    ADD CONSTRAINT endereco_pkey PRIMARY KEY (id);


--
-- TOC entry 2126 (class 2606 OID 49830)
-- Name: forma_pagamento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY forma_pagamento
    ADD CONSTRAINT forma_pagamento_pkey PRIMARY KEY (id);


--
-- TOC entry 2128 (class 2606 OID 49838)
-- Name: imagem_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY imagem_produto
    ADD CONSTRAINT imagem_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2130 (class 2606 OID 49843)
-- Name: item_venda_loja_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_venda_loja
    ADD CONSTRAINT item_venda_loja_pkey PRIMARY KEY (id);


--
-- TOC entry 2132 (class 2606 OID 49848)
-- Name: marca_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY marca_produto
    ADD CONSTRAINT marca_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2134 (class 2606 OID 49856)
-- Name: nota_fiscal_compra_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_compra
    ADD CONSTRAINT nota_fiscal_compra_pkey PRIMARY KEY (id);


--
-- TOC entry 2136 (class 2606 OID 49864)
-- Name: nota_fiscal_venda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_venda
    ADD CONSTRAINT nota_fiscal_venda_pkey PRIMARY KEY (id);


--
-- TOC entry 2138 (class 2606 OID 49869)
-- Name: nota_item_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_item_produto
    ADD CONSTRAINT nota_item_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2140 (class 2606 OID 49877)
-- Name: pessoa_fisica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pessoa_fisica
    ADD CONSTRAINT pessoa_fisica_pkey PRIMARY KEY (id);


--
-- TOC entry 2142 (class 2606 OID 49885)
-- Name: pessoa_juridica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pessoa_juridica
    ADD CONSTRAINT pessoa_juridica_pkey PRIMARY KEY (id);


--
-- TOC entry 2144 (class 2606 OID 49893)
-- Name: produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY produto
    ADD CONSTRAINT produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2146 (class 2606 OID 49901)
-- Name: status_rastreio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY status_rastreio
    ADD CONSTRAINT status_rastreio_pkey PRIMARY KEY (id);


--
-- TOC entry 2150 (class 2606 OID 49919)
-- Name: uk_fhwpg5wu1u5p306q8gycxn9ky; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario_acesso
    ADD CONSTRAINT uk_fhwpg5wu1u5p306q8gycxn9ky UNIQUE (acesso_id);


--
-- TOC entry 2152 (class 2606 OID 49921)
-- Name: unique_usuario_acesso; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario_acesso
    ADD CONSTRAINT unique_usuario_acesso UNIQUE (usuario_id, acesso_id);


--
-- TOC entry 2148 (class 2606 OID 49909)
-- Name: usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 2154 (class 2606 OID 49917)
-- Name: venda_compra_loja_virtual_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY venda_compra_loja_virtual
    ADD CONSTRAINT venda_compra_loja_virtual_pkey PRIMARY KEY (id);


--
-- TOC entry 2197 (class 2620 OID 50174)
-- Name: validarchavepessoafornecedorainsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validarchavepessoafornecedorainsert BEFORE INSERT ON conta_pagar FOR EACH ROW EXECUTE PROCEDURE validarchavepessoafornecedora();


--
-- TOC entry 2198 (class 2620 OID 50175)
-- Name: validarchavepessoafornecedoraupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validarchavepessoafornecedoraupdate BEFORE UPDATE ON conta_pagar FOR EACH ROW EXECUTE PROCEDURE validarchavepessoafornecedora();


--
-- TOC entry 2195 (class 2620 OID 50161)
-- Name: validarchavepessoainsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validarchavepessoainsert BEFORE INSERT ON avaliacao_produto FOR EACH ROW EXECUTE PROCEDURE validarchavepessoa();


--
-- TOC entry 2201 (class 2620 OID 50163)
-- Name: validarchavepessoainsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validarchavepessoainsert BEFORE INSERT ON conta_receber FOR EACH ROW EXECUTE PROCEDURE validarchavepessoa();


--
-- TOC entry 2203 (class 2620 OID 50165)
-- Name: validarchavepessoainsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validarchavepessoainsert BEFORE INSERT ON endereco FOR EACH ROW EXECUTE PROCEDURE validarchavepessoa();


--
-- TOC entry 2205 (class 2620 OID 50167)
-- Name: validarchavepessoainsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validarchavepessoainsert BEFORE INSERT ON nota_fiscal_compra FOR EACH ROW EXECUTE PROCEDURE validarchavepessoa();


--
-- TOC entry 2207 (class 2620 OID 50169)
-- Name: validarchavepessoainsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validarchavepessoainsert BEFORE INSERT ON usuario FOR EACH ROW EXECUTE PROCEDURE validarchavepessoa();


--
-- TOC entry 2209 (class 2620 OID 50171)
-- Name: validarchavepessoainsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validarchavepessoainsert BEFORE INSERT ON venda_compra_loja_virtual FOR EACH ROW EXECUTE PROCEDURE validarchavepessoa();


--
-- TOC entry 2199 (class 2620 OID 50177)
-- Name: validarchavepessoapagadorainsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validarchavepessoapagadorainsert BEFORE INSERT ON conta_pagar FOR EACH ROW EXECUTE PROCEDURE validarchavepessoapagadora();


--
-- TOC entry 2200 (class 2620 OID 50178)
-- Name: validarchavepessoapagadoraupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validarchavepessoapagadoraupdate BEFORE UPDATE ON conta_pagar FOR EACH ROW EXECUTE PROCEDURE validarchavepessoapagadora();


--
-- TOC entry 2196 (class 2620 OID 50162)
-- Name: validarchavepessoaupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validarchavepessoaupdate BEFORE UPDATE ON avaliacao_produto FOR EACH ROW EXECUTE PROCEDURE validarchavepessoa();


--
-- TOC entry 2202 (class 2620 OID 50164)
-- Name: validarchavepessoaupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validarchavepessoaupdate BEFORE UPDATE ON conta_receber FOR EACH ROW EXECUTE PROCEDURE validarchavepessoa();


--
-- TOC entry 2204 (class 2620 OID 50166)
-- Name: validarchavepessoaupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validarchavepessoaupdate BEFORE UPDATE ON endereco FOR EACH ROW EXECUTE PROCEDURE validarchavepessoa();


--
-- TOC entry 2206 (class 2620 OID 50168)
-- Name: validarchavepessoaupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validarchavepessoaupdate BEFORE UPDATE ON nota_fiscal_compra FOR EACH ROW EXECUTE PROCEDURE validarchavepessoa();


--
-- TOC entry 2208 (class 2620 OID 50170)
-- Name: validarchavepessoaupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validarchavepessoaupdate BEFORE UPDATE ON usuario FOR EACH ROW EXECUTE PROCEDURE validarchavepessoa();


--
-- TOC entry 2210 (class 2620 OID 50172)
-- Name: validarchavepessoaupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validarchavepessoaupdate BEFORE UPDATE ON venda_compra_loja_virtual FOR EACH ROW EXECUTE PROCEDURE validarchavepessoa();


--
-- TOC entry 2179 (class 2606 OID 50080)
-- Name: fk_401f6ieetrkdfwgjf7mbxary2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pessoa_fisica
    ADD CONSTRAINT fk_401f6ieetrkdfwgjf7mbxary2 FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2187 (class 2606 OID 50120)
-- Name: fk_acesso; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario_acesso
    ADD CONSTRAINT fk_acesso FOREIGN KEY (acesso_id) REFERENCES acesso(id);


--
-- TOC entry 2181 (class 2606 OID 50090)
-- Name: fk_categoria_produto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY produto
    ADD CONSTRAINT fk_categoria_produto FOREIGN KEY (categoria_produto_id) REFERENCES categoria_produto(id);


--
-- TOC entry 2171 (class 2606 OID 50040)
-- Name: fk_conta_pagar; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_compra
    ADD CONSTRAINT fk_conta_pagar FOREIGN KEY (conta_pagar_id) REFERENCES conta_pagar(id);


--
-- TOC entry 2189 (class 2606 OID 50130)
-- Name: fk_cupom_desconto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY venda_compra_loja_virtual
    ADD CONSTRAINT fk_cupom_desconto FOREIGN KEY (cupom_desconto_id) REFERENCES cupom_desconto(id);


--
-- TOC entry 2155 (class 2606 OID 49960)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY avaliacao_produto
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2157 (class 2606 OID 49970)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY categoria_produto
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2158 (class 2606 OID 49975)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conta_pagar
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2161 (class 2606 OID 49990)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conta_receber
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2162 (class 2606 OID 49995)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cupom_desconto
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2163 (class 2606 OID 50000)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY endereco
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2164 (class 2606 OID 50005)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY forma_pagamento
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2165 (class 2606 OID 50010)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY imagem_produto
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2167 (class 2606 OID 50020)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_venda_loja
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2170 (class 2606 OID 50035)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY marca_produto
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2172 (class 2606 OID 50045)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_compra
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2174 (class 2606 OID 50055)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_venda
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2176 (class 2606 OID 50065)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_item_produto
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2182 (class 2606 OID 50095)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY produto
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2184 (class 2606 OID 50105)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY status_rastreio
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2186 (class 2606 OID 50115)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2190 (class 2606 OID 50135)
-- Name: fk_empresa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY venda_compra_loja_virtual
    ADD CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2191 (class 2606 OID 50140)
-- Name: fk_endereco_cobranca; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY venda_compra_loja_virtual
    ADD CONSTRAINT fk_endereco_cobranca FOREIGN KEY (endereco_cobranca_id) REFERENCES endereco(id);


--
-- TOC entry 2192 (class 2606 OID 50145)
-- Name: fk_endereco_entrega; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY venda_compra_loja_virtual
    ADD CONSTRAINT fk_endereco_entrega FOREIGN KEY (endereco_entrega_id) REFERENCES endereco(id);


--
-- TOC entry 2193 (class 2606 OID 50150)
-- Name: fk_forma_pagamento; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY venda_compra_loja_virtual
    ADD CONSTRAINT fk_forma_pagamento FOREIGN KEY (forma_pagamento_id) REFERENCES forma_pagamento(id);


--
-- TOC entry 2180 (class 2606 OID 50085)
-- Name: fk_jcqalei9t301o4gglt27b8kdp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pessoa_juridica
    ADD CONSTRAINT fk_jcqalei9t301o4gglt27b8kdp FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2183 (class 2606 OID 50100)
-- Name: fk_marca_produto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY produto
    ADD CONSTRAINT fk_marca_produto FOREIGN KEY (marca_produto_id) REFERENCES marca_produto(id);


--
-- TOC entry 2177 (class 2606 OID 50070)
-- Name: fk_nota_fiscal_compra; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_item_produto
    ADD CONSTRAINT fk_nota_fiscal_compra FOREIGN KEY (nota_fiscal_compra_id) REFERENCES nota_fiscal_compra(id);


--
-- TOC entry 2194 (class 2606 OID 50155)
-- Name: fk_nota_fiscal_venda; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY venda_compra_loja_virtual
    ADD CONSTRAINT fk_nota_fiscal_venda FOREIGN KEY (nota_fiscal_venda_id) REFERENCES nota_fiscal_venda(id);


--
-- TOC entry 2173 (class 2606 OID 50050)
-- Name: fk_pessoa; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_compra
    ADD CONSTRAINT fk_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2159 (class 2606 OID 49980)
-- Name: fk_pessoa_fornecedora; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conta_pagar
    ADD CONSTRAINT fk_pessoa_fornecedora FOREIGN KEY (pessoa_fornecedora_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2160 (class 2606 OID 49985)
-- Name: fk_pessoa_pagadora; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conta_pagar
    ADD CONSTRAINT fk_pessoa_pagadora FOREIGN KEY (pessoa_pagadora_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2156 (class 2606 OID 49965)
-- Name: fk_produto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY avaliacao_produto
    ADD CONSTRAINT fk_produto FOREIGN KEY (produto_id) REFERENCES produto(id);


--
-- TOC entry 2166 (class 2606 OID 50015)
-- Name: fk_produto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY imagem_produto
    ADD CONSTRAINT fk_produto FOREIGN KEY (produto_id) REFERENCES produto(id);


--
-- TOC entry 2168 (class 2606 OID 50025)
-- Name: fk_produto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_venda_loja
    ADD CONSTRAINT fk_produto FOREIGN KEY (produto_id) REFERENCES produto(id);


--
-- TOC entry 2178 (class 2606 OID 50075)
-- Name: fk_produto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_item_produto
    ADD CONSTRAINT fk_produto FOREIGN KEY (produto_id) REFERENCES produto(id);


--
-- TOC entry 2188 (class 2606 OID 50125)
-- Name: fk_usuario; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario_acesso
    ADD CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id);


--
-- TOC entry 2169 (class 2606 OID 50030)
-- Name: fk_venda_compra_loja_virtual; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_venda_loja
    ADD CONSTRAINT fk_venda_compra_loja_virtual FOREIGN KEY (venda_compra_loja_virtual_id) REFERENCES venda_compra_loja_virtual(id);


--
-- TOC entry 2175 (class 2606 OID 50060)
-- Name: fk_venda_compra_loja_virtual; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_venda
    ADD CONSTRAINT fk_venda_compra_loja_virtual FOREIGN KEY (venda_compra_loja_virtual_id) REFERENCES venda_compra_loja_virtual(id);


--
-- TOC entry 2185 (class 2606 OID 50110)
-- Name: fk_venda_compra_loja_virtual; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY status_rastreio
    ADD CONSTRAINT fk_venda_compra_loja_virtual FOREIGN KEY (venda_compra_loja_virtual_id) REFERENCES venda_compra_loja_virtual(id);


--
-- TOC entry 2371 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2023-12-18 02:18:19

--
-- PostgreSQL database dump complete
--