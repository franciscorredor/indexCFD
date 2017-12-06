/**
 * 
 */
package com.wireless.soft.indices.cfd.statistics;

/**
 * @author HP
 *
 */
public final class PrecioAccion implements IStatistics {
	
	private static final Double ganoStockPriceDecenas = 0.5d;

	private static final Double ganoStockPriceCentenas = 0.375d;

	private static final Double ganoStockPriceMiles = 0.125d;
	
	private static final Double perdioStockPriceDecenas = 0.461538462d;

	private static final Double perdioStockPriceCentenas = 0.472527473d;

	private static final Double perdioStockPriceMiles = 0.065934066d;



	/* (non-Javadoc)
	 * @see com.wireless.soft.indices.cfd.statistics.IStatistics#getWinStatistics(java.lang.Object)
	 */
	//=IF(AND(D6>0,D6<100),"StockPriceDecenas",IF(AND(D6>=100,D6<1000),"StockPriceCentenas",IF(AND(D6>=1000,D6<10000),"StockPriceMiles","Clasificarme")))
	@Override
	public Double getWinStatistics(Object valor) {
		Double winStatistics = null;
		if ((double)valor >=0 && (double)valor < 100){
			winStatistics = ganoStockPriceDecenas;
		}else if ((double)valor >=100 && (double)valor < 1000){
			winStatistics = ganoStockPriceCentenas;
		}else if ((double)valor >=1000 && (double)valor < 10000){
			winStatistics = ganoStockPriceMiles;
		}
		return winStatistics;
	}

	/* (non-Javadoc)
	 * @see com.wireless.soft.indices.cfd.statistics.IStatistics#getLostStatistics(java.lang.Object)
	 */
	//=IF(AND(D6>0,D6<100),"StockPriceDecenas",IF(AND(D6>=100,D6<1000),"StockPriceCentenas",IF(AND(D6>=1000,D6<10000),"StockPriceMiles","Clasificarme")))
	@Override
	public Double getLostStatistics(Object valor) {
		Double lostStatistics = null;
		if ((double)valor >=0 && (double)valor < 100){
			lostStatistics = perdioStockPriceDecenas;
		}else if ((double)valor >=100 && (double)valor < 1000){
			lostStatistics = perdioStockPriceCentenas;
		}else if ((double)valor >=1000 && (double)valor < 10000){
			lostStatistics = perdioStockPriceMiles;
		}
		return lostStatistics;
	}

}
