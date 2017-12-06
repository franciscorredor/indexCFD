package com.wireless.soft.indices.cfd.collections;

import java.util.Calendar;

import com.wireless.soft.indices.cfd.util.UtilGeneral;


/**
 * @author Francisco
 *
 */
public class CompanyRanking  implements Comparable<CompanyRanking> {  
	


	
	public Long idCompany;
	
	public String companyName;
	
	public double volumePercentageIncrement;
	
	public double pricePercentageincrement;
	
	private double OBV;
	
	private double notaPonderada;
	
	private double precioEvaluado;
	private double volumenEvaluado;
	
	private double dayHigh;
	
	private double dayLow;
	
	private Calendar fechaIteracion1;
	
	private Calendar fechaIteracion2;
	
	/**
	 * 
	 */
	private double peRatio;
	
	private String Capitalization;
	
	private String symbol;
	
	/*
	 * Year return from Bloomberg
	 */
	private String yearReturn;
	
	/*
	 * Year return evaluado por el sistema
	 */
	/*
	 * FIXME: Adicionarme en el Datamning
	 */
	private double YTD;
	


	/**
	 * @return the idCompany
	 */
	public Long getIdCompany() {
		return idCompany;
	}

	/**
	 * @param idCompany the idCompany to set
	 */
	public void setIdCompany(Long idCompany) {
		this.idCompany = idCompany;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the volumePercentageIncrement
	 */
	public double getVolumePercentageIncrement() {
		return volumePercentageIncrement;
	}

	/**
	 * @param volumePercentageIncrement the volumePercentageIncrement to set
	 */
	public void setVolumePercentageIncrement(double volumePercentageIncrement) {
		this.volumePercentageIncrement = volumePercentageIncrement;
	}

	/**
	 * @return the pricePercentageincrement
	 */
	public double getPricePercentageincrement() {
		return pricePercentageincrement;
	}

	/**
	 * @param pricePercentageincrement the pricePercentageincrement to set
	 */
	public void setPricePercentageincrement(double pricePercentageincrement) {
		this.pricePercentageincrement = pricePercentageincrement;
	}
	
	/**
	 * @return the oBV
	 */
	public double getOBV() {
		return OBV;
	}

	
	public void setPrecioEvaluado(double precioEvaluado) {
		this.precioEvaluado = precioEvaluado;
	}
	
	public double getPrecioEvaluado() {
		return this.precioEvaluado;
	}

	/**
	 * @return the volumenEvaluado
	 */
	public double getVolumenEvaluado() {
		return volumenEvaluado;
	}

	/**
	 * @param volumenEvaluado the volumenEvaluado to set
	 */
	public void setVolumenEvaluado(double volumenEvaluado) {
		this.volumenEvaluado = volumenEvaluado;
	}

	/**
	 * @param oBV the oBV to set
	 */
	public void setOBV(double oBV) {
		OBV = oBV;
	}

	/**
	 * @return the notaPonderada
	 */
	public double getNotaPonderada() {
		
		float calificacionVariable1 = (float)  0.40; 
		float calificacionVariable2 = (float)  0.10;
		float calificacionVariable3 = (float)  0.50;
		//40%  precio
		//10% volumen
		//50% PERatio
		this.notaPonderada =  this.pricePercentageincrement  * calificacionVariable1
							+ this.volumePercentageIncrement  * calificacionVariable2
							+ this.peRatio  * calificacionVariable3; 
		
		return notaPonderada;
	}

	/**
	 * @param notaPonderada the notaPonderada to set
	 */
	public void setNotaPonderada(double notaPonderada) {
		this.notaPonderada = notaPonderada;
	}

	/**
	 * @return the dayHigh
	 */
	public double getDayHigh() {
		return dayHigh;
	}

	/**
	 * @param dayHigh the dayHigh to set
	 */
	public void setDayHigh(double dayHigh) {
		this.dayHigh = dayHigh;
	}

	/**
	 * @return the dayLow
	 */
	public double getDayLow() {
		return dayLow;
	}

	/**
	 * @param dayLow the dayLow to set
	 */
	public void setDayLow(double dayLow) {
		this.dayLow = dayLow;
	}

	/**
	 * @return the fechaIteracion1
	 */
	public Calendar getFechaIteracion1() {
		return fechaIteracion1;
	}

	/**
	 * @param fechaIteracion1 the fechaIteracion1 to set
	 */
	public void setFechaIteracion1(Calendar fechaIteracion1) {
		this.fechaIteracion1 = fechaIteracion1;
	}

	/**
	 * @return the fechaIteracion2
	 */
	public Calendar getFechaIteracion2() {
		return fechaIteracion2;
	}

	/**
	 * @param fechaIteracion2 the fechaIteracion2 to set
	 */
	public void setFechaIteracion2(Calendar fechaIteracion2) {
		this.fechaIteracion2 = fechaIteracion2;
	}

	/**
	 * @return the peRatio
	 */
	public double getPeRatio() {
		return peRatio;
	}

	/**
	 * @param peRatio the peRatio to set
	 */
	public void setPeRatio(double peRatio) {
		this.peRatio = peRatio;
	}

	/**
	 * @return the capitalization
	 */
	public String getCapitalization() {
		return Capitalization;
	}

	/**
	 * @param capitalization the capitalization to set
	 */
	public void setCapitalization(String capitalization) {
		Capitalization = capitalization;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the yearReturn
	 */
	public String getYearReturn() {
		return yearReturn;
	}

	/**
	 * @param yearReturn the yearReturn to set
	 */
	public void setYearReturn(String yearReturn) {
		this.yearReturn = yearReturn;
	}
	
	/**
	 * @return the yTD
	 */
	public double getYTD() {
		return YTD;
	}

	/**
	 * @param yTD the yTD to set
	 */
	public void setYTD(double yTD) {
		YTD = yTD;
	}

	/**
	 * @param compareCR
	 * @return
	 * Compara Ponderado y volumen
	 */
	@Override
	public int compareTo(CompanyRanking compareCR) {
		
		if(this == compareCR){
	        return 0;
	    }

		
		int value1 = 0;
		try{
		int comparePonderado = (int) compareCR.getNotaPonderada();
		int evaluarNotaPonderada = (int) this.getNotaPonderada();
		
		
		
		value1 = comparePonderado - evaluarNotaPonderada;
		
		return value1;
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1;
		
	}
	



	
	
	@Override
    public String toString() {
		
	int printPrecioEvaluado = (int)this.precioEvaluado % 10;
		
	StringBuffer s = new StringBuffer();
	s.append("\n idCompany [" + this.idCompany + "]");
	s.append("\n companyName [" + this.companyName + "]");
	//s.append(" OBV [" + this.OBV + "]");
	s.append(" pricePercentageincrement [" + UtilGeneral.printNumberFormat(this.pricePercentageincrement, "###.###") + "]");
	s.append(" volumePercentageIncrement [" + UtilGeneral.printNumberFormat(this.volumePercentageIncrement, "###.###") + "]");
	s.append("\n notaPonderada [" + this.getNotaPonderada() + "]");
	s.append(" precioEvaluado [" + this.precioEvaluado + (printPrecioEvaluado==7?"Termina en 7*":"")  + "]" );
	s.append(" volumenEvaluado [" + this.volumenEvaluado + "]");
	s.append(" dayHigh [" + this.dayHigh + "]");
	s.append(" dayLow [" + this.dayLow + "]");
	s.append("\n isPriceBetweenHighLow [" + this.isPriceBetweenHighLow() + "]");
	s.append(" fechaIteracion1 [" +  UtilGeneral.printFormat(this.fechaIteracion1, "yyyy-MM-dd HH:mm:ss")  + "]"); 
	s.append(" fechaIteracion2 [" + UtilGeneral.printFormat(this.fechaIteracion2, "yyyy-MM-dd HH:mm:ss")  + "]");
	s.append("\n Capitalization [" + this.Capitalization + "]");
	s.append(" peRatio [" + this.peRatio + "]");
	s.append(" symbol [" + this.symbol + "]");
	s.append("\n yearReturn Bloomberg [" + this.yearReturn + "]");
	s.append("\n YTD Plataforma FrancisCorredor [" + this.YTD + "]");
	return s.toString();
    }
	
	
	public String printToChart() {
		StringBuffer s = new StringBuffer();
		s.append("\n companyName [" + this.companyName + "]");
		s.append(" precioEvaluado [" + this.precioEvaluado + "]");
		s.append(" pricePercentageincrement [" + UtilGeneral.printNumberFormat(this.pricePercentageincrement, "###.###") + "]");
		s.append("\n notaPonderada [" + this.getNotaPonderada() + "]");
		s.append(" symbol [" + this.symbol + "]");

		return s.toString();
	    }
	
	/**
	 * @return
	 */
	public Boolean isPriceBetweenHighLow(){
		return  UtilGeneral.isPriceBetweenHighLow(this.precioEvaluado, this.dayHigh, this.dayLow);
	}

	

}
