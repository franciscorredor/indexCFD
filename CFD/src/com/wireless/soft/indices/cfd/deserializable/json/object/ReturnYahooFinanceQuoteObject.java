package com.wireless.soft.indices.cfd.deserializable.json.object;

import java.io.Serializable;

import com.wireless.soft.indices.cfd.util.UtilGeneral;

/**
 * @author Francisco
 * 
 * Obtiene en unobjeto el resultado del WebService Yahoo
 * http://query.yahooapis.com/v1/public/yql?q=select%20PERatio%20from%20yahoo.finance.quotes%20where%20symbol%20IN%20(%22LLOY.L%22)&format=json&env=http://datatables.org/alltables.env
 * http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quotes%20where%20symbol%20IN%20(%22LLOY.L%22)&format=json&env=http://datatables.org/alltables.env
 * 
 * @version 20160714
 * 
 * Dejo de funcionar el servicio: http://finance.yahoo.com/webservice/v1/symbols/COALINDIA.NS/quote?format=json
 * Desde ahora se optine toda la informacin de: http://query.yahooapis.com/v1/public/yql?q=select%20symbol,Volume,PriceSale,PriceBook,DividendPayDate,DividendYield,MarketCapitalization,MarketCapRealtime,PERatio,Ask,Bid,PriceSales,EBITDA,PEGRatio,PriceEPSEstimateNextYear,PriceEPSEstimateCurrentYear%20from%20yahoo.finance.quotes%20where%20symbol%20IN%20(%22AIG%22)&format=json&env=http://datatables.org/alltables.env
 * ** Ayuda para vel el nuevo formato: http://jsonviewer.stack.hu/
 */
public class ReturnYahooFinanceQuoteObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5151925176934907761L;
	
	// ////////////////////////////////////////////////////////////////////////
	// Atributos de la clase
	// ////////////////////////////////////////////////////////////////////////
	
	private Query query;
	
	/**
	 * @return the query
	 */
	public Query getQuery() {
		return query;
	}


	/**
	 * @param query the query to set
	 */
	public void setQuery(Query query) {
		this.query = query;
	}

	
	public class Query{
		private String count;
		
		private String created;
		
		private String lang;
		
		private Results results;
		
		/**
		 * @return the count
		 */
		public String getCount() {
			return count;
		}


		/**
		 * @param count the count to set
		 */
		public void setCount(String count) {
			this.count = count;
		}
		
		
		/**
		 * @return the created
		 */
		public String getCreated() {
			return created;
		}


		/**
		 * @param created the created to set
		 */
		public void setCreated(String created) {
			this.created = created;
		}


		/**
		 * @return the lang
		 */
		public String getLang() {
			return lang;
		}


		/**
		 * @param lang the lang to set
		 */
		public void setLang(String lang) {
			this.lang = lang;
		}


		/**
		 * @return the results
		 */
		public Results getResults() {
			return results;
		}


		/**
		 * @param results the results to set
		 */
		public void setResults(Results results) {
			this.results = results;
		}


		public class Results{
			
			/**
			 * @return the quote
			 */
			public Quote getQuote() {
				return quote;
			}

			/**
			 * @param quote the quote to set
			 */
			public void setQuote(Quote quote) {
				this.quote = quote;
			}

			private Quote quote;
			
			public class Quote{
				private String PERatio;
				private String Bid;
				private String EBITDA;
				private String PriceSales;
				private String PriceEPSEstimateCurrentYear;
				private String PriceEPSEstimateNextYear;
				private String Ask;
				private String PEGRatio;
				private String MarketCapitalization;
				private String MarketCapRealtime;
				/*
				 * Nuevos atributos desde 14Jul2016
				 */
				private String Volume;
				private String PriceSale;
				private String PriceBook;
				private String DividendPayDate;
				private String DividendYield;
				private String symbol;
				private String YearLow;
				private String YearHigh;
				private String DaysLow;
				private String DaysHigh;
				private String Change;
				
				private String LastTradePriceOnly;

				/**
				 * @return the pERatio
				 */
				public String getPERatio() {
					if (null == PERatio){
						return PERatio;
					}else{
						PERatio = PERatio.trim();
						return PERatio.equals("-")?null:PERatio; 
					}
					
				}

				/**
				 * @param pERatio the pERatio to set
				 */
				public void setPERatio(String pERatio) {
					PERatio = pERatio;
				}
				
				public String getBid() {
					return Bid;
				}

				public void setBid(String bid) {
					Bid = bid;
				}

				public String getEBITDA() {
					return EBITDA;
				}

				public void setEBITDA(String eBITDA) {
					EBITDA = eBITDA;
				}

				public String getPriceSales() {
					return PriceSales;
				}

				public void setPriceSales(String priceSales) {
					PriceSales = priceSales;
				}

				public String getPriceEPSEstimateCurrentYear() {
					return PriceEPSEstimateCurrentYear;
				}

				public void setPriceEPSEstimateCurrentYear(String priceEPSEstimateCurrentYear) {
					PriceEPSEstimateCurrentYear = priceEPSEstimateCurrentYear;
				}

				public String getPriceEPSEstimateNextYear() {
					return PriceEPSEstimateNextYear;
				}

				public void setPriceEPSEstimateNextYear(String priceEPSEstimateNextYear) {
					PriceEPSEstimateNextYear = priceEPSEstimateNextYear;
				}

				public String getAsk() {
					return Ask;
				}

				public void setAsk(String ask) {
					Ask = ask;
				}

				/**
				 * @return the pEGRatio
				 */
				public String getPEGRatio() {
					return PEGRatio;
				}

				/**
				 * @param pEGRatio the pEGRatio to set
				 */
				public void setPEGRatio(String pEGRatio) {
					PEGRatio = pEGRatio;
				}

				/**
				 * @return the marketCapitalization
				 */
				public String getMarketCapitalization() {
					return MarketCapitalization;
				}

				/**
				 * @param marketCapitalization the marketCapitalization to set
				 */
				public void setMarketCapitalization(String marketCapitalization) {
					MarketCapitalization = marketCapitalization;
				}

				/**
				 * @return the marketCapRealtime
				 */
				public String getMarketCapRealtime() {
					return MarketCapRealtime;
				}

				/**
				 * @param marketCapRealtime the marketCapRealtime to set
				 */
				public void setMarketCapRealtime(String marketCapRealtime) {
					MarketCapRealtime = marketCapRealtime;
				}

				/**
				 * @return the volume
				 */
				public String getVolume() {
					if (this.Volume == null){
						return Volume;
					}else{
						Volume = this.Volume.trim();
						String mult = null;
						String value = null;
						
						
						 //Matcher matcher = "E[1-9]".matcher(Volume);
						if (Volume.indexOf("E") > 0 && Volume.indexOf("P/E") < 0){
							value = Volume.substring(0, Volume.indexOf("E"));
							int vz = Integer.valueOf(Volume.substring(Volume.indexOf("E")+1,Volume.length()));
							String zrs = "1";
							for (int i= 0; i<vz;i++){
								zrs = zrs + "0";
							}
							try{
								double conv = Double.parseDouble(value) * Integer.valueOf(zrs);
								
								Volume = UtilGeneral.printNumberFormat(conv, "###.###");
							}catch (Exception e){
								Volume = null;
								//System.out.println("Error al obtener el volumen" + e.getMessage());
								//e.printStackTrace();
							}
							
						}else{
								mult = Volume.substring(Volume.length()-1, Volume.length());
								value = Volume.substring(0, Volume.length()-1);
								int m = 1;
								if (mult.equals("M")){
									m=1000000;
								}else if (mult.equals("B")){
									m=1000000000;
								}else if (mult.equals("k")){
									m=1000;
								}else{
									Volume = value;
								}
								try{
									double conv = Double.parseDouble(value) * m;
									Volume = String.valueOf(conv);
								}catch (Exception e){
									Volume = null;
									//System.out.println("Error al obtener el volumen" + e.getMessage());
									//e.printStackTrace();
								}
						}
						//System.out.println("mult(" + mult +") Volume (" + Volume + ")");
						
						return Volume;	
					}
					
				}

				/**
				 * @param volume the volume to set
				 */
				public void setVolume(String volume) {
					Volume = volume;
				}

				/**
				 * @return the priceSale
				 */
				public String getPriceSale() {
					return PriceSale;
				}

				/**
				 * @param priceSale the priceSale to set
				 */
				public void setPriceSale(String priceSale) {
					PriceSale = priceSale;
				}

				/**
				 * @return the priceBook
				 */
				public String getPriceBook() {
					return PriceBook;
				}

				/**
				 * @param priceBook the priceBook to set
				 */
				public void setPriceBook(String priceBook) {
					PriceBook = priceBook;
				}

				/**
				 * @return the dividendPayDate
				 */
				public String getDividendPayDate() {
					return DividendPayDate;
				}

				/**
				 * @param dividendPayDate the dividendPayDate to set
				 */
				public void setDividendPayDate(String dividendPayDate) {
					DividendPayDate = dividendPayDate;
				}

				/**
				 * @return the dividendYield
				 */
				public String getDividendYield() {
					return DividendYield;
				}

				/**
				 * @param dividendYield the dividendYield to set
				 */
				public void setDividendYield(String dividendYield) {
					DividendYield = dividendYield;
				}

				/**
				 * @return the symbol
				 */
				public String getSymbol() {
					return symbol;
				}

				/**
				 * @param symbol the symbol to set
				 */
				public void setSymbol(String symbol) {
					this.symbol = symbol;
				}

				/**
				 * @return the yearLow
				 */
				public String getYearLow() {
					return YearLow;
				}

				/**
				 * @param yearLow the yearLow to set
				 */
				public void setYearLow(String yearLow) {
					YearLow = yearLow;
				}

				/**
				 * @return the yearHigh
				 */
				public String getYearHigh() {
					return YearHigh;
				}

				/**
				 * @param yearHigh the yearHigh to set
				 */
				public void setYearHigh(String yearHigh) {
					YearHigh = yearHigh;
				}

				/**
				 * @return the daysLow
				 */
				public String getDaysLow() {
					return DaysLow;
				}

				/**
				 * @param daysLow the daysLow to set
				 */
				public void setDaysLow(String daysLow) {
					DaysLow = daysLow;
				}

				/**
				 * @return the daysHigh
				 */
				public String getDaysHigh() {
					return DaysHigh;
				}

				/**
				 * @param daysHigh the daysHigh to set
				 */
				public void setDaysHigh(String daysHigh) {
					DaysHigh = daysHigh;
				}

				/**
				 * @return the change
				 */
				public String getChange() {
					return Change;
				}

				/**
				 * @param change the change to set
				 */
				public void setChange(String change) {
					Change = change;
				}

				/**
				 * @return the lastTradePriceOnly
				 */
				public String getLastTradePriceOnly() {
					return LastTradePriceOnly;
				}

				/**
				 * @param lastTradePriceOnly the lastTradePriceOnly to set
				 */
				public void setLastTradePriceOnly(String lastTradePriceOnly) {
					LastTradePriceOnly = lastTradePriceOnly;
				}

				@Override
			    public String toString() {
				StringBuffer s = new StringBuffer(this.getClass().getCanonicalName());
				if (this.PERatio !=null){
					s.append("\n pERatio [" + this.PERatio.toString() + "]");
				}

				return s.toString();
				}

			}
			
			@Override
		    public String toString() {
			StringBuffer s = new StringBuffer(this.getClass().getCanonicalName());
			if (this.quote !=null){
				s.append("\n quote [" + this.quote.toString() + "]");
			}

			return s.toString();
			}
		}
		
		@Override
	    public String toString() {
		StringBuffer s = new StringBuffer(this.getClass().getCanonicalName());
		s.append("\n count [" + this.count + "]");
		s.append(" created [" + this.created + "]");
		s.append(" lang [" + this.lang + "]");
		if (this.results !=null){
			s.append("\n results [" + this.results.toString() + "]");
		}

		return s.toString();
		}


		
	}

	@Override
    public String toString() {
	StringBuffer s = new StringBuffer(this.getClass().getCanonicalName());
	if (this.query !=null){
		s.append("\n query [" + this.query.toString() + "]");
	}

	return s.toString();
	}

	

}
