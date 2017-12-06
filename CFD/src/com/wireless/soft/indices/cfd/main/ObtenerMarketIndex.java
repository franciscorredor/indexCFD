package com.wireless.soft.indices.cfd.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wireless.soft.indices.cfd.business.adm.AdminEntity;
import com.wireless.soft.indices.cfd.business.entities.BloombergIndex;
import com.wireless.soft.indices.cfd.business.entities.Company;
import com.wireless.soft.indices.cfd.business.entities.DataMiningCompany;
import com.wireless.soft.indices.cfd.business.entities.FundamentalHistoryCompany;
import com.wireless.soft.indices.cfd.business.entities.QuoteHistoryCompany;
import com.wireless.soft.indices.cfd.collections.CompanyRanking;
import com.wireless.soft.indices.cfd.collections.RelativeStrengthIndexData;
import com.wireless.soft.indices.cfd.deserializable.json.object.ReturnHistoricaldataYahooFinance;
import com.wireless.soft.indices.cfd.deserializable.json.object.ReturnYahooFinanceQuoteObject;
import com.wireless.soft.indices.cfd.exception.BusinessException;
import com.wireless.soft.indices.cfd.statistics.DiffMaxMin;
import com.wireless.soft.indices.cfd.statistics.IStatistics;
import com.wireless.soft.indices.cfd.statistics.LastDigit;
import com.wireless.soft.indices.cfd.statistics.NotaPonderada;
import com.wireless.soft.indices.cfd.statistics.PercentageIncremento;
import com.wireless.soft.indices.cfd.statistics.PrecioAccion;
import com.wireless.soft.indices.cfd.statistics.PriceBetweenHighLow;
import com.wireless.soft.indices.cfd.statistics.PriceEarningRatio;
import com.wireless.soft.indices.cfd.statistics.PricePercentageIncrement;
import com.wireless.soft.indices.cfd.statistics.RelativeStrengthIndex;
import com.wireless.soft.indices.cfd.statistics.StockMayorMedia;
import com.wireless.soft.indices.cfd.statistics.TendenciaTresMeses;
import com.wireless.soft.indices.cfd.util.UtilGeneral;

/**
 * Clase principal encargada de obtener los indices de diferentes compaias 
 * @author Francisco Corredor
 * https://sites.google.com/site/gson/gson-user-guide
 * TODO
 * Conseguir BD en la nube y realizar lgica de negocio en CLOUD
 */

enum Evalua {ONE, THREE}

enum TENDENCIA {minusalza, minusbaja, ALZA, BAJA, NO_EVALUADA;}

public class ObtenerMarketIndex {
	
	// ////////////////////////////////////////////////////////////////////////
    // Atributos de la clase
    // ////////////////////////////////////////////////////////////////////////
    /** */
    private final Gson gson = this.createGson();
    
    private AdminEntity admEnt = null;
    
    private static int WAIT_TIME = 3500;
    
    private static int diasIntentos = -1;
    
    private int variableGlobalIntentos = 0;
    
    /*
     * El sistema esta generando un out of memory
     * porque esta creando varias instancias de los objetos
     * por el loop, la idea es reutilizar los objetos
     * para que la JVM, no los tenga que crear de nuevo
     * y verificar el garbage collector,
     */
    private static List<CompanyRanking> cr;
    private static ReturnYahooFinanceQuoteObject ri;
    
    
    
    
    
    public ObtenerMarketIndex(){
    	admEnt = AdminEntity.getInstance();
    }

	
	
	/**
	 * @param args
	 * args[0]  --> Persistir o consultar (1/0)
	 * args[1]  --> Numero de iteraciones anteriores a ver
	 * args[2]  --> Porcentaje [1-100], indicador del TOP de las mejores compa_ias a imprimir, dependiendo del porcentaje
	 * Samples:
	 * 	java -jar indicesToCFD.jar 1 2 10
	 *  java -jar indicesToCFD.jar 0
	 *  java -jar indicesToCFD.jar 3 1  ===> imprime chart cada minuto de la compania 1
	 */
	public static void main(String[] args) {
		
		diasIntentos =-1;
		
		/*if (isValidTime()){
			System.out.println("Tiempo evaluacion termino, contactar a herbert.andes@gmail.com");
			System.exit(0);
		}*/
		
		
		
		if (null == args || args.length < 1){
			System.out.println("Debe especificar un argumento");
			return;
		}
		else{
				System.out.println("ini Proceso " + new Date());
			}
		
		Integer argumento2 = null;
		Integer cortePorcentajePonderado = null;
		PropertyConfigurator.configure("log4j.properties");
		//Valida si hay un 2do argumento 
		//		i.  	# Iteraciones antes
		//		ii.		Id compania	
		if (null != args && args.length >1 && null != args[1]){
			try{
				argumento2 = Integer.parseInt( args[1] );
			}catch (Exception e){
				argumento2 = null;
			}
		}
		//Valida si hay un 3er argumento (# Corte pocentaje ponderado)
		if (null != args && args.length >2 && null != args[2]){
			try{
				cortePorcentajePonderado = Integer.parseInt( args[2] );
			}catch (Exception e){
				cortePorcentajePonderado = null;
			}
		}
		
		//Inicializa la clase principal.
		ObtenerMarketIndex omi = new ObtenerMarketIndex();
//		if (args[0].equals("test")) {
//			Company cmp = new Company();
//			cmp.setId(609l);
//			omi.obtenerReturnIndex(cmp);
//		}
		
		/*
		if (args[0].equals("test")) {
			try {
				//ReturnHistoricaldataYahooFinance tr =	omi.executeYahooIndexHistoricaldata("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22YHOO%22%20and%20startDate%20%3D%20%222016-07-01%22%20and%20endDate%20%3D%20%222016-08-04%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=");
				//System.out.print( tr.toString() );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/		
		
		
		
		
		try {
			String accion = args[0];
			switch (accion){
			case "0":
				runManytimes(omi,argumento2,cortePorcentajePonderado);
				break;
			case "1":
				System.out.println("\n Imprime el indicador OBV");
				System.out.println("Precio accion menor = & % volumen mayor a cero!" + "Time:" + new Date());
				omi.printOBV(argumento2, cortePorcentajePonderado, Evalua.ONE);
				System.out.println("Precio accion mayor & % volumen mayor a cero!"+ "Time:" + new Date());
				omi.printOBV(argumento2, cortePorcentajePonderado, Evalua.THREE);
				break;
			case "2":
				System.out.println("\n Indica si el mecado esta en Bull o Bear");
				omi.printIndicadorMercado();
				break;
			case "3":
				System.out.println("\n Print Chart Company UP&Down Price");
				omi.printChartCompany(argumento2);
				break;
			case "4":
				System.out.println("\n Persistir cada 10 minutos informacion de la companias");
				omi.persisteVariasIteraciones();
				break;
			case "5":
				System.out.println("\n test Call PE Ratio");
				omi.printPERatio();
				break;	
			case "6":
				System.out.println("\n Call relativeStrengthIndex");
				omi.relativeStrengthIndex();
				break;
			case "7":
				System.out.println("\n Call relativeStrengthIndex: --> [" + args[1] + "]");
				//El identificador  0 es que no tiene iteracion y no debe almacenar ningun tipo
				//de informacion para el Datamining
				omi.relativeStrengthIndexFromGoogle(args[1], 0, true, "0");
				break;	
			case "8":
				System.out.println("\n Persiste info de las companies, consultando de yahoo [Go short]");
				omi.printPERatio();
				System.out.println("Precio accion menor = & % volumen mayor a cero!" +"Time:" + new Date());
				omi.printOBVGoShort(argumento2, cortePorcentajePonderado, Evalua.ONE);
				System.out.println("Precio accion mayor & % volumen mayor a cero!" +"Time:" + new Date());
				omi.printOBVGoShort(argumento2, cortePorcentajePonderado, Evalua.THREE);
				break;
			case "9":
				System.out.println("\n Obtener indicador YTD (Regla de tres con respecto al inicio del year) --> [" + args[1] + "] | "+omi.getYearToDateReturn(args[1]));
				break;
			case "10":
				System.out.println("\n Obtener historico de n companias, definiendo el dia en q empieza la iteracion \nsample:java -jar indicesToCFD.jar 10 JUP.L;NEO.PA;APC.DE;HSX.L -1 \n");
				omi.relativeStrengthIndexArray(args[1], args[2], args[3]);
				break;	
			case "11":
				System.out.println("\n Obtener tendencia de la compania en n meses (0) - alza 	(1)	- baja		(2)	Alza		(3)	Baja \n");
				System.out.println(" " + omi.getTendenciaGoogle(args[1],  Integer.parseInt( args[2] )));
				break;
			case "12":
				System.out.println("\n Evaluar data mining statistical modeling ");
				omi.getStatisticalModeling(Long.parseLong( args[1] ));
				break;
				
							
			default:
				System.out.println("\n No realiza acci_n");
				break;
			}
			
			
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (BusinessException be) {
			be.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		System.out.println("fin Proceso " + new Date());
		
		System.exit(0);
	}
	
	
	/**
	 * @param url
	 * @return el Quote de yahooIndex
	 * @throws IOException
	 * @throws Exception 
	 */
	private ReturnYahooFinanceQuoteObject executeYahooIndexQuoteHTML(String url)
			throws IOException {

		ReturnYahooFinanceQuoteObject.Query.Results.Quote q = new ReturnYahooFinanceQuoteObject().new Query().new Results().new Quote();
		ReturnYahooFinanceQuoteObject.Query.Results r = new ReturnYahooFinanceQuoteObject().new Query().new Results();
		ReturnYahooFinanceQuoteObject.Query qry = new ReturnYahooFinanceQuoteObject().new Query();
		ReturnYahooFinanceQuoteObject y = new ReturnYahooFinanceQuoteObject();
		// url =
		// "https://www.msn.com/en-us/money/stockdetails/analysis/fi-160.1.BNP.PAR";
		// url =
		// "https://www.msn.com/en-us/money/stockdetails/analysis/fi-125.1.KOEBF.GREY";

		try {
			// -----------------------------------------------------------
			Document doc;
			doc = Jsoup.connect(url).timeout(10000).get();

			Elements newsHeadlines = doc.select("p.truncated-string");
			Elements newsHeadlines2 = doc.select("div.precurrentvalue");

			/*
			 * String[] strSplit = newsHeadlines.text().split(" ");
			 * q.setBid(strSplit[0]); q.setAsk(strSplit[0]);
			 * q.setPERatio(strSplit[15]);
			 * 
			 * url = "https://uk.finance.yahoo.com/quote/BNP.PA/news?p=BNP.PA";
			 * 
			 * Elements newsHeadlines = doc
			 * //.select("span[src~=Trsdu(0.3s) Fw(b) Fz(36px) Mb(-4px) D(ib)]"
			 * ); //.select("img[src~=(?i)\\.(png|jpe?g)]");
			 * //.select("span[class~=(Trsdu)+]");
			 * .select("span[class~=(Trsdu)+]");
			 */
			//System.out.println(" --> : " + newsHeadlines.text());
			//System.out.println(" --> : " + newsHeadlines2.text());

			String peratio = null;
			String marketCap = null;
			String volume = null;
			String dayRange = null;

			if (newsHeadlines != null) {
				try {
					peratio = newsHeadlines.text()
							.substring(
									newsHeadlines.text().indexOf(
											"P/E Ratio (EPS)") + 16,
									newsHeadlines.text().indexOf(
											"(", newsHeadlines.text().indexOf(
													"P/E Ratio (EPS)") + 16));
				} catch (Exception e) {}
				try {
					marketCap = newsHeadlines.text()
							.substring(
									newsHeadlines.text().indexOf(
											"Market Cap.") + 12,
									newsHeadlines.text().indexOf(
											"Dividend Rate (Yield)"));
				} catch (Exception e) {}
				try {
					volume = newsHeadlines.text()
							.substring(
									newsHeadlines.text().indexOf(
											"Volume (Avg)") + 13,
									newsHeadlines.text().indexOf(
											"(", newsHeadlines.text().indexOf(
													"Volume (Avg)") + 13));
				} catch (Exception e) {}
				try {
					dayRange = newsHeadlines.text()
							.substring(
									newsHeadlines.text().indexOf(
											"Day's Range") + 12,
									newsHeadlines.text().indexOf(
											"52Wk Range"));
				} catch (Exception e) {}
			}

			//System.out.println("peratio: " + peratio == null ? null : peratio.trim());
			q.setBid(newsHeadlines2.text() == null ? null : newsHeadlines2
					.text().trim().replaceAll(",", ""));
			q.setAsk(newsHeadlines2.text() == null ? null : newsHeadlines2
					.text().trim().replaceAll(",", ""));
			q.setPERatio(peratio);
			q.setMarketCapitalization(marketCap);
			q.setVolume(volume);
			if (dayRange != null && dayRange.split("-").length > 0){
				q.setDaysLow(dayRange.split("-")[0].replaceAll(",", ""));
				q.setDaysHigh(dayRange.split("-")[1].replaceAll(",", ""));
			}


			r.setQuote(q);
			qry.setResults(r);
			y.setQuery(qry);

			return y;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error al leer [" + url
					+ "](FileNotFoundException)");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error al leer [" + url + "](IOException)");
		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;

	}
	
	

	

	/**
	 * @param url
	 * @return
	 * @throws IOException
	 */
	
	
	/**
	 * @param url
	 * @return
	 * @throws IOException
	 */
	/*
	@Deprecated
	private ReturnSingleDataYahooFinance executeYahooIndexSingleData(String url)
			throws IOException {
		try {
			
			JsonElement result = executeJ(url);
			if (result.isJsonObject()) {
				JsonElement error = result.getAsJsonObject().get("error");
				if (error != null) {
					JsonElement code = result.getAsJsonObject().get("code");
					System.out.println("[Error] code:" + code);
				}
			}

			return gson.fromJson(result, ReturnSingleDataYahooFinance.class);

		} catch (Exception e) {
			System.out.println("[" + e.getMessage() + "]Error en ReturnSingleDataYahooFinance executeYahooIndexSingleData:" + url);
			//e.printStackTrace();
		}
		return null;

	}*/
	
	/**
	 * @param url
	 * @return
	 * @throws IOException
	 * Retorna el valor de la copañia
	 */
	private Double executeGoogleIndexSingleData(String url)
			throws IOException {
		
		try(InputStream input = new URL( url ).openStream()) {
			
			
			
			BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		    	
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		        if (null != line){
		        	String[] torsid = line.split(",");
			        
			        return Double.parseDouble(torsid[4]);
			                
			        
		        }
		        
		    }
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			System.out.println("Error al leer ["+url+"](FileNotFoundException)");
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Error al leer ["+url+"](IOException)");
		} 
		
		
		return null;

	}
	
	/**
	 * @param url
	 * @return
	 * @throws IOException
	 * Retorna el valor de la copañia
	 */
	private Double executeGoogleIndexSingleDataByHTML(String url)
			throws IOException {
		
		try {
			
			//-----------------------------------------------------------
			Document doc;
			doc = Jsoup.connect(url).timeout(3000).get();
			Elements newsHeadlines = doc.select("table.gf-table.historical_price");

			for (Element table : newsHeadlines) {
				
				Elements rows = table.select("tr");

				for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
					
				    Element row = rows.get(i);
				    Elements cols = row.select("td");
				    
				    //Open	High	Low	Close	
				    //0		1		2	3
				    
				    	Elements dataValue = cols.select("td.rgt");
			        	String[] torsid = dataValue.text().split(" ");
			        	
			        	//System.out.print("executeGoogleIndexSingleDataByHTML:" + Double.parseDouble(torsid[3]));
			        	
			        	return Double.parseDouble(torsid[3]);

				}
				//System.out.print("[" + table.text() + "]");
			}			
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			System.out.println("Error al leer ["+url+"](FileNotFoundException)");
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Error al leer ["+url+"](IOException)");
		} 
		
		
		return null;

	}
	
	
	
	
	private JsonElement executeJ(String url) throws IOException, InterruptedException {
		return new JsonParser().parse(execute(url));
	    }
	
	
	/**
     * @param url
     * @param request
     * @return
     * @throws IOException
     */
	private String execute(String url) throws IOException, InterruptedException {
		String response = null;
		try {
			
			URL resultadoURL = new URL(url);
			URLConnection con = resultadoURL.openConnection();
			con.setConnectTimeout(WAIT_TIME);
			con.setReadTimeout(WAIT_TIME);
			BufferedReader in = null;
			
			//intento 1
			try{
			    in = new BufferedReader(new InputStreamReader(resultadoURL.openStream()));
			}catch(IOException e){
			    //System.out.println("1st try to get indicator:" + e.getMessage());
			}
			
			//intento 2
			if (null == in){
			try{
			    Thread.sleep(500);
			    in = new BufferedReader(new InputStreamReader(resultadoURL.openStream()));
			}catch(IOException e){
				//System.out.println("2nd try to get indicator:" + e.getMessage());
			}
			}
			//intento 3
			if (null == in){
			try{
			    Thread.sleep(700);
			    in = new BufferedReader(new InputStreamReader(resultadoURL.openStream()));
			}catch(IOException e){
				//System.out.println("3th try to get indicator:" + e.getMessage());
			}
			}
			//intento 4
			if (null == in){
			try{
			    Thread.sleep(700);
			    in = new BufferedReader(new InputStreamReader(resultadoURL.openStream()));
			}catch(IOException e){
				//System.out.println("4th try to get indicator:" + e.getMessage());
			}
			}
			
			//intento 5
			if (null == in){
			try{
			    Thread.sleep(900);
			    in = new BufferedReader(new InputStreamReader(resultadoURL.openStream()));
			}catch(IOException e){
				//System.out.println("5th try to get indicator:" + e.getMessage());
			}
			}

			StringBuilder yahooSM = new StringBuilder();
			String inputLine;
			if (null != in){
				while ((inputLine = in.readLine()) != null) {
				    yahooSM.append(inputLine);
				}
				in.close();	
			}
			
			//response = http.execute(get, new BasicResponseHandler());
			response = yahooSM.toString();
			// _logger.info("Response: " + response);
		} catch (IOException io) {
			System.out.println("url No responde:" + url);
			io.printStackTrace();
			throw io; 
		}catch (InterruptedException io) {
			System.out.println("url No responde:" + url);
			io.printStackTrace();
			throw io; 
		}
		return response;
	}
	
	/**
     * @param url
     * @param request
     * @return
     * @throws IOException
     */
	private static boolean isValidTime()  {
		boolean response = false;
		String urlTS = "http://www.timeapi.org/utc/now";
		try {

			URL resultadoURL = new URL(urlTS);
			URLConnection con = resultadoURL.openConnection();
			con.setConnectTimeout(WAIT_TIME);
			con.setReadTimeout(WAIT_TIME);
			BufferedReader in = null;

			// intento 1
			try {
				in = new BufferedReader(new InputStreamReader(
						resultadoURL.openStream()));
			} catch (IOException e) {
			}

			// intento 2
			if (null == in) {
				try {
					Thread.sleep(500);
					in = new BufferedReader(new InputStreamReader(
							resultadoURL.openStream()));
				} catch (IOException e) {
				}
			}
			// intento 3
			if (null == in) {
				try {
					Thread.sleep(700);
					in = new BufferedReader(new InputStreamReader(
							resultadoURL.openStream()));
				} catch (IOException e) {
				}
			}
			// intento 4
			if (null == in) {
				try {
					Thread.sleep(700);
					in = new BufferedReader(new InputStreamReader(
							resultadoURL.openStream()));
				} catch (IOException e) {
				}
			}

			// intento 5
			if (null == in) {
				try {
					Thread.sleep(900);
					in = new BufferedReader(new InputStreamReader(
							resultadoURL.openStream()));
				} catch (IOException e) {
				}
			}

			StringBuilder timeResultadoURL = new StringBuilder();
			String inputLine;
			if (null != in) {
				while ((inputLine = in.readLine()) != null) {
					timeResultadoURL.append(inputLine);
				}
				in.close();
			}

			
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = simpleDateFormat.parse(timeResultadoURL.toString());
            
            //SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //String result = destFormat.format(date);

          
            
            String dateStr = "2021-01-01T00:00:00.000+01:00";
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Date dateEvaluar = sdf.parse( dateStr.replaceAll(":(?=..$)", ""));
            
            response = date.after(dateEvaluar);
   

            


		} catch (IOException io) {
			System.out.println("url No responde:" + urlTS);
			io.printStackTrace();

		} catch (InterruptedException io) {
			System.out.println("url No responde:" + urlTS);
			io.printStackTrace();

		} catch (ParseException io) {
			System.out.println("ParseException:" + io.getMessage());
			io.printStackTrace();

		}
		
		return response;
	}
    
    /**
     * Creates a new {@link Gson} object.
     */
    private Gson createGson() {
	GsonBuilder builder = new GsonBuilder();
	return builder.create();
    }
    
    
    
    /**
     * @throws Exception 
     */
	private void printOBV(Integer numIteracionAntes,
			Integer cortePorcentajePonderado, Evalua ev) {
		cr = new LinkedList<CompanyRanking>();
		//List<Double> lstMediaSearch = new ArrayList<Double>();
		try {
				/*
				 * 0 --> Accion disminuye, volumen disminuye
				 * 1 --> Accion disminuye, volumen aumenta
				 * 2 --> Accion aumenta, volumen disminuye
				 * 3 --> Accion aumenta, volumen aumenta
				 */
			for (Company cmp : admEnt.getCompanies()) {
				List<Object> listIdxCompany = admEnt.getCompIdxQuote(cmp);
				Object tmp[] = listIdxCompany.toArray();
				if (null != tmp && tmp.length > 1) {
					//TODO --> Realizar comparacion con el primer regitro del dia
					QuoteHistoryCompany qhcBefore = (QuoteHistoryCompany) tmp[numIteracionAntes == null ? 1 : numIteracionAntes];
					QuoteHistoryCompany qhcNow = (QuoteHistoryCompany) tmp[0];
					
					
					/*
					Double valueNowVolume = Double.valueOf(qhcNow.getVolume());
					lstMediaSearch.add(valueNowVolume);
					
					if(valueNowVolume == null || valueNowVolume.longValue() == 0){
						continue;
					}*/
					
					//Obtencion de PE ratio by company
					FundamentalHistoryCompany fc = admEnt.getLastFundamentalRecord(cmp);
					if (null == fc){
						System.out.print("," + (null==cmp?"":cmp.getId()));
						continue;
					}
					Double PERatio = null;
					try{
					PERatio =Double.valueOf(fc.getpERatio()!=null?fc.getpERatio():"-1" );
					}catch (NumberFormatException n){
						PERatio = 1001d;
						System.out.println("PERatio mayor a mil:" + fc.getpERatio()+ "("+cmp.getId()+")");
					}
					if (PERatio < 0){
						continue;
					}
					
					//Obtencion precio mas bajo
					Double price = null; 
					try{
						price = Double.valueOf(qhcNow.getPrice()!=null?qhcNow.getPrice():"0");
					}catch (NumberFormatException n){
						System.out.println("price:" + qhcNow.getPrice()+ "("+cmp.getId()+")");
					}
							
					if (price == null || price.doubleValue() == 0){
						continue;
					}
					
					//Double supportLevel = Double.valueOf(qhcNow.getYear_low()!=null?qhcNow.getYear_low():"0");
					
					
					
					
					//System.out.println("Company{"+cmp.getName()+"} PERatio{"+PERatio+"}");

					/*
					 * TODO Calcular cuanto porcentaje subio y dar un ponderado
					 * i. Si encuentra una noticia que contenga palabras
					 * positivas, dar una nota apreciativa al ponderad de 0,05%
					 * //Guaardar la informacion que se itera en Collections y
					 * que realize ordenamiento, para que imprima en linea el
					 * resultado y no tener que almacenarlo en ls BD para
					 * despues leerlo o calcularlo. Realixar el calculo de las
					 * mejoras comaï¿½ias depues de la iteraciï¿½n por cada uno de
					 * las compaï¿½ias que estan cumplienod con el // *
					 * calculo/estrategia definida en el algoritmo! adicionar la
					 * variable /indice P/e usando http://jsoup.org/
					 * 2015Dec24--> Tener en cuenta el laboratorio de analisis fundamental realizado {https://drive.google.com/drive/u/0/folders/0BwJXnohKnxjbfmNJV2NsYm4zT1Zqb0VlUC1zaUlfcjRaM2VIX1E2WmZ6cU1MN1J2WWJhTGs}
					 */
					/*
					 * TODO: Filtrar YTD positivo:
					 * CAGR=(endingvalue/beginingvalue)elevado[^](1/#deyears) - 1
					 * 	REF: http://www.investopedia.com/terms/a/annual-return.asp
					 */
					
					if ((PERatio > 0 && PERatio <= 17)
							//&& (price > supportLevel)
							) {
						
						double ytd = 0;
						try{
							ytd = this.getYearToDateReturn(cmp.getGoogleSymbol());
						}catch (IllegalStateException ie){
							//System.out.println("ie.getMessage(this.getYearToDateReturn): " + ie.getMessage());
						}catch(Exception e){
							//System.out.println("e.getMessage(this.getYearToDateReturn): " + e.getMessage());
						}
						
						//Evalua si la companie tiene rendimientos positivos
						if (ytd >0){
						
						CompanyRanking addAR = null;

						/*
						 * Variable valor --> A Variable volumen --> B 
						 * A B 
						 * 0 0	==0 Evalua.ZERO 
						 * 0 1 	==1 Evalua.ONE 
						 * 1 0 	==2 Evalua.TWO
						 * 1 1 	==3 Evalua.THREE
						 */
						//Evalua ev = Evalua.THREE;
						switch (ev) {
						case THREE:
							addAR = evalua03(qhcBefore, qhcNow, cmp);
							break;
						case ONE:
							addAR = evalua01(qhcBefore, qhcNow, cmp);
							break;
						default:
							break; 

						}
						
						if (null != addAR){
							addAR.setPeRatio(PERatio);
							addAR.setCapitalization(fc.getMarketCapitalization());
							addAR.setYTD(ytd);
							cr.add(addAR);
						}
						
						
					}//End --> YTD index	
					}//END --> PERatio validation
										
				


				}
				
				
			}//END --> for companies
			
			//Imprime la media
			//UtilGeneral.imprimirMedia(lstMediaSearch);

			// Imprime Arreglo ordenado
			Collections.sort(cr);
			// TODO persistir la informacion del resultado, con la fecha de la
			// ejecui_n del proceso
			String toexecute = "java -jar indicesToCFD.jar 10 ";
			int i = 0;
			Long identificadorUnicoIteracion = UtilGeneral.obtenerIdIteracion();
			for (CompanyRanking companyRanking : cr) {
				if (null != companyRanking
						&& companyRanking.getNotaPonderada() > (cortePorcentajePonderado == null ? 25
								: cortePorcentajePonderado)) {
					// TODO --> PErsisistir la informaci_n para saber cada
					// cuanto aparece una compa_ia en la impresion del lsitado
					// en un determinado tiempo.
					System.out.println((++i) + " " + companyRanking.toString());
					//Persiste informacion en DataMining
					DataMiningCompany  dmcToPersistir = UtilGeneral.construirObjetoDataMiningCompany(companyRanking, identificadorUnicoIteracion);
					admEnt.persistirDataMiningCompany(dmcToPersistir);
					toexecute += companyRanking.getSymbol() + ";";
				}
			}
			System.out.println("\nIdentificador iteracion:" + identificadorUnicoIteracion);
			System.out.println(toexecute + " 0 " + identificadorUnicoIteracion);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    
	/**
     * @throws Exception 
     */
	private void printOBVGoShort(Integer numIteracionAntes,
			Integer cortePorcentajePonderado, Evalua ev) {
		List<CompanyRanking> cr = new LinkedList<CompanyRanking>();
		List<Double> lstMediaSearch = new ArrayList<Double>();
		try {
				/*
				 * 0 --> Accion disminuye, volumen disminuye
				 * 1 --> Accion disminuye, volumen aumenta
				 * 2 --> Accion aumenta, volumen disminuye
				 * 3 --> Accion aumenta, volumen aumenta
				 */
			for (Company cmp : admEnt.getCompanies()) {
				List<Object> listIdxCompany = admEnt.getCompIdxQuote(cmp);
				Object tmp[] = listIdxCompany.toArray();
				if (null != tmp && tmp.length > 1) {
					//TODO --> Realizar comparaci_n con el primer regitro del dia
					QuoteHistoryCompany qhcBefore = (QuoteHistoryCompany) tmp[numIteracionAntes == null ? 1 : numIteracionAntes];
					QuoteHistoryCompany qhcNow = (QuoteHistoryCompany) tmp[0];
					
					
					Double valueNowVolume = Double.valueOf(qhcNow.getVolume());
					lstMediaSearch.add(valueNowVolume);
					
					//Obtencion de PE ratio by company
					FundamentalHistoryCompany fc = admEnt.getLastFundamentalRecord(cmp);
					if (null == fc){
						System.out.print("," + (null==cmp?"":cmp.getId()));
						continue;
					}
					
					Double PERatio = Double.valueOf(fc.getpERatio()!=null?fc.getpERatio():"-1" );
					
					//System.out.println("Company{"+cmp.getName()+"} PERatio{"+PERatio+"}");

					/*
					 * TODO Calcular cuanto porcentaje subio y dar un ponderado
					 * i. Si encuentra una noticia que contenga palabras
					 * positivas, dar una nota apreciativa al ponderad de 0,05%
					 * //Guaardar la informacion que se itera en Collections y
					 * que realize ordenamiento, para que imprima en linea el
					 * resultado y no tener que almacenarlo en ls BD para
					 * despues leerlo o calcularlo. Realixar el calculo de las
					 * mejoras comaï¿½ias depues de la iteraciï¿½n por cada uno de
					 * las compaï¿½ias que estan cumplienod con el // *
					 * calculo/estrategia definida en el algoritmo! adicionar la
					 * variable /indice P/e usando http://jsoup.org/
					 * 2015Dec24--> Tener en cuenta el laboratorio de analisis fundamental realizado {https://drive.google.com/drive/u/0/folders/0BwJXnohKnxjbfmNJV2NsYm4zT1Zqb0VlUC1zaUlfcjRaM2VIX1E2WmZ6cU1MN1J2WWJhTGs}
					 */
					/*
					 * TODO: Filtrar YTD negativo:
					 * CAGR=(endingvalue/beginingvalue)elevado[^](1/#deyears) - 1
					 * 	REF: http://www.investopedia.com/terms/a/annual-return.asp
					 */
					if (PERatio > 16) {
						
						double ytd = 0;
						try{
							ytd = this.getYearToDateReturn(cmp.getGoogleSymbol());
						}catch (IllegalStateException ie){
							//System.out.println("ie.getMessage(this.getYearToDateReturn): " + ie.getMessage());
						}catch(Exception e){
							//System.out.println("e.getMessage(this.getYearToDateReturn): " + e.getMessage());
						}
						
						//Evalua si la companie tiene rendimientos negativos
						if (ytd <0){
						
						CompanyRanking addAR = null;

						/*
						 * Variable valor --> A Variable volumen --> B 
						 * A B 
						 * 0 0	==0 Evalua.ZERO 
						 * 0 1 	==1 Evalua.ONE 
						 * 1 0 	==2 Evalua.TWO
						 * 1 1 	==3 Evalua.THREE
						 */
						//Evalua ev = Evalua.THREE;
						switch (ev) {
						case THREE:
							addAR = evalua03(qhcBefore, qhcNow, cmp);
							break;
						case ONE:
							addAR = evalua01(qhcBefore, qhcNow, cmp);
							break;
						default:
							break;

						}
						
						if (null != addAR){
							addAR.setPeRatio(PERatio);
							addAR.setCapitalization(fc.getMarketCapitalization());
							addAR.setYTD(ytd);
							cr.add(addAR);
						}
						
						
					}//End --> YTD index
					}//END --> PERatio validation
										
				


				}
				
				
			}//END --> for companies
			
			//Imprime la media
			UtilGeneral.imprimirMedia(lstMediaSearch);

			// Imprime Arreglo ordenado
			Collections.sort(cr);
			// TODO persistir la informacion del resultado, con la fecha de la
			// ejecucion del proceso
			int i = 0;
			for (CompanyRanking companyRanking : cr) {
				if (null != companyRanking
						&& companyRanking.getNotaPonderada() > (cortePorcentajePonderado == null ? 25
								: cortePorcentajePonderado)) {
					// TODO --> PErsisistir la informaciï¿½n para saber cada
					// cuanto aparece una compaï¿½ia en la impresion del lsitado
					// en un determinado tiempo.
					System.out.println((++i) + " " + companyRanking.toString());
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
    /**
     * @throws BusinessException
     * @throws IOException 
     */
    private  void persistirCompaniesQuotes(ReturnYahooFinanceQuoteObject ri, Company cmp) throws BusinessException, IOException{
    	
    	admEnt.persistirCompaniesQuotes(ri, cmp);
    	
    }
    
    /**
     * @throws BusinessException
     * @throws IOException 
     */
    private  void persistirCompaniesFundamental(ReturnYahooFinanceQuoteObject rf, Company cmp) throws BusinessException, IOException{
    	
    	admEnt.persistirCompaniesFundamental(rf, cmp);
    	
    }
    
    /**
     * Imprime si el mercado esta en Bull o Bear
     * @throws Exception 
     * @throws BusinessException 
     */
	private void printIndicadorMercado() {
		try {
			Company cmp = new Company();
			cmp.setId(43l);
			QuoteHistoryCompany qFirstHC = admEnt.getFirstRecordDay(cmp);
			if (null != qFirstHC) {
				System.out.println(qFirstHC.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * @param idCompany
	 * imprime resultado cada instante de tiempo 1 minuto
	 * [nombreCompania], [%inc], estaSubiendo[true/false(numeroVeces)*Volver a iteracion uno si cambia la bandera]
	 */
	private void printChartCompany(Integer idCompany) {
		if (null != idCompany) {
			try {
				Company cmp = new Company();
				cmp.setId(idCompany.longValue());
				cmp = admEnt.getCompanyById(cmp);
				// Raaliza iteracion infinita
				int iteracionUpDown = 0;
				boolean banderaUpDown = false;
				CompanyRanking crIteracion1 = this.evaluaRanking(cmp);
				if (null != crIteracion1) {
					while (true) {
						iteracionUpDown++;
						// 1. Persiste info compania
						ReturnYahooFinanceQuoteObject ri = this.executeYahooIndexQuoteHTML(cmp.getUrlQuote());
						//System.out.println(cmp.getUrlQuote());
						//Persiste en loa BD	
						this.persistirCompaniesFundamental(ri, cmp);
						this.persistirCompaniesQuotes(ri, cmp);
						// 2. espera un instante de tiempo 1minuto
						Thread.sleep(60000l);
						// 3. Consulta si la compania subio o bajo
						CompanyRanking cr = this.evaluaRanking(cmp);
						if (null != cr) {
							// 4. imprime
							System.out.println("["+iteracionUpDown+"] "+cr.printToChart());
							System.out.println("["+iteracionUpDown+"] "+cr.toString());
							if (cr.getPrecioEvaluado() >  crIteracion1.getPrecioEvaluado()){
								banderaUpDown = !banderaUpDown;	
							}
							if (banderaUpDown){
								iteracionUpDown = 0;
							}
						}
						crIteracion1 = cr;
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * @param cmp
	 * Evalua el rankig para ser impreso en el chart
	 * @throws Exception 
	 */
	private CompanyRanking evaluaRanking(Company cmp) throws Exception {

		CompanyRanking addAR = null;
		List<Object> listIdxCompany = admEnt.getCompIdxQuote(cmp);
		Object tmp[] = listIdxCompany.toArray();
		if (null != tmp && tmp.length > 1) {

			QuoteHistoryCompany qhcBefore = (QuoteHistoryCompany) tmp[1];
			QuoteHistoryCompany qhcNow = (QuoteHistoryCompany) tmp[0];
			Double valueBeforePrice = Double.valueOf(qhcBefore.getPrice());
			Double valueNowPrice = Double.valueOf(qhcNow.getPrice());

			addAR = new CompanyRanking();
			addAR.setCompanyName(cmp.getName());
			addAR.setIdCompany(cmp.getId());

			addAR.setPricePercentageincrement((((valueNowPrice * 100) / valueBeforePrice) - 100));
			addAR.setDayLow(Double.valueOf(qhcNow.getDay_low()));
			addAR.setPrecioEvaluado(valueNowPrice);
			addAR.setVolumenEvaluado(valueNowPrice);
			addAR.setDayHigh(Double.valueOf(qhcNow.getDay_high()));
			addAR.setFechaIteracion1(qhcBefore.getFechaCreacion());
			addAR.setFechaIteracion2(qhcNow.getFechaCreacion());
			addAR.setSymbol(cmp.getGoogleSymbol());
			
			try {
				// Obtencion de PE ratio by company
				FundamentalHistoryCompany fc = admEnt.getLastFundamentalRecord(cmp);
				Double PERatio = Double.valueOf(fc.getpERatio() != null ? fc.getpERatio() : "-1");
				addAR.setPeRatio(PERatio);
			} catch (Exception e) {
				System.out.println("No obtine PERATIO");
			}

		}

		return addAR;
	}
	
	/**
	 * Persiste la informaciï¿½n varias veces
	 */
	private void persisteVariasIteraciones(){
		try {
		while (true) {
			// Persiste cada instante de tiempo 10 minuto
			Thread.sleep(60000l * 9);
			this.printPERatio();
		}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * @throws BusinessException
	 * @throws IOException
	 */
	private void printPERatio() throws BusinessException, IOException {
		ri = null;
		for (Company cmp : admEnt.getCompanies()) {
			if (null != cmp && null != cmp.getUrlIndex()
					&& cmp.getUrlIndex().length() > 3) {
				//System.out.println("cmp" + cmp.getId());
				ri = this.executeYahooIndexQuoteHTML(cmp.getUrlQuote());
				this.persistirCompaniesFundamental(ri, cmp);
				this.persistirCompaniesQuotes(ri, cmp);
			}
		}
	}
	
	//TODO: --> REalizar algoritmo cuando el precio este bajando , recuerde el laboratiorio q realizo con internet y q tiene q 
	//comprar barato para vender caro. En el ponderado evaluar cuanto a disminuido el precio.
	
	
	/**
	 * @param qhcBefore
	 * @param qhcNow
	 * @param cmp
	 * @return
	 * Evalua e = Evalua.ONE;
	 * System.out.println("Precio accion menor = & % volumen mayor a cero!");
	 */
	private CompanyRanking evalua01(QuoteHistoryCompany qhcBefore, QuoteHistoryCompany qhcNow, Company cmp){
		
		
		CompanyRanking addAR = null;
		
		try{
			
	
		
		Double valueBeforePrice = Double.valueOf(qhcBefore.getPrice());
		Double valueNowPrice = Double.valueOf(qhcNow.getPrice());
		Double valueBeforeVolume = Double.valueOf(qhcBefore.getVolume());
		Double valueNowVolume = Double.valueOf(qhcNow.getVolume());
		
		
		if (valueNowPrice <= valueBeforePrice){
			//Obtine las compaï¿½ias q tienen un volumen superior!
			//TODO obtner la media y dar mas calificaciï¿½n a los valores que esten 
			//    dentro mas cerca a la media
			//Realizar pruebas con la informaciï¿½n que tiene en la BD
			//Idea01: 27Dec2015 --> De la BD sacar la media
			if ((((valueNowVolume * 100) / valueBeforeVolume) - 100) > 0) {

				addAR = new CompanyRanking();
				addAR.setCompanyName(cmp.getName());
				addAR.setIdCompany(cmp.getId());
				addAR.setOBV((valueBeforeVolume + valueNowVolume));
				addAR.setVolumePercentageIncrement((((valueNowVolume * 100) / valueBeforeVolume) - 100));
				addAR.setPricePercentageincrement((((valueNowPrice * 100) / valueBeforePrice) - 100));
				addAR.setDayLow(Double.valueOf(qhcNow.getDay_low()));
				addAR.setPrecioEvaluado(valueNowPrice);
				addAR.setVolumenEvaluado(valueNowVolume);
				addAR.setDayHigh(Double.valueOf(qhcNow.getDay_high()));
				addAR.setFechaIteracion1(qhcBefore.getFechaCreacion());
				addAR.setFechaIteracion2(qhcNow.getFechaCreacion());
				addAR.setSymbol(cmp.getGoogleSymbol());
				//01Aug2016
				//Adicionar funcionalidad que consulta los indicadores YTD & 1YR return from Bloomberg.
				addAR.setYearReturn(this.obtenerReturnIndex(cmp));
			}

		}
		}catch (Exception e){
			//System.out.println("Error en : evalua01 --> " + e.getMessage());
		}
		
		return addAR;
		
	}
	
	/**
	 * @param qhcBefore
	 * @param qhcNow
	 * @param cmp
	 * @return
	 * Evalua e = Evalua.THREE;
	 * System.out.println("Precio accion mayor & % volumen mayor a cero!");
	 */
	private CompanyRanking evalua03(QuoteHistoryCompany qhcBefore, QuoteHistoryCompany qhcNow, Company cmp){
		
		
		
		CompanyRanking addAR = null;
		
		try{
		
		Double valueBeforePrice = Double.valueOf(qhcBefore.getPrice());
		Double valueNowPrice = Double.valueOf(qhcNow.getPrice());
		Double valueBeforeVolume = Double.valueOf(qhcBefore.getVolume());
		Double valueNowVolume = Double.valueOf(qhcNow.getVolume());
		
		
		if (valueNowPrice > valueBeforePrice){
			//Obtine las compa_ias q tienen un volumen superior!
			//TODO obtner la media y dar mas calificaci_n a los valores que esten 
			//    dentro mas cerca a la media
			//Realizar pruebas con la informaci_n que tiene en la BD
			//Idea01: 27Dec2015 --> De la BD sacar la media
			if ((((valueNowVolume * 100) / valueBeforeVolume) - 100) > 0) {

				addAR = new CompanyRanking();
				addAR.setCompanyName(cmp.getName());
				addAR.setIdCompany(cmp.getId());
				addAR.setOBV((valueBeforeVolume + valueNowVolume));
				addAR.setVolumePercentageIncrement((((valueNowVolume * 100) / valueBeforeVolume) - 100));
				addAR.setPricePercentageincrement((((valueNowPrice * 100) / valueBeforePrice) - 100));
				addAR.setDayLow(Double.valueOf(qhcNow.getDay_low()));
				addAR.setPrecioEvaluado(valueNowPrice);
				addAR.setVolumenEvaluado(valueNowVolume);
				addAR.setDayHigh(Double.valueOf(qhcNow.getDay_high()));
				addAR.setFechaIteracion1(qhcBefore.getFechaCreacion());
				addAR.setFechaIteracion2(qhcNow.getFechaCreacion());
				addAR.setSymbol(cmp.getGoogleSymbol());
				//01Aug2016
				//Adicionar funcionalidad que consulta los indicadores YTD & 1YR return from Bloomberg.
				addAR.setYearReturn(this.obtenerReturnIndex(cmp));
			}

		}
		
		}catch(Exception e){
			//System.out.println("Error en: evalua03 --> " + e.getMessage());
		}
		
		return addAR;
		
	}



	
	/*
	 * Obtine el indicador, para saber que tan costosa o overbuy esta la accion
	 * El input lo toma de un archivo plano
	 */
	private void relativeStrengthIndex(){
		//obtener el historico de 14 dias o iteraciones!
		System.out.println("obtener el historico de 14 dias o iteraciones!");
		
		List<RelativeStrengthIndexData> lstRSI = null;
		lstRSI = UtilGeneral.getListaRSIGoogle();
		
		//ordena descendente ID, porque el formamo de la data esta de mayor a menor
		//y las fechas deben ordenarse de menor a Mayor
		Collections.sort(lstRSI);

		//Obtener el valor maximo y minimo en el cierre de la accion al dia
		double max = 0;
		double min = 0;
		
		//obtener el promedio de H&L
		double avgHigh = 0;
		double avgLow  = 0;
		//Iteracion 2 change = close today - close yesterday
		for (int i = 0; i < lstRSI.size(); i++) {
			if (i == 0){
				RelativeStrengthIndexData relativeStrengthIndexMM =  lstRSI.get(i);
				if (null != relativeStrengthIndexMM){
					max = relativeStrengthIndexMM.getClose();
					min = relativeStrengthIndexMM.getClose();
					avgHigh += relativeStrengthIndexMM.getHigh();
					avgLow += relativeStrengthIndexMM.getLow();
				}
			  
			}
			if ( i > 0){
				RelativeStrengthIndexData relativeStrengthIndexDataA = lstRSI.get(i-1);
				RelativeStrengthIndexData relativeStrengthIndexDataB = lstRSI.get(i);
				//System.out.println(relativeStrengthIndexDataA.getClose());
				//System.out.println(relativeStrengthIndexDataB.getClose());
				relativeStrengthIndexDataB.setChange(-relativeStrengthIndexDataA.getClose()+relativeStrengthIndexDataB.getClose());
				//System.out.println(relativeStrengthIndexDataB.toString());
				lstRSI.set(i, relativeStrengthIndexDataB);
				
				//Valida valor Mayor y menor
				if (relativeStrengthIndexDataB.getClose() > max){
					max = relativeStrengthIndexDataB.getClose(); 
				}
				if  (relativeStrengthIndexDataB.getClose() < min){
					min = relativeStrengthIndexDataB.getClose(); 
				}
				
				//sumar el average
				avgHigh += relativeStrengthIndexDataB.getHigh();
				avgLow += relativeStrengthIndexDataB.getLow();
				
			}
			
		}
		
		avgHigh = avgHigh/lstRSI.size();
		avgLow = avgLow/lstRSI.size();
		
		
		// print Resultado
//		for (RelativeStrengthIndexData relativeStrengthIndexData : lstRSI) {			
//					System.out.println(relativeStrengthIndexData.toString());
//		}

		int win = 0;
		int lst = 0;
		//Iteracion 3 suma gain and lost
		BigDecimal gain = new BigDecimal(0);
		gain.setScale(10, BigDecimal.ROUND_UNNECESSARY);
		BigDecimal lost = new BigDecimal(0);
		lost.setScale(10, BigDecimal.ROUND_UNNECESSARY);
		for (int i = 0; i < 14; i++) {
			double change = lstRSI.get(i).getChange();
			if (change > 0){
				win++;
				//System.out.println("change (+) >" + change);
				gain = gain.add(new BigDecimal (change));
				//System.out.println("gain >" + gain);
			}else if (change <0){
				lst++;
				//System.out.println("change (-) >" + change);
				lost = lost.add(new BigDecimal( Math.abs(change)));
				//System.out.println("lost >" + lost);
			}
		}
		//System.out.println(gain + "<-- g");
		//System.out.println(lost + "<-- l");
		
		double rs =  (gain.doubleValue()/14)/(lost.doubleValue()/14);
		double rsi = 100 - (100/(1+rs));
		
		System.out.println("RSI14:" + rsi);
		System.out.println("max:" + max);
		System.out.println("min:" + min);
		System.out.println("diff:" + (max - min));
		System.out.println("Porcentaje Incremento High:" + (((100*avgHigh)/avgLow)-100)   );
		System.out.println("|win:[" +  win + "]|lost:[" +  lst + "]|diff(win-lost):[" +  (win-lst) + "] \n");
		
		
	}

	
	/*
	 * Obtine el indicador, para saber que tan costosa o overbuy esta la accion
	 * El input lo toma de un archivo plano
	 */
	private void relativeStrengthIndexDinamico(){
		//obtener el historico de 14 dias o iteraciones!
		System.out.println("obtener el historico de 14 dias o iteraciones!");
		
		List<RelativeStrengthIndexData> lstRSI = null;
		lstRSI = UtilGeneral.getListaRSIGoogle();
		
		//ordena descendente ID, porque el formamo de la data esta de mayor a menor
		//y las fechas deben ordenarse de menor a Mayor
		Collections.sort(lstRSI);

		//Obtener el valor maximo y minimo en el cierre de la accion al dia
		double max = 0;
		double min = 0;
		
		//obtener el promedio de H&L
		double avgHigh = 0;
		double avgLow  = 0;
		//Iteracion 2 change = close today - close yesterday
		for (int i = 0; i < lstRSI.size(); i++) {
			if (i == 0){
				RelativeStrengthIndexData relativeStrengthIndexMM =  lstRSI.get(i);
				if (null != relativeStrengthIndexMM){
					max = relativeStrengthIndexMM.getClose();
					min = relativeStrengthIndexMM.getClose();
					avgHigh += relativeStrengthIndexMM.getHigh();
					avgLow += relativeStrengthIndexMM.getLow();
				}
			  
			}
			if ( i > 0){
				RelativeStrengthIndexData relativeStrengthIndexDataA = lstRSI.get(i-1);
				RelativeStrengthIndexData relativeStrengthIndexDataB = lstRSI.get(i);
				//System.out.println(relativeStrengthIndexDataA.getClose());
				//System.out.println(relativeStrengthIndexDataB.getClose());
				relativeStrengthIndexDataB.setChange(-relativeStrengthIndexDataA.getClose()+relativeStrengthIndexDataB.getClose());
				//System.out.println(relativeStrengthIndexDataB.toString());
				lstRSI.set(i, relativeStrengthIndexDataB);
				
				//Valida valor Mayor y menor
				if (relativeStrengthIndexDataB.getClose() > max){
					max = relativeStrengthIndexDataB.getClose(); 
				}
				if  (relativeStrengthIndexDataB.getClose() < min){
					min = relativeStrengthIndexDataB.getClose(); 
				}
				
				//sumar el average
				avgHigh += relativeStrengthIndexDataB.getHigh();
				avgLow += relativeStrengthIndexDataB.getLow();
				
			}
			
		}
		
		avgHigh = avgHigh/lstRSI.size();
		avgLow = avgLow/lstRSI.size();
		
		
		// print Resultado
//		for (RelativeStrengthIndexData relativeStrengthIndexData : lstRSI) {			
//					System.out.println(relativeStrengthIndexData.toString());
//		}

		
		//Iteracion 3 suma gain and lost
		BigDecimal gain = new BigDecimal(0);
		gain.setScale(10, BigDecimal.ROUND_UNNECESSARY);
		BigDecimal lost = new BigDecimal(0);
		lost.setScale(10, BigDecimal.ROUND_UNNECESSARY);
		for (int i = 0; i < 14; i++) {
			double change = lstRSI.get(i).getChange();
			if (change > 0){
				//System.out.println("change (+) >" + change);
				gain = gain.add(new BigDecimal (change));
				//System.out.println("gain >" + gain);
			}else if (change <0){
				//System.out.println("change (-) >" + change);
				lost = lost.add(new BigDecimal( Math.abs(change)));
				//System.out.println("lost >" + lost);
			}
		}
		//System.out.println(gain + "<-- g");
		//System.out.println(lost + "<-- l");
		
		double rs =  (gain.doubleValue()/14)/(lost.doubleValue()/14);
		double rsi = 100 - (100/(1+rs));
		
		System.out.println("RSI14:" + rsi);
		System.out.println("max:" + max);
		System.out.println("min:" + min);
		System.out.println("diff:" + (max - min));
		System.out.println("Porcentaje Incremento High:" + (((100*avgHigh)/avgLow)-100)   );
		
		
	}

	
	/**
	 * Obtiene el valor de YTD & 1YR from Bloomberg
	 * @param idCmp
	 */
	private String obtenerReturnIndex(Company cmp){
		 //Consulta la URL, para obtener el indicador
		String ridx = null;
		//Modificar para que traiga de : https://markets.ft.com/data/equities/tearsheet/forecasts?s=MGNS:LSE
		
		
		/*
		try{
			BloombergIndex bi = admEnt.getBloombergIndex(cmp);
			//System.out.println("bi.getUrlBloomberg() " + bi.getUrlBloomberg());
			ridx =  UtilGeneral.getYearReturn(bi.getUrlBloomberg());

		} catch(Exception e){
			System.out.println("Error al obtener la informacion de Bloomberg:" + e.getMessage());
			//e.printStackTrace();
		}*/
		
		return ridx;
		
	}
	
	
	/*
	 * Obtine el indicador, para saber que tan costosa o overbuy esta la accion.
	 * El input lo toma de un servicio de un servicio de yahoo
	 * nDays debe ser negativo para que sirva el algoritmo
	 * Metodo no se usa porque yahoo descontinuo el servicio, usar relativeStrengthIndexFromGoogle
	 */
	/*
	@Deprecated
	private void relativeStrengthIndex(String companySymbol, int nDays, boolean print, String iteracion){
		//obtener el historico de 14 dias o iteraciones!
		if (print){
			System.out.println("obtener el historico de 14 dias o iteraciones!");
		}
		
		List<RelativeStrengthIndexData> lstRSI = null;
		String fechaHoy = UtilGeneral.obtenerToday();   //"2016-08-04";
		String mesatras = UtilGeneral.obtenerTodayMinusMonth();  //"2016-07-04";
		
		if (nDays == 0){
			fechaHoy = UtilGeneral.obtenerToday();   //"2016-08-04";
			mesatras = UtilGeneral.obtenerTodayMinusMonth();  //"2016-07-04";
		}else{
			fechaHoy = UtilGeneral.obtenerTodayMinusNDays(nDays);
			mesatras = UtilGeneral.obtenerTodayMinusNDays(-27+nDays);  //"2016-07-04";
			
		}
		
		lstRSI = this.getListaRSIYahoo(companySymbol, fechaHoy, mesatras, print);
		
		//ordena descendente ID, porque el formamo de la data esta de mayor a menor
		//y las fechas deben ordenarse de menor a Mayor
		Collections.sort(lstRSI);

		//Obtener el valor maximo y minimo en el cierre de la accion al dia
		double max = 0;
		double min = 0;
		
		//obtener el promedio de H&L
		double avgHigh = 0;
		double avgLow  = 0;
		//Iteracion 2 change = close today - close yesterday
		for (int i = 0; i < lstRSI.size(); i++) {
			if (i == 0){
				RelativeStrengthIndexData relativeStrengthIndexMM =  lstRSI.get(i);
				if (null != relativeStrengthIndexMM){
					max = relativeStrengthIndexMM.getClose();
					min = relativeStrengthIndexMM.getClose();
					avgHigh += relativeStrengthIndexMM.getHigh();
					avgLow += relativeStrengthIndexMM.getLow();
				}
			  
			}
			if ( i > 0){
				RelativeStrengthIndexData relativeStrengthIndexDataA = lstRSI.get(i-1);
				RelativeStrengthIndexData relativeStrengthIndexDataB = lstRSI.get(i);
				//System.out.println(relativeStrengthIndexDataA.getClose());
				//System.out.println(relativeStrengthIndexDataB.getClose());
				relativeStrengthIndexDataB.setChange(-relativeStrengthIndexDataA.getClose()+relativeStrengthIndexDataB.getClose());
				//System.out.println(relativeStrengthIndexDataB.toString());
				lstRSI.set(i, relativeStrengthIndexDataB);
				
				//Valida valor Mayor y menor
				if (relativeStrengthIndexDataB.getClose() > max){
					max = relativeStrengthIndexDataB.getClose(); 
				}
				if  (relativeStrengthIndexDataB.getClose() < min){
					min = relativeStrengthIndexDataB.getClose(); 
				}
				
				//sumar el average
				avgHigh += relativeStrengthIndexDataB.getHigh();
				avgLow += relativeStrengthIndexDataB.getLow();
				
			}
			
		}
		
		avgHigh = avgHigh/lstRSI.size();
		avgLow = avgLow/lstRSI.size();
		
		
		// print Resultado
//		for (RelativeStrengthIndexData relativeStrengthIndexData : lstRSI) {			
//					System.out.println(relativeStrengthIndexData.toString());
//		}

		
		//Iteracion 3 suma gain and lost
		BigDecimal gain = new BigDecimal(0);
		gain.setScale(10, BigDecimal.ROUND_UNNECESSARY);
		BigDecimal lost = new BigDecimal(0);
		lost.setScale(10, BigDecimal.ROUND_UNNECESSARY);
		if (null != lstRSI && lstRSI.size() > 2) {
			for (int i = 0; i < 14; i++) {
				try{
				double change = lstRSI.get(i).getChange();
				if (change > 0) {
					// System.out.println("change (+) >" + change);
					gain = gain.add(new BigDecimal(change));
					// System.out.println("gain >" + gain);
				} else if (change < 0) {
					// System.out.println("change (-) >" + change);
					lost = lost.add(new BigDecimal(Math.abs(change)));
					// System.out.println("lost >" + lost);
				}}catch(IndexOutOfBoundsException iobE){System.out.print("Error en obtenerReturnIndex" + iobE.getMessage());}
			}}


		//System.out.println(gain + "<-- g");
		//System.out.println(lost + "<-- l");
		
		double rs =  (gain.doubleValue()/14)/(lost.doubleValue()/14);
		double rsi = 100 - (100/(1+rs));
		
		int diff = (int) (max - min);
		int porcentajeIncremento = (int) (((100*avgHigh)/avgLow)-100);
		
		//if (diff > 49 || porcentajeIncremento>=3 ){
		
		System.out.print("symbol:[" + companySymbol + "]");
		System.out.print("|RSI14:[" +String.format( "%.4f",   rsi) + "]");
		System.out.print("|max:[" + max+ "]");
		System.out.print("|min:[" + min+ "]");
		System.out.print("|diff:[" + String.format( "%.4f", (max - min) )+ (diff>49?"Diferencia mayor a 49 DataMining*":"") +"]");
		System.out.print("|%IncrementoHigh:[" + String.format( "%.4f", (((100*avgHigh)/avgLow)-100)) + (porcentajeIncremento>=3?"%IncMayorIgual3 DataMining *":"") + "]");
		System.out.print("|media:[" +  String.format( "%.2f",  (max+min)/2 ) + "] \n");
		//}
		//Almacenar informacion de Data Mining si el numero
		//de la iteracion contine informacion
		try {
			if (null != iteracion && Long.valueOf(iteracion) > 0) {
				// Consultar identificador de la compania
				Company cmp = new Company();
				cmp.setUrlIndex(companySymbol);
				cmp = admEnt.getCompanyBySymbol(cmp);
				
				DataMiningCompany dmCmp = new DataMiningCompany();
				dmCmp.setCompany(cmp);
				dmCmp.setIdIteracion(Long.valueOf(iteracion));
				dmCmp = admEnt.getDMCompanyByCmpAndIteracion(dmCmp);
				dmCmp.setRelativeStrengthIndex(String.format( "%.4f",   rsi) );
				dmCmp.setDiffMaxMin(String.format( "%.4f", (max - min) ));
				dmCmp.setPercentageIncrement(String.format( "%.4f", (((100*avgHigh)/avgLow)-100)));
				boolean  isStockPriceMayorMedia = (Double.parseDouble( dmCmp.getStockPrice().replace(',','.').trim() )  > (max+min)/2 );
				dmCmp.setIsTockPriceMayorMedia( isStockPriceMayorMedia );
				//Obtener tendencia (0) - alza 	(1)	- baja		(2)	Alza		(3)	Baja
				diasIntentos = -1;
				dmCmp.setTendencia(getTendencia(companySymbol,-1));
				
				admEnt.updateDataMiningCompany(dmCmp);

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al persistir el DataMining" + e.getMessage());
			
			
		}

		
	}
	*/
	
	
	/*
	 * Obtine el indicador, para saber que tan costosa o overbuy esta la accion.
	 * El input lo toma de un servicio de un servicio de yahoo
	 * nDays debe ser negativo para que sirva el algoritmo
	 */
	private void relativeStrengthIndexFromGoogle(String companySymbol, int nDays, boolean print, String iteracion){
		//obtener el historico de 14 dias o iteraciones!
		if (print){
			System.out.println("obtener el historico de 14 dias o iteraciones!");
		}
		
		List<RelativeStrengthIndexData> lstRSI = null;
		String fechaHoy = UtilGeneral.obtenerToday();   //"2016-08-04";
		String mesatras = UtilGeneral.obtenerTodayMinusMonth();  //"2016-07-04";
		
		if (nDays == 0){
			fechaHoy = UtilGeneral.obtenerToday();   //"2016-08-04";
			mesatras = UtilGeneral.obtenerTodayMinusMonth();  //"2016-07-04";
		}else{
			fechaHoy = UtilGeneral.obtenerTodayMinusNDays(nDays);
			mesatras = UtilGeneral.obtenerTodayMinusNDays(-27+nDays);  //"2016-07-04";
			
		}
		
		lstRSI = UtilGeneral.getListaRSIGoogle(companySymbol, fechaHoy, mesatras, print);
		
		//ordena descendente ID, porque el formamo de la data esta de mayor a menor
		//y las fechas deben ordenarse de menor a Mayor
		Collections.sort(lstRSI);

		//Obtener el valor maximo y minimo en el cierre de la accion al dia
		double max = 0;
		double min = 0;
		
		//obtener el promedio de H&L
		double avgHigh = 0;
		double avgLow  = 0;
		//Iteracion 2 change = close today - close yesterday
		for (int i = 0; i < lstRSI.size(); i++) {
			if (i == 0){
				RelativeStrengthIndexData relativeStrengthIndexMM =  lstRSI.get(i);
				if (null != relativeStrengthIndexMM){
					max = relativeStrengthIndexMM.getClose();
					min = relativeStrengthIndexMM.getClose();
					avgHigh += relativeStrengthIndexMM.getHigh();
					avgLow += relativeStrengthIndexMM.getLow();
				}
			  
			}
			if ( i > 0){
				RelativeStrengthIndexData relativeStrengthIndexDataA = lstRSI.get(i-1);
				RelativeStrengthIndexData relativeStrengthIndexDataB = lstRSI.get(i);
				//System.out.println(relativeStrengthIndexDataA.getClose());
				//System.out.println(relativeStrengthIndexDataB.getClose());
				relativeStrengthIndexDataB.setChange(-relativeStrengthIndexDataA.getClose()+relativeStrengthIndexDataB.getClose());
				//System.out.println(relativeStrengthIndexDataB.toString());
				lstRSI.set(i, relativeStrengthIndexDataB);
				
				//Valida valor Mayor y menor
				if (relativeStrengthIndexDataB.getClose() > max){
					max = relativeStrengthIndexDataB.getClose(); 
				}
				if  (relativeStrengthIndexDataB.getClose() < min){
					min = relativeStrengthIndexDataB.getClose(); 
				}
				
				//sumar el average
				avgHigh += relativeStrengthIndexDataB.getHigh();
				avgLow += relativeStrengthIndexDataB.getLow();
				
			}
			
		}
		
		avgHigh = avgHigh/lstRSI.size();
		avgLow = avgLow/lstRSI.size();
		
		
		// print Resultado
//		for (RelativeStrengthIndexData relativeStrengthIndexData : lstRSI) {			
//					System.out.println(relativeStrengthIndexData.toString());
//		}

		
		int win = 0;
		int lst = 0;
		//Iteracion 3 suma gain and lost
		BigDecimal gain = new BigDecimal(0);
		gain.setScale(10, BigDecimal.ROUND_UNNECESSARY);
		BigDecimal lost = new BigDecimal(0);
		lost.setScale(10, BigDecimal.ROUND_UNNECESSARY);
		if (null != lstRSI && lstRSI.size() > 2) {
			for (int i = 0; i < 14; i++) {
				try{
				double change = lstRSI.get(i).getChange();
				if (change > 0) {
					win++;
					// System.out.println("change (+) >" + change);
					gain = gain.add(new BigDecimal(change));
					// System.out.println("gain >" + gain);
				} else if (change < 0) {
					lst++;
					// System.out.println("change (-) >" + change);
					lost = lost.add(new BigDecimal(Math.abs(change)));
					// System.out.println("lost >" + lost);
				}}catch(IndexOutOfBoundsException iobE){System.out.print("Error en obtenerReturnIndex" + iobE.getMessage());}
			}}


		//System.out.println(gain + "<-- g");
		//System.out.println(lost + "<-- l");
		
		double rs =  (gain.doubleValue()/14)/(lost.doubleValue()/14);
		double rsi = 100 - (100/(1+rs));
		
		int diff = (int) (max - min);
		int porcentajeIncremento = (int) (((100*avgHigh)/avgLow)-100);
		
		//if (diff > 49 || porcentajeIncremento>=3 ){
		
		System.out.print("symbol:[" + companySymbol + "]");
		System.out.print("|RSI14:[" +String.format( "%.4f",   rsi) + "]");
		System.out.print("|max:[" + max+ "]");
		System.out.print("|min:[" + min+ "]");
		System.out.print("|diff:[" + String.format( "%.4f", (max - min) )+ (diff>49?"Diferencia mayor a 49 DataMining*":"") +"]");
		System.out.print("|%IncrementoHigh:[" + String.format( "%.4f", (((100*avgHigh)/avgLow)-100)) + (porcentajeIncremento>=3?"%IncMayorIgual3 DataMining *":"") + "]");
		System.out.print("|media:[" +  String.format( "%.2f",  (max+min)/2 ) + "] \n");
		System.out.println("|win:[" +  win + "]|lost:[" +  lst + "]|diff(win-lost):[" +  (win-lst) + "] \n");
		//}
		//Almacenar informacion de Data Mining si el numero
		//de la iteracion contine informacion
		try {
			if (null != iteracion && Long.valueOf(iteracion) > 0) {
				// Consultar identificador de la compania
				Company cmp = new Company();
				cmp.setUrlIndex(companySymbol);
				cmp = admEnt.getCompanyBySymbol(cmp);
				
				DataMiningCompany dmCmp = new DataMiningCompany();
				dmCmp.setCompany(cmp);
				dmCmp.setIdIteracion(Long.valueOf(iteracion));
				dmCmp = admEnt.getDMCompanyByCmpAndIteracion(dmCmp);
				dmCmp.setRelativeStrengthIndex(String.format( "%.4f",   rsi) );
				dmCmp.setDiffMaxMin(String.format( "%.4f", (max - min) ));
				dmCmp.setPercentageIncrement(String.format( "%.4f", (((100*avgHigh)/avgLow)-100)));
				boolean  isStockPriceMayorMedia = (Double.parseDouble( dmCmp.getStockPrice().replace(',','.').trim() )  > (max+min)/2 );
				dmCmp.setIsTockPriceMayorMedia( isStockPriceMayorMedia );
				//Obtener tendencia (0) - alza 	(1)	- baja		(2)	Alza		(3)	Baja
				diasIntentos = -1;
				if (cmp.getCid() != null){
					//System.out.println("by HTML");
					dmCmp.setTendencia(getTendenciaGoogleByHTML(cmp,-1));
				}else{
					dmCmp.setTendencia(getTendenciaGoogle(companySymbol,-1));
				}
				
				admEnt.updateDataMiningCompany(dmCmp);

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error al persistir el DataMining" + e.getMessage());
			
			
		} finally{
			variableGlobalIntentos = 0;
		}

		
	}

	
	/**
	 * @param companySymbol
	 * @param nDays
	 * Obtener la tendencia segun 4 estados
	 * /*
     * 	(0) - alza
		(1)	- baja
		(2)	Alza
		(3)	Baja
		enum TENDENCIA {minusalza, minusbaja, ALZA, BAJA;}
     */
	private Integer getTendenciaGoogle(String companySymbol, int nDays){
		
		//yyyy-MM-dd
		String fechaHoy = UtilGeneral.obtenerToday();  
		String mesatras = UtilGeneral.obtenerTodayMinusNDays(-90); 
		
		if (nDays == 0){
			fechaHoy = UtilGeneral.obtenerToday();  
			mesatras = UtilGeneral.obtenerTodayMinusNDays(-90); 
		}else{
			fechaHoy = UtilGeneral.obtenerTodayMinusNDays(nDays);
			mesatras = UtilGeneral.obtenerTodayMinusNDays(-90+nDays); 
			
		}
		
		
			
		switch (this.getTendenciaGoogle(companySymbol, fechaHoy, mesatras)) {
		case minusalza:
			return 0; // "-alza";
		case minusbaja:
			return 1; //"-baja";
		case ALZA:
			return 2; //"ALZA";
		case BAJA:
			return 3; //"BAJA";
		case NO_EVALUADA:
			System.out.println("Se llama de forma recursiva a getTendencia " + (diasIntentos--));
			this.variableGlobalIntentos++;
			if (variableGlobalIntentos > 3){
				return null;
			}
			return getTendenciaGoogle(companySymbol, nDays + (diasIntentos));
		default:
			break; 

		}
		
		return null;
		
	}
	
	/**
	 * @param companySymbol
	 * @param nDays
	 * Obtener la tendencia segun 4 estados
	 * /*
     * 	(0) - alza
		(1)	- baja
		(2)	Alza
		(3)	Baja
		enum TENDENCIA {minusalza, minusbaja, ALZA, BAJA;}
     */
	private Integer getTendenciaGoogleByHTML(Company cmp, int nDays){
		
		//yyyy-MM-dd
		String fechaHoy = UtilGeneral.obtenerToday();  
		String mesatras = UtilGeneral.obtenerTodayMinusNDays(-90); 
		
		if (nDays == 0){
			fechaHoy = UtilGeneral.obtenerToday();  
			mesatras = UtilGeneral.obtenerTodayMinusNDays(-90); 
		}else{
			fechaHoy = UtilGeneral.obtenerTodayMinusNDays(nDays);
			mesatras = UtilGeneral.obtenerTodayMinusNDays(-90+nDays); 
			
		}
		
		
			
		switch (this.getTendenciaGoogleByHTML(cmp, fechaHoy, mesatras)) {
		case minusalza:
			return 0; // "-alza";
		case minusbaja:
			return 1; //"-baja";
		case ALZA:
			return 2; //"ALZA";
		case BAJA:
			return 3; //"BAJA";
		case NO_EVALUADA:
			System.out.println("Se llama de forma recursiva a getTendencia " + (diasIntentos--));
			this.variableGlobalIntentos++;
			if (variableGlobalIntentos > 3){
				return null;
			}
			return getTendenciaGoogleByHTML(cmp, nDays + (diasIntentos));
		default:
			break; 

		}
		
		return null;
		
	}
	
	/**
	 * @param companySymbols
	 * @param diasAtras
	 * Obtener el RSI de varias companias dado una fecha hacia atras
	 */
	private void relativeStrengthIndexArray(String companySymbols, String diasAtras, String idIteracion){
		
		String cmpSymbol[] = companySymbols.split(";");
		
		for (String string : cmpSymbol) {
			if (string != null){
				string = string.replace(',', '.');
			}
		  //this.relativeStrengthIndex(string.trim(), Integer.parseInt(diasAtras), false, idIteracion);
		  this.relativeStrengthIndexFromGoogle(string.trim(), Integer.parseInt(diasAtras), false, idIteracion);
		}
		
		
				
		
	}

	
	
	

	/**
	 * Obtine listado de RSI Data
	 * @return
	 */
	/**
	 * @param symbol
	 * @param dateEnd: Fecha mas proxima
	 * @param dateBegin: Fecha mas lejana
	 * @return
	 * Ya no se usa porque el servicio historico de Yahoo ya no esta trayendo la informacion
	 *   se debe usar el metodo: relativeStrengthIndexDinamico
	 */
	/*
	@Deprecated
	private List<RelativeStrengthIndexData> getListaRSIYahoo(String symbol, String dateEnd, String dateBegin, boolean print){
		List<RelativeStrengthIndexData> lstRSI = null;
		lstRSI = new ArrayList<RelativeStrengthIndexData>();
		
		//String fechaHoy = UtilGeneral.obtenerToday();   //"2016-08-04";
		//String mesatras = UtilGeneral.obtenerTodayMinusMonth();  //"2016-07-04";
		
		ReturnHistoricaldataYahooFinance rHistData = null;
		try {
			String urlHistdata = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22"+symbol+"%22%20and%20startDate%20%3D%20%22"+dateBegin+"%22%20and%20endDate%20%3D%20%22"+dateEnd+"%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
			if (print){
				System.out.println("urlHistdata: ["+urlHistdata+"]");
				System.out.println("Date,Open,High,Low,Close,Adj Close");
			}
			rHistData =	this.executeYahooIndexHistoricaldata(urlHistdata);
			
			
			if (null != rHistData && null != rHistData.getQuery() 
					&& null !=  rHistData.getQuery().getResults() && null != rHistData.getQuery().getResults().getQuote()){
				int ctd = 0;
				for (ReturnHistoricaldataYahooFinance.Query.Results.Quote quote :  rHistData.getQuery().getResults().getQuote()) {
					RelativeStrengthIndexData rsid = new RelativeStrengthIndexData();
			        rsid.setId(++ctd);
			        DateFormat formatter1;
			        formatter1 = new SimpleDateFormat("yyyy-mm-DD");
			        rsid.setFecha(  formatter1.parse(quote.getDate()) ) ;
			        rsid.setClose(Double.parseDouble(quote.getAdj_Close()));
			        rsid.setHigh(Double.parseDouble(quote.getHigh()));
			        rsid.setLow(Double.parseDouble(quote.getLow()));
			        //System.out.println("quote ["+ctd+"] : " +quote.toString());
			        if (print){
			        	System.out.println(quote.toString());
			        }
			        lstRSI.add(rsid);
			        
			        if (ctd > 13){
			        	break;
			        }
				}
			}
				
			
			//System.out.print( tr.toString() );
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return lstRSI;
	}
	*/
	
	
	/**
	 * @param symbol
	 * @param dateEnd
	 * @param dateBegin
	 * @param print
	 * @return
	 * Obtiene la tendencia de la compania
	 */
	private TENDENCIA getTendenciaGoogle(String symbol, String dateEnd, String dateBegin){
		
		Double valorTresMesesAtras = null;
		Double valorHoy = null;
		//String fechaHoy = UtilGeneral.obtenerToday();   //"2016-08-04";
		//String mesatras = UtilGeneral.obtenerTodayMinusMonth();  //"2016-07-04";
		
		String urlDataHoy = null;
		String urlDataTresMonthBefore = null;
		
		//ReturnSingleDataYahooFinance rHistDataHoy = null;
		//ReturnSingleDataYahooFinance rHistDataTresMonthBefore = null;
		try {
			//urlDataHoy = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22"+symbol+"%22%20and%20startDate%20%3D%20%22"+dateEnd+"%22%20and%20endDate%20%3D%20%22"+dateEnd+"%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
			//urlDataTresMonthBefore = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22"+symbol+"%22%20and%20startDate%20%3D%20%22"+dateBegin+"%22%20and%20endDate%20%3D%20%22"+dateBegin+"%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
			//https://www.google.com/finance/historical?q=EPA:CA
			//https://pypi.python.org/pypi/googlefinance
			//
			urlDataHoy = "https://finance.google.ca/finance/historical?q="+symbol.replace(":", "%3A")+"&startdate="+dateEnd.replace(" ", "%20")+"&enddate="+dateEnd.replace(" ", "%20")+"&output=csv";
			urlDataTresMonthBefore = "https://finance.google.ca/finance/historical?q="+symbol.replace(":", "%3A")+"&startdate="+dateBegin.replace(" ", "%20")+"&enddate="+dateBegin.replace(" ", "%20")+"&output=csv";
			valorHoy =	this.executeGoogleIndexSingleData(urlDataHoy);
			valorTresMesesAtras =	this.executeGoogleIndexSingleData(urlDataTresMonthBefore);
			/*
			rHistDataHoy =	this.executeYahooIndexSingleData(urlDataHoy);
			rHistDataTresMonthBefore =	this.executeYahooIndexSingleData(urlDataTresMonthBefore);
			if (null != rHistDataHoy && null != rHistDataHoy.getQuery() 
					&& null !=  rHistDataHoy.getQuery().getResults() && null != rHistDataHoy.getQuery().getResults().getQuote()){
					valorHoy = Double.parseDouble(rHistDataHoy.getQuery().getResults().getQuote().getAdj_Close());
			}
			if (null != rHistDataTresMonthBefore && null != rHistDataTresMonthBefore.getQuery() 
					&& null !=  rHistDataTresMonthBefore.getQuery().getResults() && null != rHistDataTresMonthBefore.getQuery().getResults().getQuote()){
					valorTresMesesAtras = Double.parseDouble(rHistDataTresMonthBefore.getQuery().getResults().getQuote().getAdj_Close());
				
			}
			*/
			
			if (null == valorHoy | null == valorTresMesesAtras){
				return TENDENCIA.NO_EVALUADA;
			}
			
			if (valorHoy > valorTresMesesAtras){
				
				double res = valorHoy - valorTresMesesAtras;
				if (res < 1.2d){
					return TENDENCIA.minusalza;
				}
				return TENDENCIA.ALZA;
			}else{
				double res = valorTresMesesAtras - valorHoy;
				if (res < 1.2d){
					return TENDENCIA.minusbaja;
				}
				return TENDENCIA.BAJA;	
			}
				
			
			//System.out.print( tr.toString() );
		} catch (IOException e) {
			System.out.println("urlDataHoy: ["+urlDataHoy+"]");
			System.out.println("urlDataTresMonthBefore: ["+urlDataTresMonthBefore+"]");
			System.out.println("Error al evaluar la tendencia" + e.getMessage()) ;
		} 
		
		
		return TENDENCIA.NO_EVALUADA;
	}
	
	/**
	 * @param symbol
	 * @param dateEnd
	 * @param dateBegin
	 * @param print
	 * @return
	 * Obtiene la tendencia de la compania
	 */
	private TENDENCIA getTendenciaGoogleByHTML(Company cmp, String dateEnd, String dateBegin){
		
		Double valorTresMesesAtras = null;
		Double valorHoy = null;
		
		String urlDataHoy = null;
		String urlDataTresMonthBefore = null;
		
		try {
			urlDataHoy = "https://finance.google.ca/finance/historical?cid="+cmp.getCid()+"&startdate="+dateEnd.replace(" ", "%20")+"&enddate="+dateEnd.replace(" ", "%20")+"&num=30";
			urlDataTresMonthBefore = "https://finance.google.ca/finance/historical?cid="+cmp.getCid()+"&startdate="+dateBegin.replace(" ", "%20")+"&enddate="+dateBegin.replace(" ", "%20")+"&num=30";
			valorHoy =	this.executeGoogleIndexSingleDataByHTML(urlDataHoy);
			valorTresMesesAtras =	this.executeGoogleIndexSingleDataByHTML(urlDataTresMonthBefore);
			
			if (null == valorHoy | null == valorTresMesesAtras){
				return TENDENCIA.NO_EVALUADA;
			}
			
			if (valorHoy > valorTresMesesAtras){
				
				double res = valorHoy - valorTresMesesAtras;
				if (res < 1.2d){
					return TENDENCIA.minusalza;
				}
				return TENDENCIA.ALZA;
			}else{
				double res = valorTresMesesAtras - valorHoy;
				if (res < 1.2d){
					return TENDENCIA.minusbaja;
				}
				return TENDENCIA.BAJA;	
			}
				
		} catch (IOException e) {
			System.out.println("urlDataHoy: ["+urlDataHoy+"]");
			System.out.println("urlDataTresMonthBefore: ["+urlDataTresMonthBefore+"]");
			System.out.println("Error al evaluar la tendencia" + e.getMessage()) ;
		} 
		
		
		return TENDENCIA.NO_EVALUADA;
	}

	
	//TODO: Filtrar YTD positivo:
	//	 * CAGR=(endingvalue/beginingvalue)elevado[^](1/#deyears) - 1
	//	 * 	REF: http://www.investopedia.com/terms/a/annual-return.asp
	// Compara hoy y hace un aÃ±o
	//private Double getAnnualReaturn(String companySymbol){
		
	//}
	
	
	/*
	 * Ecuacion de regla de tres
	 * Compara hoy con respecto al primero de enero. 
	 * 
	 * FIXME: ##Si estoy a principio de anio (Enero 01 a 30 de abril) comparar con Junio!
	 */
	private Double getYearToDateReturn(String companySymbol) throws IllegalStateException, Exception{
		
		Double returnPorcentajeYTD = null;
		
		//Valor stock a hoy
		List<RelativeStrengthIndexData> valuePonderadoToday = null;
		valuePonderadoToday = UtilGeneral.getListaRSIGoogle(companySymbol, UtilGeneral.obtenerToday(), UtilGeneral.obtenerTodayMinusThree(), false);
		//valuePonderadoToday = this.getListaRSIYahoo(companySymbol, UtilGeneral.obtenerToday(), UtilGeneral.obtenerTodayMinusThree(), false);	
		
		//Valor stock a principio del anio
		List<RelativeStrengthIndexData> valuePonderadoBeginYear = null;
		valuePonderadoBeginYear = UtilGeneral.getListaRSIGoogle(companySymbol, UtilGeneral.obtenerFirstDateOftheYearPlusOne(), UtilGeneral.obtenerFirstDateOftheYearMinusOne(), false);	
		
		Double valorActual = 0d;
		Double valorBeginYear = 0d;
		//Obtiene el valor ajustado al cierre [Adj Close] a la fecha
		valorActual = valuePonderadoToday.size() > 0?(valuePonderadoToday.get(0)==null?0d:valuePonderadoToday.get(0).getClose()):0d;
		for (RelativeStrengthIndexData tmpVBY : valuePonderadoBeginYear) {
			if (tmpVBY.getClose() > valorBeginYear){
				valorBeginYear = tmpVBY.getClose();
			}
			
		}

		//Calculo de regla de tres para consultar el porcentaje de YTD
		returnPorcentajeYTD = ((100*valorActual)/valorBeginYear)-100;
		
		return returnPorcentajeYTD;

		
	}
	
	
	/*
	 * Algoritmo que:
	 * 	1. Consula en la BD por Numero de Iteracion
	 * 	2. Da un peso a cada variable del data mining
	 *  3. Genera un respuesta idicando si el peso de ganancia es mayor al de perdida
	 *  
	 * REF: 
	 *  Book: Data Mining, Practical machine learning, tools and techniques
	 *		Chapter: 4.2
	 *
	 * FIXME: Ajustar los pesos con la variable YTD de la plataforma.
	 */
	/*
	 * Tener en cuenta esta consulta>
	 * 
SELECT	CASE 
			WHEN d.DMC_TENDENCIA = 0 THEN '(0) - alza'
			WHEN d.DMC_TENDENCIA = 1 THEN '(1)	- baja'
			WHEN d.DMC_TENDENCIA = 2 THEN '(2)	Alza'
			WHEN d.DMC_TENDENCIA = 3 THEN '(3)	Baja'
		END as tendencia, 
		c.id, c.name, c.urlIndex, d.DMC_STOCK_PRICE, d.DMC_FECHA_CREACION, d.DMC_RELATIVE_STRENGTH_INDEX
FROM		indexyahoocfd.company c   inner join  indexyahoocfd.dmc_data_mining_company d on d.SCN_CODIGO = c.id 
--WHERE	name like '%Vonovia SE%'
WHERE	d.DMC_CODIGO_GRP_ITERACION = 1611040744
ORDER by d.DMC_FECHA_CREACION desc

     SIEMPRE:
      	1. VERIFICAR QUE EL rsi ESTE POR DEBAJO DE 60
      	2. https://online.capitalcube.com/#!/stock/gb/london/pdl, verificar CapitalCube4
      	3. Sector: seguros y finanzas muy riesgoso
	 * 
	 */
	private void getStatisticalModeling(Long numeroIteracion) throws Exception{
		
		System.out.println("Evalua numero de iteracion: " + numeroIteracion);
		
		try{
		
		DataMiningCompany dmCmp = new DataMiningCompany();
		dmCmp.setIdIteracion(numeroIteracion);
		
		List<DataMiningCompany> lstDM = admEnt.getDMCompanyByIteracion(dmCmp);
		
		for (DataMiningCompany dataMiningCompany : lstDM) {
			try{
			Double probabiliadadWinTotal = null;
			Double probabiliadadLostTotal = null;
			//System.out.println(dataMiningCompany.toString());
			PriceBetweenHighLow probabilidadVariable01 = new PriceBetweenHighLow();
			double probabiliadadWin01  = probabilidadVariable01.getWinStatistics(dataMiningCompany.getIsPriceBetweenHighLow());
			double probabiliadadLost01 = probabilidadVariable01.getLostStatistics(dataMiningCompany.getIsPriceBetweenHighLow());
			//System.out.print("W1" + probabiliadadWin01);
			//System.out.print("L1" + probabiliadadLost01);
			
			TendenciaTresMeses probabilidadVariable02 = new TendenciaTresMeses();
			double probabiliadadWin02  =   probabilidadVariable02.getWinStatistics(dataMiningCompany.getTendencia());
			double probabiliadadLost02 =   probabilidadVariable02.getLostStatistics(dataMiningCompany.getTendencia());
			//System.out.print("W2" + probabiliadadWin02);
			//System.out.print("L2" + probabiliadadLost02);
			
			PricePercentageIncrement probabilidadVariable03 = new PricePercentageIncrement();
			double probabiliadadWin03  =   probabilidadVariable03.getWinStatistics(Double.parseDouble( dataMiningCompany.getPercentageIncrement().replace(',','.').trim() ));
			double probabiliadadLost03 =   probabilidadVariable03.getLostStatistics(Double.parseDouble( dataMiningCompany.getPercentageIncrement().replace(',','.').trim() ));
			//System.out.print("W3" + probabiliadadWin03);
			//System.out.print("L3" + probabiliadadLost03);
			
			NotaPonderada probabilidadVariable04 = new NotaPonderada();
			double probabiliadadWin04  =   probabilidadVariable04.getWinStatistics(Double.parseDouble(dataMiningCompany.getNotaPonderada().replace(',','.').trim()));
			double probabiliadadLost04 =   probabilidadVariable04.getLostStatistics(Double.parseDouble(dataMiningCompany.getNotaPonderada().replace(',','.').trim()));
			//System.out.print("W4" + probabiliadadWin04);
			//System.out.print("L4" + probabiliadadLost04);
			
			PriceEarningRatio probabilidadVariable05 = new PriceEarningRatio();
			double probabiliadadWin05  =   probabilidadVariable05.getWinStatistics(Double.parseDouble(dataMiningCompany.getPriceEarningRatio().replace(',','.').trim()));
			double probabiliadadLost05 =   probabilidadVariable05.getLostStatistics(Double.parseDouble(dataMiningCompany.getPriceEarningRatio().replace(',','.').trim()));
			//System.out.print("W5" + probabiliadadWin05);
			//System.out.print("L5" + probabiliadadLost05);
			
			RelativeStrengthIndex probabilidadVariable06 = new RelativeStrengthIndex();
			double probabiliadadWin06  =   probabilidadVariable06.getWinStatistics(Double.parseDouble(dataMiningCompany.getRelativeStrengthIndex().replace(',','.').trim()));
			double probabiliadadLost06 =   probabilidadVariable06.getLostStatistics(Double.parseDouble(dataMiningCompany.getRelativeStrengthIndex().replace(',','.').trim()));
			//System.out.print("W6" + probabiliadadWin06);
			//System.out.print("L6" + probabiliadadLost06);
			
			PrecioAccion probabilidadVariable07 = new PrecioAccion();
			double probabiliadadWin07  =   probabilidadVariable07.getWinStatistics(Double.parseDouble(dataMiningCompany.getStockPrice().replace(',','.').trim()));
			double probabiliadadLost07 =   probabilidadVariable07.getLostStatistics(Double.parseDouble(dataMiningCompany.getStockPrice().replace(',','.').trim()));
			//System.out.print("W7" + probabiliadadWin07);
			//System.out.print("L7" + probabiliadadLost07);
			
			LastDigit probabilidadVariable08 = new LastDigit();
			double probabiliadadWin08  =   probabilidadVariable08.getWinStatistics(dataMiningCompany.getLastDigitStockPrice());
			double probabiliadadLost08 =   probabilidadVariable08.getLostStatistics(dataMiningCompany.getLastDigitStockPrice());
			//System.out.print("W8" + probabiliadadWin08);
			//System.out.print("L8" + probabiliadadLost08);
			
			DiffMaxMin probabilidadVariable09 = new DiffMaxMin();
			double probabiliadadWin09  =   probabilidadVariable09.getWinStatistics(Double.parseDouble(dataMiningCompany.getDiffMaxMin().replace(',','.').trim()));
			double probabiliadadLost09 =   probabilidadVariable09.getLostStatistics(Double.parseDouble(dataMiningCompany.getDiffMaxMin().replace(',','.').trim()));
			//System.out.print("W9" + probabiliadadWin09);
			//System.out.print("L9" + probabiliadadLost09);
			
			PercentageIncremento probabilidadVariable10 = new PercentageIncremento();
			double probabiliadadWin10  =   probabilidadVariable10.getWinStatistics(Double.parseDouble(dataMiningCompany.getPercentageIncrement().replace(',','.').trim()));
			double probabiliadadLost10 =   probabilidadVariable10.getLostStatistics(Double.parseDouble(dataMiningCompany.getPercentageIncrement().replace(',','.').trim()));
			//System.out.print("W10" + probabiliadadWin10);
			//System.out.print("L10" + probabiliadadLost10);
			
			StockMayorMedia probabilidadVariable11 = new StockMayorMedia();
			double probabiliadadWin11  =   probabilidadVariable11.getWinStatistics(dataMiningCompany.getIsTockPriceMayorMedia());
			double probabiliadadLost11 =   probabilidadVariable11.getLostStatistics(dataMiningCompany.getIsTockPriceMayorMedia());
			//System.out.print("W11" + probabiliadadWin11);
			//System.out.print("L11" + probabiliadadLost11);
			
			//Indicador/peso de la ganancia o perdida de la accion al final del dia
			double probabiliadadWin12  =   IStatistics.ganoVariacionPrecio;
			double probabiliadadLost12 =   IStatistics.perdioVariacionPrecio;
			//System.out.print("W12" + probabiliadadWin12);
			//System.out.print("L12" + probabiliadadLost12);
			
			
			probabiliadadWinTotal  = probabiliadadWin01 * probabiliadadWin02 * probabiliadadWin03 * probabiliadadWin04 * probabiliadadWin05 * probabiliadadWin06 * probabiliadadWin07 * probabiliadadWin08 * probabiliadadWin09 * probabiliadadWin10 * probabiliadadWin11 * probabiliadadWin12;
			probabiliadadLostTotal = probabiliadadLost01 * probabiliadadLost02 * probabiliadadLost03 * probabiliadadLost04 * probabiliadadLost05 * probabiliadadLost06 * probabiliadadLost07 * probabiliadadLost08 * probabiliadadLost09 * probabiliadadLost10 * probabiliadadLost11 * probabiliadadLost12;
			
			System.out.println("*******************(ini) [" +dataMiningCompany.getCompany().getName()+ "]*************************" );
			if (probabiliadadWinTotal  > probabiliadadLostTotal && Double.parseDouble( dataMiningCompany.getRelativeStrengthIndex().replace(',','.').trim() ) < 60 ){
				//System.out.println("Tiene probabilidad de ganancia al final del dia ");
				System.out.println ("["+ dataMiningCompany.getCompany().getId()+"-"+dataMiningCompany.getCompany().getName() + "]probabiliadadWin - Lost *10000-->"+  ((probabiliadadWinTotal - probabiliadadLostTotal) * 10000) );
				System.out.println(dataMiningCompany.toString());
				
			}else{
				System.out.println ("["+ dataMiningCompany.getCompany().getId()+"-"+dataMiningCompany.getCompany().getName() + "]probabiliadadLost - Lost *10000-->"+  ((probabiliadadWinTotal - probabiliadadLostTotal) * 10000) );
			}
			
			Double ytd = Double.parseDouble(dataMiningCompany.getYTDPlataforma().replace(',', '.').trim() );
			
			if ( Double.parseDouble(dataMiningCompany.getDiffMaxMin().replace(',','.').trim() ) > 49 |  Double.parseDouble( dataMiningCompany.getPercentageIncrement().replace(',','.').trim() ) >= 3
					| (ytd > 0 && ytd < 20)){
				System.out.println("Diferencia mayor a 49 DataMining* OR %IncMayorIgual3 DataMining OR YTD between 0 - 20");
				System.out.println ("dataMiningCompany.getDiffMaxMin()"+  dataMiningCompany.getDiffMaxMin() );
				System.out.println ("dataMiningCompany.getPercentageIncrement()"+ dataMiningCompany.getPercentageIncrement() );
				System.out.println ("dataMiningCompany.getYTDPlataforma()"+ dataMiningCompany.getYTDPlataforma() );
				System.out.println(dataMiningCompany.toString());
			}
			System.out.println("*******************(fin) [" +dataMiningCompany.getCompany().getName()+ "]*************************" );
			
			}catch (NumberFormatException e) {
				System.out.println("NumberFormatException");
			}catch (NullPointerException e) {
				System.out.println("NullPointerException");
			}
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}
	
	
	/**
	 * @param omi
	 * @param argumento2
	 * @param cortePorcentajePonderado
	 * @throws BusinessException
	 * @throws IOException
	 */
	private static void runManytimes(ObtenerMarketIndex omi, Integer argumento2, Integer cortePorcentajePonderado) throws BusinessException, IOException{

		for (;;){
			cr = null;
			System.out.println("\n Persiste info de las companias, consultando de yahoo  [Go long]");
			omi.printPERatio();
			System.out.println("Precio accion menor = & % volumen mayor a cero!" + " Time:" + new Date());
			omi.printOBV(argumento2, cortePorcentajePonderado, Evalua.ONE);
			System.out.println("Precio accion mayor & % volumen mayor a cero!" + "Time:" + new Date());
			omi.printOBV(argumento2, cortePorcentajePonderado, Evalua.THREE);
			System.out.println("\n FIN, esperar 10 minutos.." + new Date());
			try {
				//Cuando este en idle deberia liberar los objetos
				System.gc();
				Thread.sleep(600000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}//10 minutos
		}
		
	}

}
