CREATE TABLE despesa (
	id BIGINT NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(255) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_despesa DATETIME NOT NULL, 
    
    PRIMARY KEY (id)
);