/**
 * 
 */
package com.wireless.soft.indices.cfd.statistics;

/**
 * @author FrancisCorredor
 * @since Nov 1, 2016
 * @version 2016
 * @git 
 * 
 * Clase que contiene el concepto del atributo: Valor accion mayor a la media
 */
public final class StockMayorMedia implements IStatistics {
	
	public final Double ganoTrue = 0.482142857d;
	public final Double ganoFalse = 0.517857143d;
	
	public final Double perdioTrue = 0.472527473d;
	public final Double perdioFalse = 0.527472527d;

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
