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
 */
public final class NotaPonderada implements IStatistics{
	

	
	private static final Double ganoDecenas = 0.642857143d;
	
	private static  final Double ganoCentenas = 0.214285714d;
	
	private static  final Double ganoMiles = 0.089285714d;
	
	private static  final Double ganoDiezMiles = 0.053571429d;
	
	private static  final Double perdioDecenas = 0.802197802d;
	
	private static  final Double perdioCentenas = 0.142857143d;
	
	private static  final Double perdioMiles = 0.010989011d;
	
	private static  final Double perdioDiezMiles = 0.043956044d;


	
	
	//fixme:
		//2. Realizar Prueba unitaria de los valores




	/*
	 * (non-Javadoc)
	 * @see com.wireless.soft.indices.cfd.statistics.IStatistics#getWinStatistics(java.lang.Double)
	 * 
	 */
	//	=IF(AND(J4>=0,J4<100),"decenas",IF(AND(J4>=100,J4<1000),"centenas",IF(AND(J4>=1000,J4<10000),"miles",IF(J4>=10000,"10miles","NO"))))
	@Override
	public Double getWinStatistics(Object valor) {
		Double winStatistics = null;
		if ((double)valor >=0 && (double)valor < 100){
			winStatistics = ganoDecenas;
		}else if ((double)valor >=100 && (double)valor < 1000){
			winStatistics = ganoCentenas;
		}else if ((double)valor >=1000 && (double)valor < 10000){
			winStatistics = ganoMiles;
		}else if ((double)valor >=10000){
			winStatistics = ganoDiezMiles;
		}
		return winStatistics;
	}
	
	/* (non-Javadoc)
	 * @see com.wireless.soft.indices.cfd.statistics.IStatistics#getLostStatistics(java.lang.Double)
	 */
	//	=IF(AND(J4>=0,J4<100),"decenas",IF(AND(J4>=100,J4<1000),"centenas",IF(AND(J4>=1000,J4<10000),"miles",IF(J4>=10000,"10miles","NO"))))
	@Override
	public Double getLostStatistics(Object valor) {
		Double lostStatistics = null;
		if ((double)valor >=0 && (double)valor < 100){
			lostStatistics = perdioDecenas;
		}else if ((double)valor >=100 && (double)valor < 1000){
			lostStatistics = perdioCentenas;
		}else if ((double)valor >=1000 && (double)valor < 10000){
			lostStatistics = perdioMiles;
		}else if ((double)valor >=10000){
			lostStatistics = perdioDiezMiles;
		}
		return lostStatistics;
	}
	
}
