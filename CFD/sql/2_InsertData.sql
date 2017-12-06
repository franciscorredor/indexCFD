--insertar companias
insert into indexyahoocfd.iyc_stock_companies (scn_name) values ('Yelp Inc.')

--Inserta relacion entre companias e 
insert into indexyahoocfd.iyc_stack_company_index (scn_codigo, sci_url_index) values (1, 'http://finance.yahoo.com/webservice/v1/symbols/YELP/quote?format=json&view=detail')

--inserta link para encontrar los indicadores: YTD & 1YR Return
insert into indexyahoocfd.iyc_bbg_indicator(SCN_CODIGO, BBG_URL_YRTN, BBG_FECHA_CREACION) values (119, 'http://www.bloomberg.com/quote/AIG:US', now());