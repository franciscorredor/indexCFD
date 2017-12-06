/**
 * 
 */
package com.wireless.soft.indices.cfd.statistics;

/**
 * @author Francisco Corredor
 * @version 1.0
 * @since 20161030
 * 
 * Clase final que continen los valores del modelo estadistico 
 * 	de DataMining REF: 
 *  Book: Data Mining, Practical machine learning, tools and techniques
 *		Chapter: 4.2
 *
 *
     * 	(0) - alza
		(1)	- baja
		(2)	Alza
		(3)	Baja
     
 */
public final class TendenciaTresMeses implements IStatistics{
	
	private final Double gano_alza = 0.089285714d;
	
	private final Double gano_baja = 0.071428571d;
	
	private final Double ganoALZA = 0.535714286d;
	
	private final Double ganoBAJA = 0.303571429d;
	
	private final Double perdio_alza = 0.032967033d;
	
	private final Double perdio_baja = 0.054945055d;
	
	private final Double perdioALZA = 0.769230769d;
	
	private final Double perdioBAJA = 0.142857143d;

	@Override
	public Double getWinStatistics(Object valor) {
		Double winStatistics = null;
		if ((int)valor == 0 ){
			winStatistics = gano_alza;
		}else if ((int)valor == 1 ){
			winStatistics = gano_baja;
		}else if ((int)valor == 2 ){
			winStatistics = ganoALZA;
		}else if ((int)valor == 3 ){
			winStatistics = ganoBAJA;
		}
		return winStatistics;
	}

	@Override
	public Double getLostStatistics(Object valor) {
		Double lostStatistics = null;
		if ((int)valor == 0 ){
			lostStatistics = perdio_alza;
		}else if ((int)valor == 1 ){
			lostStatistics = perdio_baja;
		}else if ((int)valor == 2 ){
			lostStatistics = perdioALZA;
		}else if ((int)valor == 3 ){
			lostStatistics = perdioBAJA;
		}
		return lostStatistics;
	}
	
	

}
