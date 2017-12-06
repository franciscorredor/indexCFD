package com.wireless.soft.indices.cfd.deserializable.json.object;

import java.io.Serializable;


/**
 * @author Francisco
 * 
 * Obtiene en un objeto el resultado del WebService Yahoo
 * http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22YHOO%22%20and%20startDate%20%3D%20%222016-07-01%22%20and%20endDate%20%3D%20%222016-08-04%22&format=json&env=http://datatables.org/alltables.env
		yahoo.finance.historicaldata
			http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22YHOO%22%20and%20startDate%20%3D%20%222009-09-11%22%20and%20endDate%20%3D%20%222010-03-10%22&format=json&env=http://datatables.org/alltables.env
			http://developer.yahoo.com/yql/console/?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22YHOO%22%20and%20startDate%20%3D%20%222009-09-11%22%20and%20endDate%20%3D%20%222010-03-10%22&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys
 * 
 * @version 20160803
 * 
 * ** Ayuda para vel el formato: http://jsonviewer.stack.hu/
 */
public class ReturnHistoricaldataYahooFinance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -504299423059500220L;

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
			
			//Expected BEGIN_OBJECT but was BEGIN_ARRAY --> private Quote quote;
			private Quote[] quote;
			
			/**
			 * @return the quote
			 */
			public Quote[] getQuote() {
				return quote;
			}

			/**
			 * @param quote the quote to set
			 */
			public void setQuote(Quote[] quote) {
				this.quote = quote;
			}

			
			public class Quote{
				private String Symbol;
				private String Date;
				private String Open;
				private String High;
				private String Low;
				private String Close;			
				private String Volume;
				private String Adj_Close;
				
				/**
				 * @return the symbol
				 */
				public String getSymbol() {
					return Symbol;
				}

				/**
				 * @param symbol the symbol to set
				 */
				public void setSymbol(String symbol) {
					Symbol = symbol;
				}

				/**
				 * @return the date
				 */
				public String getDate() {
					return Date;
				}

				/**
				 * @param date the date to set
				 */
				public void setDate(String date) {
					Date = date;
				}

				/**
				 * @return the open
				 */
				public String getOpen() {
					return Open;
				}

				/**
				 * @param open the open to set
				 */
				public void setOpen(String open) {
					Open = open;
				}

				/**
				 * @return the high
				 */
				public String getHigh() {
					return High;
				}

				/**
				 * @param high the high to set
				 */
				public void setHigh(String high) {
					High = high;
				}

				/**
				 * @return the low
				 */
				public String getLow() {
					return Low;
				}

				/**
				 * @param low the low to set
				 */
				public void setLow(String low) {
					Low = low;
				}

				/**
				 * @return the close
				 */
				public String getClose() {
					return Close;
				}

				/**
				 * @param close the close to set
				 */
				public void setClose(String close) {
					Close = close;
				}

				/**
				 * @return the volume
				 */
				public String getVolume() {
					return Volume;
				}

				/**
				 * @param volume the volume to set
				 */
				public void setVolume(String volume) {
					Volume = volume;
				}

				/**
				 * @return the adj_Close
				 */
				public String getAdj_Close() {
					return Adj_Close;
				}

				/**
				 * @param adj_Close the adj_Close to set
				 */
				public void setAdj_Close(String adj_Close) {
					Adj_Close = adj_Close;
				}

				@Override
			    public String toString() {
				StringBuffer s = new StringBuffer();//new StringBuffer(this.getClass().getCanonicalName());
				/*if (this.Symbol !=null){
					s.append(" Symbol [" + this.Symbol.toString() + "]");
				}*/
				//Date	Open	High	Low	Close	Adj Close
				if (this.Date !=null){
					s.append(this.Date.toString() + ",");
				}else s.append(",");
				
				if (this.Open !=null){
					s.append(this.Open.toString() + ",");
				}else s.append(",");
				
				if (this.High !=null){
					s.append(this.High.toString() + ",");
				}else s.append(",");
				
				if (this.Low !=null){
					s.append(this.Low.toString() + ",");
				}else s.append(",");

				if (this.Close !=null){
					s.append(this.Close.toString() + ",");
				}else s.append(",");
				
				if (this.Adj_Close !=null){
					s.append(this.Adj_Close.toString());
				}else s.append("");
				
				
				
				return s.toString();
				}

			}
			
			
			@Override
		    public String toString() {
			StringBuffer s = new StringBuffer("");
			if (this.getQuote() !=null){
				int itera = 0;
				for (Quote q : this.getQuote()) {
					s.append("\n " + q.toString() );
				}
				
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
