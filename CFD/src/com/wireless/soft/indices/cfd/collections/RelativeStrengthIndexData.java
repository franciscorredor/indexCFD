package com.wireless.soft.indices.cfd.collections;

import java.util.Date;

/**
 * @author Francisco
 *
 *REf BASE:
 *	http://real-chart.finance.yahoo.com/table.csv?s=M&888.L&d=0&e=10&f=2016&g=d&a=11&b=18&c=2015&ignore=.csv
 */
public class RelativeStrengthIndexData   implements Comparable<RelativeStrengthIndexData> {
	
	private long id;
	
	private long idCompany;
	
	private Date fecha;
	
	private double close;
	
	private double change;
	
	private double gain;
	
	private double loss;
	
	private double avgGain;
	
	private double avgLoss;
	
	private double rs;
	
	private double fourteenDayRSI;
	
	private double high;
	
	private double low;
	
	
	/*
	 * Getters and 
	 */

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(long idCompany) {
		this.idCompany = idCompany;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getChange() {
		return change;
	}

	public void setChange(double change) {
		this.change = change;
	}

	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}

	public double getLoss() {
		return loss;
	}

	public void setLoss(double loss) {
		this.loss = loss;
	}

	public double getAvgGain() {
		return avgGain;
	}

	public void setAvgGain(double avgGain) {
		this.avgGain = avgGain;
	}

	public double getAvgLoss() {
		return avgLoss;
	}

	public void setAvgLoss(double avgLoss) {
		this.avgLoss = avgLoss;
	}

	public double getRs() {
		return rs;
	}

	public void setRs(double rs) {
		this.rs = rs;
	}

	public double getFourteenDayRSI() {
		return fourteenDayRSI;
	}

	public void setFourteenDayRSI(double fourteenDayRSI) {
		this.fourteenDayRSI = fourteenDayRSI;
	}
	
	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	@Override
    public String toString() {
	StringBuffer s = new StringBuffer();
	
	s.append("\n id [" + this.id + "]");
	s.append(" close [" + this.close + "]");
	s.append(" change [" + this.change + "]");
	s.append(" fecha [" + this.fecha + "]");
	return s.toString();
    }
	

	@Override
	public int compareTo(RelativeStrengthIndexData compareSID) {
		
		if(this == compareSID){
	        return 0;
	    }

		
		int value1 = 0;
		try{
		int compareOrden = (int) compareSID.getId();
		int evaluarOrden = (int) this.getId();
		
		
		
		value1 = compareOrden - evaluarOrden;
		
		return value1;
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1;
		
	}

}
