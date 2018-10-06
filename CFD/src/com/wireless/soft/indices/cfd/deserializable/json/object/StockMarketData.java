package com.wireless.soft.indices.cfd.deserializable.json.object;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Francisco
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"AfterHoursSeries",
"Ct",
"Eeq",
"EqTkr",
"Ert",
"Fi",
"SecType",
"Series",
"Tkr",
"Ycp",
"utcFullRunTime"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockMarketData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8262274541551511984L;
	//private List<Object> afterHoursSeries = null;
	
	@JsonProperty("Ct")
	private String Ct;
	@JsonProperty("Eeq")
	private String Eeq;
	@JsonProperty("EqTkr")
	private String EqTkr;
	@JsonProperty("Ert")
	private String Ert;
	@JsonProperty("Fi")
	private String Fi;
	@JsonProperty("SecType")
	private String SecType;
	@JsonProperty("Series")
	private List<SeriesObj> Series = null;
	@JsonProperty("Tkr")
	private String Tkr;
	@JsonProperty("Ycp")
	private Double Ycp;
	@JsonProperty("utcFullRunTime")
	private String utcFullRunTime;
	public String getCt() {
		return Ct;
	}
	public void setCt(String ct) {
		Ct = ct;
	}

	public String getUtcFullRunTime() {
		return utcFullRunTime;
	}
	public void setUtcFullRunTime(String utcFullRunTime) {
		this.utcFullRunTime = utcFullRunTime;
	}
	/**
	 * @return the eeq
	 */
	public String getEeq() {
		return Eeq;
	}
	/**
	 * @param eeq the eeq to set
	 */
	public void setEeq(String eeq) {
		Eeq = eeq;
	}
	/**
	 * @return the eqTkr
	 */
	public String getEqTkr() {
		return EqTkr;
	}
	/**
	 * @param eqTkr the eqTkr to set
	 */
	public void setEqTkr(String eqTkr) {
		EqTkr = eqTkr;
	}
	/**
	 * @return the ert
	 */
	public String getErt() {
		return Ert;
	}
	/**
	 * @param ert the ert to set
	 */
	public void setErt(String ert) {
		Ert = ert;
	}
	/**
	 * @return the fi
	 */
	public String getFi() {
		return Fi;
	}
	/**
	 * @param fi the fi to set
	 */
	public void setFi(String fi) {
		Fi = fi;
	}
	/**
	 * @return the secType
	 */
	public String getSecType() {
		return SecType;
	}
	/**
	 * @param secType the secType to set
	 */
	public void setSecType(String secType) {
		SecType = secType;
	}

	/**
	 * @return the tkr
	 */
	public String getTkr() {
		return Tkr;
	}
	/**
	 * @param tkr the tkr to set
	 */
	public void setTkr(String tkr) {
		Tkr = tkr;
	}
	/**
	 * @return the ycp
	 */
	public Double getYcp() {
		return Ycp;
	}
	/**
	 * @param ycp the ycp to set
	 */
	public void setYcp(Double ycp) {
		Ycp = ycp;
	}
	/**
	 * @return the series
	 */
	public List<SeriesObj> getSeries() {
		return Series;
	}
	/**
	 * @param series the series to set
	 */
	public void setSeries(List<SeriesObj> series) {
		Series = series;
	}
	
	


	


}
