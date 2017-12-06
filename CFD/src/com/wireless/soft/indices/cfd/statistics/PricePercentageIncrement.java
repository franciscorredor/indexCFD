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
public final class PricePercentageIncrement implements IStatistics{
	
	private final Double ganoCero = 0d;
	
	private final Double ganoPriceIncrementA1 = 0.017857143d;
	
	private final Double ganoPriceIncrementB2 = 0.214285714d;
	
	private final Double ganoPriceIncrementD4 = 0.714285714d;
	
	private final Double ganoPriceIncrementE5 = 0.017857143d;
	
	private final Double ganoPriceIncrementF6 = 0.035714286d;
	
	private final Double perdioCero = 0.032967033d;
	
	private final Double perdioPriceIncrementA1 = 0.010989011d;
	
	private final Double perdioPriceIncrementB2 = 0.153846154d;
	
	private final Double perdioPriceIncrementD4 = 0.714285714d;
	
	private final Double perdioPriceIncrementE5 = 0.065934066d;
	
	private final Double perdioPriceIncrementF6 = 0.021978022d;

	@Override
	//=IF(H3<=-3,"PriceIncrementA1",IF(AND(H3<0,H3>-3),"PriceIncrementB2",IF(H3=0,"cero",IF(AND(H3>0,H3<=1),"PriceIncrementD4",IF(AND(H3>1,H3<=2),"PriceIncrementE5",IF(H3>2,"PriceIncrementF6","Clasificarme"))))))
	public Double getWinStatistics(Object valor) {
		Double winStatistics = null;
		if ((double)valor <= -3 ){
			winStatistics = ganoPriceIncrementA1;
		}else if ((double)valor < 0 && (double)valor > -3 ){
			winStatistics = ganoPriceIncrementB2;
		}else if ((double)valor == 0){
			winStatistics = ganoCero;
		}else if ((double)valor > 0 && (double)valor <= 1){
			winStatistics = ganoPriceIncrementD4;
		}else if ((double)valor > 1 && (double)valor <= 2){
			winStatistics = ganoPriceIncrementE5;
		}else if ((double)valor > 2){
			winStatistics = ganoPriceIncrementF6;
		}
		return winStatistics;
	}
	

	@Override
	//=IF(H3<=-3,"PriceIncrementA1",IF(AND(H3<0,H3>-3),"PriceIncrementB2",IF(H3=0,"cero",IF(AND(H3>0,H3<=1),"PriceIncrementD4",IF(AND(H3>1,H3<=2),"PriceIncrementE5",IF(H3>2,"PriceIncrementF6","Clasificarme"))))))
	public Double getLostStatistics(Object valor) {
		Double lostStatistics = null;
		if ((double)valor <= -3 ){
			lostStatistics = perdioPriceIncrementA1;
		}else if ((double)valor < 0 && (double)valor > -3 ){
			lostStatistics = perdioPriceIncrementB2;
		}else if ((double)valor == 0){
			lostStatistics = perdioCero;
		}else if ((double)valor > 0 && (double)valor <= 1){
			lostStatistics = perdioPriceIncrementD4;
		}else if ((double)valor > 1 && (double)valor <= 2){
			lostStatistics = perdioPriceIncrementE5;
		}else if ((double)valor > 2){
			lostStatistics = perdioPriceIncrementF6;
		}
		return lostStatistics;
	}
	
	
	

}
