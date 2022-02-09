CREATE TABLE receita (
	id BIGINT NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(255) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_receita DATETIME NOT NULL, 
    
    PRIMARY KEY (id)
);