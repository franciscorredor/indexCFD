/**
 * 
 */
package com.wireless.soft.indices.cfd.statistics;

/**
 * @author HP
 *
 */
public final class DiffMaxMin implements IStatistics {
	
	private static final Double ganoDiff0_2 = 0.285714286d;
	private static final Double ganoDiff2_7 = 0.285714286d;
	private static final Double ganoDiff7_49 = 0.232142857d;
	private static final Double ganoDiff49mas = 0.196428571d;

	private static final Double perdioDiff0_2 = 0.395604396d;
	private static final Double perdioDiff2_7 = 0.241758242d;
	private static final Double perdioDiff7_49 = 0.296703297d;
	private static final Double perdioDiff49mas = 0.065934066d;

	
	/* (non-Javadoc)
	 * @see com.wireless.soft.indices.cfd.statistics.IStatistics#getWinStatistics(java.lang.Object)
	 */
	@Override
	//=IF(AND(R6>0,R6<=2),"Diff(0-2)",IF(AND(R6>2,R6<=7),"Diff(2-7)",IF(AND(R6>7,R6<=49),"Diff(7-49)",IF(R6>49,"Diff(49Mas)","Clasificarmne"))))
	public Double getWinStatistics(Object valor) {
		Double winStatistics = null;
		if ((double)valor >0 && (double)valor <= 2){
			winStatistics = ganoDiff0_2;
		}else if ((double)valor >2 && (double)valor <= 7){
			winStatistics = ganoDiff2_7;
		}else if ((double)valor >7 && (double)valor <= 49){
			winStatistics = ganoDiff7_49;
		}else if ((double)valor >49){
			winStatistics = ganoDiff49mas;
		}
		return winStatistics;
	}

	/* (non-Javadoc)
	 * @see com.wireless.soft.indices.cfd.statistics.IStatistics#getLostStatistics(java.lang.Object)
	 */
	@Override
	//=IF(AND(R6>0,R6<=2),"Diff(0-2)",IF(AND(R6>2,R6<=7),"Diff(2-7)",IF(AND(R6>7,R6<=49),"Diff(7-49)",IF(R6>49,"Diff(49Mas)","Clasificarmne"))))
	public Double getLostStatistics(Object valor) {
		Double lostStatistics = null;
		if ((double)valor >0 && (double)valor <= 2){
			lostStatistics = perdioDiff0_2;
		}else if ((double)valor >2 && (double)valor <= 7){
			lostStatistics = perdioDiff2_7;
		}else if ((double)valor >7 && (double)valor <= 49){
			lostStatistics = perdioDiff7_49;
		}else if ((double)valor >49){
			lostStatistics = perdioDiff49mas;
		}
		return lostStatistics;
	
	}

}
