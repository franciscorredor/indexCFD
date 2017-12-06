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
public class PriceBetweenHighLow implements IStatistics{
	
	private final Double ganoTrue = 0.285714286d;
	
	private final Double ganoFalse = 0.714285714d;
	
	private final Double perdioTrue = 0.351648352d;
	
	private final Double perdioFalse = 0.648351648d;

	/* (non-Javadoc)
	 * @see com.wireless.soft.indices.cfd.statistics.IStatistics#getWinStatistics(java.lang.Object)
	 */
	@Override
	public Double getWinStatistics(Object valor) {
		Double winStatistics = null;
		if ((boolean)valor == true ){
			winStatistics = ganoTrue;
		}else if ((boolean)valor == false ){
			winStatistics = ganoFalse;
		}
		return winStatistics;
	}

	/* (non-Javadoc)
	 * @see com.wireless.soft.indices.cfd.statistics.IStatistics#getLostStatistics(java.lang.Object)
	 */
	@Override
	public Double getLostStatistics(Object valor) {
		Double lostStatistics = null;
		if ((boolean)valor == true ){
			lostStatistics = perdioTrue;
		}else if ((boolean)valor == false ){
			lostStatistics = perdioFalse;
		}
		return lostStatistics;
	}
	
	

}
