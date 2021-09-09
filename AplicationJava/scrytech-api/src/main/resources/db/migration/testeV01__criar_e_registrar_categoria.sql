CREATE TABLE CATEGORIA(
	CODIGO BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	NOME VARCHAR(50) NOT NULL
	
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO CATEGORIA (NOME) VALUES ('Lazer');
INSERT INTO CATEGORIA (NOME) VALUES ('Alimentação');
INSERT INTO CATEGORIA (NOME) VALUES ('Supermercado');
INSERT INTO CATEGORIA (NOME) VALUES ('Farmácia');
INSERT INTO CATEGORIA (NOME) VALUES ('Outros');
