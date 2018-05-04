--2018May04
ALTER TABLE  `indexyahoocfd`.`dmc_data_mining_company` 
	ADD DMC_PROBABILIDAD_WIN VARCHAR(1000),
	ADD DMC_PROBABILIDAD_LOST VARCHAR(1000),
	ADD DMC_BANDERA_INCREMENTO smallint;


--2018May04
ALTER TABLE  `indexyahoocfd`.`dmc_data_mining_company` 
	ADD DMC_MOMENTUM_FACTOR int;

--2018Abril20
CREATE TABLE `indexyahoocfd`.`HST_HISTORICAL_DATA_COMPANY_TO_RSI`
(
	-- identificador unico del regitro
   HST_CODIGO INT NOT NULL AUTO_INCREMENT, --int PRIMARY KEY NOT NULL,
   -- Join con la compania
   SCN_CODIGO INT NOT NULL,
   --DATE,CLOSE,HIGH,LOW,OPEN,VOLUME
   -- FECHA REGISTRO
   HST_DATE timestamp,
   -- Precio Accion CLOSE			
   HST_STOCK_PRICE_CLOSE VARCHAR(1000),
   -- Precio Accion HIGH			
   HST_STOCK_PRICE_HIGH VARCHAR(1000),
   -- Precio Accion LOW			
   HST_STOCK_PRICE_LOW VARCHAR(1000),
   -- Precio Accion OPEN			
   HST_STOCK_PRICE_OPEN VARCHAR(1000),
   -- Accion VOLUME			
   HST_STOCK_VOLUME VARCHAR(1000),
   -- fecha creacion del registro
   HST_FECHA_CREACION timestamp,
   PRIMARY KEY (`HST_CODIGO`)
)
;   

ALTER TABLE `indexyahoocfd`.`HST_HISTORICAL_DATA_COMPANY_TO_RSI` 

  ADD CONSTRAINT `companyForeignKeyHst`

  FOREIGN KEY (`SCN_CODIGO` )

  REFERENCES `indexyahoocfd`.`iyc_stock_companies` (`SCN_CODIGO` )
;

--2017Nov06
ALTER TABLE  `indexyahoocfd`.`iyc_stock_companies` 
	ADD SCN_MSN_QUOTE VARCHAR(500);



--2017Jun19
ALTER TABLE  `indexyahoocfd`.`iyc_stock_companies` 
	ADD SCN_CID VARCHAR(25);

--2016Nov01
ALTER TABLE  `indexyahoocfd`.`iyc_stock_companies` 
	ADD SCN_GOOGLE_SYMBOL VARCHAR(500);


CREATE SCHEMA `indexyahoocfd` ;

-- Tabla que me indica la industria a la que pertence la compa�ia


-- Tabla que me indica la bolsa a la que pertenece la compa�ia, uk, francia, usa
		--LSE: London Stock Exchange

-- Pagina web de la compa�ia.

-- Nombre de la tabla de datamining
--DROP TABLE  `indexyahoocfd`.`dmc_data_mining_company`
CREATE TABLE `indexyahoocfd`.`dmc_data_mining_company`
(
	-- identificador unico del regitro
   DMC_CODIGO INT NOT NULL AUTO_INCREMENT, --int PRIMARY KEY NOT NULL,
   -- identificador unico de la iteracion
   DMC_CODIGO_GRP_ITERACION int NOT NULL,
   -- fecha creacion del registro
   DMC_FECHA_CREACION timestamp,
   -- Join con la compania
   `SCN_CODIGO` INT NOT NULL,
   -- isPriceBetweenHighLow		
   DMC_IS_PRICE_BETWEEN_HIGH_LOW smallint,
   -- Tendencia, ipod 3meses, *atributo Nominal		
   /*
     * 	(0) - alza
		(1)	- baja
		(2)	Alza
		(3)	Baja
     */	
   DMC_TENDENCIA int,
   --Price Percentage Increment Class
   DMC_PRICE_PERCENTAGE_INCREMENT VARCHAR(1000),
   -- Nota Ponderada			
   DMC_NOTA_PONDERADA VARCHAR(1000),
   --Price Earning Ratio		
   DMC_PRICE_EARNING_RATIO VARCHAR(1000),
   -- 	RSI, Relative Strength Index		
   DMC_RELATIVE_STRENGTH_INDEX VARCHAR(1000),
   -- 	Precio Accion			
   DMC_STOCK_PRICE VARCHAR(1000),
   --LastDigit, es el utimo digito del precio de la accion
   -- en la muestra indica que un valor de 7siete tiene
   -- probablidad en que la accion suba de precio.
   DMC_LAST_DIGIT int,
   --Diff Max Vs Min			
   DMC_DIFF_MAX_MIN VARCHAR(1000),
   --% Incremento			
   DMC_PERCENTAGE_INCREMENT VARCHAR(1000),
   --Valor accion Mayor a la media en RSI			
   DMC_IS_STOCK_PRICE_MAYOR_MEDIA smallint,
   -- Determina el valor de la accion al final del dia, 
   -- en la logica del sistema nos permite saber si la
   --accion gano o perdio valor, comparando con el campo 'dmc_stock_price'
   DMC_STOCK_PRICE_CLOSE VARCHAR(1000),
   PRIMARY KEY (`DMC_CODIGO`)
)
;

ALTER TABLE `indexyahoocfd`.`dmc_data_mining_company` 

  ADD CONSTRAINT `companyForeignKey`

  FOREIGN KEY (`SCN_CODIGO` )

  REFERENCES `indexyahoocfd`.`iyc_stock_companies` (`SCN_CODIGO` )

  ON DELETE NO ACTION

  ON UPDATE NO ACTION

, ADD INDEX `company` (`SCN_CODIGO` ASC) ;

--CREATE UNIQUE INDEX PRIMARY ON dmc_data_mining_company(DMC_CODIGO)
--;
--2016Nov01
ALTER TABLE  `indexyahoocfd`.`dmc_data_mining_company` 
	ADD DMC_YTD_PLATAFORMA VARCHAR(1000);



-- Nombre de compa�ias en la bolsa
CREATE TABLE `indexyahoocfd`.`iyc_stock_companies` (
  -- identificador unico de la compa�ia
  `SCN_CODIGO` INT NOT NULL AUTO_INCREMENT,
  -- Nombre de la compa�ia
  `SCN_NAME` VARCHAR(500) NOT NULL,
  -- Pagina web de la compa�ia, puede que tenga diferentes, tenerlo en cuenta en el dise�o
  --`SCN_WEB_PAGE
  -- Sigla en la bolsa, puede que tenga otra sigla en una bolsa diferentes uk, tenerlo en cuenta en el die�o
  --`SCN_SIGLA
  PRIMARY KEY (`SCN_CODIGO`),
  UNIQUE INDEX `SCN_NAME_UNIQUE` (`SCN_NAME` ASC));

-- indice yahoo de las compa�ias 
CREATE TABLE `indexyahoocfd`.`iyc_stack_company_index` (
--identificador unico de la tabla 
`SCI_CODIGO` INT NOT NULL AUTO_INCREMENT,
--identificador que relaciona con el nombre de la compa�ia 
`SCN_CODIGO` INT NOT NULL,
 --Link del indicador
`SCI_URL_INDEX` VARCHAR(5000) NOT NULL,
-- Fecha creaci�n del indice 
`SCI_FECHA_CREACION` DATE NULL,
  PRIMARY KEY (`SCI_CODIGO`));

--DROP TABLE  `indexyahoocfd`.`iyc_quote_company_history`  
 -- almacena el historico de las quotes de la diferentes compa�ias 
CREATE TABLE `indexyahoocfd`.`iyc_quote_company_history` (
--identificador unico de la tabla 
`QCH_CODIGO` INT NOT NULL AUTO_INCREMENT,
--identificador que relaciona con el nombre de la compa�ia 
`SCN_CODIGO` INT NOT NULL, 
-- Fecha creaci�n del indice 
`QHC_FECHA_CREACION` TIMESTAMP NOT NULL,
`name` VARCHAR(1000),
`symbol` VARCHAR(1000),
`ts` VARCHAR(1000),
`type` VARCHAR(1000),
`utctime` VARCHAR(1000),
`volume` VARCHAR(1000),
--Se modifica nombre de tabla porque cuando se inserta, genera error de syntaxis
`syntaxis_change` VARCHAR(1000),
`chg_percent` VARCHAR(1000),
`day_high` VARCHAR(1000),
`day_low` VARCHAR(1000),
`issuer_name` VARCHAR(1000),
`issuer_name_lang` VARCHAR(1000),
`year_high` VARCHAR(1000),
`year_low` VARCHAR(1000),
`price` VARCHAR(1000),
PRIMARY KEY (`QCH_CODIGO`));


-- quotes yahoo de las compa�ias 
CREATE TABLE `indexyahoocfd`.`iyc_stack_company_quotes` (
--identificador unico de la tabla 
`SCQ_CODIGO` INT NOT NULL AUTO_INCREMENT,
--identificador que relaciona con el nombre de la compa�ia 
`SCN_CODIGO` INT NOT NULL,
 --Link del indicador
`SCQ_URL_QUOTE` VARCHAR(5000) NOT NULL,
-- Fecha creaci�n del indice 
`SCQ_FECHA_CREACION` DATE NULL,
  PRIMARY KEY (`SCQ_CODIGO`));
  
  
 --DROP TABLE  `indexyahoocfd`.`iyc_fundamental_company_history`  
 -- almacena el historico de informacion fundamental de las diferentes compa�ias 
CREATE TABLE `indexyahoocfd`.`iyc_fundamental_company_history` (
--identificador unico de la tabla 
`FCH_CODIGO` INT NOT NULL AUTO_INCREMENT,
--identificador que relaciona con el nombre de la compa�ia 
`SCN_CODIGO` INT NOT NULL, 
-- Fecha creaci�n del indice 
`FCH_FECHA_CREACION` TIMESTAMP NOT NULL,
`PERatio` VARCHAR(1000),
`Bid` VARCHAR(1000),
`Ask` VARCHAR(1000),
`EBITDA` VARCHAR(1000),
`PriceSales` VARCHAR(1000),
`PriceEPSEstimateCurrentYear` VARCHAR(1000),
`PriceEPSEstimateNextYear` VARCHAR(1000),
`PEGRatio` VARCHAR(1000),
`MarketCapitalization` VARCHAR(1000),
`MarketCapRealtime` VARCHAR(1000),
PRIMARY KEY (`FCH_CODIGO`));

--Alter Table sample
--alter table `indexyahoocfd`.`iyc_fundamental_company_history`
--ADD Column `MarketCapRealtime` VARCHAR(1000)

-- indicador YTD & 1YR Return from Bloomberg 
CREATE TABLE `indexyahoocfd`.`iyc_bbg_indicator` (
--identificador unico de la tabla 
`BBG_CODIGO` INT NOT NULL AUTO_INCREMENT,
--identificador que relaciona con el nombre de la compa�ia 
`SCN_CODIGO` INT NOT NULL,
 --Link del indicador
`BBG_URL_YRTN` VARCHAR(5000) NOT NULL,
-- Fecha creaci�n del indice 
`BBG_FECHA_CREACION` DATE NULL,
  PRIMARY KEY (`BBG_CODIGO`));