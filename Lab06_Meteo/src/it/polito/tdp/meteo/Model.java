package it.polito.tdp.meteo;

import java.util.*;
import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {
//PROVARE A RIFARLO: COPIO NELLE 3 LISTE POI  A OGNI PASSO  INSERISCOuno delle 3 city COME SIMPLE CITY INCREMENTANDO IL COUNTER
	private final static int COST = 50;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI =9;
	List <SimpleCity> listaParziale;
	List <SimpleCity>listaBest;
	List<Citta>listaCitta;
	double best;
	int cont=0;
	String prec="";

	
	
	
	public Model() {
		listaCitta=new ArrayList<Citta>();
		Citta mil= new Citta ("Milano");
		Citta tor= new Citta ("Torino");
		Citta gen= new Citta ("Genova");
		listaCitta.add(mil);
		listaCitta.add(tor);
		listaCitta.add(gen);
		
	}
	
	
	public void CaricaRilevamentiMese(int mese){                 
    MeteoDAO mDAO= new MeteoDAO();
		for(Citta c: listaCitta ){
		c.setRilevamenti(mDAO.getAllRilevamentiLocalitaMese(mese, c.getNome()));
	}
	
		}	
	
	
	

	public String getUmiditaMedia(int mese) {             
		MeteoDAO mDAO= new MeteoDAO();
		String result="";
		result+="Milano: "+ mDAO.getAvgRilevamentiLocalitaMese(mese,"Milano")+"\n";
		result+="Genova: "+ mDAO.getAvgRilevamentiLocalitaMese(mese,"Genova")+"\n";
		result+="Torino: "+ mDAO.getAvgRilevamentiLocalitaMese(mese,"Torino")+"\n";
		
		return result;
	}

	public String trovaSequenza(int mese) {
		CaricaRilevamentiMese(mese);
		listaParziale = new ArrayList<SimpleCity>();
		listaBest=new ArrayList<SimpleCity>();
		best=99999999;
		int livello=0;
		cont=0;
		prec="";
		
		
		recursive(listaParziale, livello );
		
		String result="";
		int i=1;
		
		for(SimpleCity s : listaBest){
			result+="Giorno "+i+" : "+s.getNome()+"\n";
			i++;
		}
		return result;
	}
	
	
	private void recursive (List<SimpleCity>listaParziale, int livello){
	    if((livello==NUMERO_GIORNI_TOTALI)){
	    	double score=punteggioSoluzione(listaParziale);
	    	if((score<best)&&(verificaPresenza(listaParziale))){
	    		best=score;
	    		listaBest.clear();
	    		listaBest.addAll(listaParziale);
	    		
	    	}
	    	//copia nel best verificando il punteggio
	    }
	    else{
	    
	    for(int i=0; i<NUMERO_GIORNI_TOTALI;i++){
	    	for(Citta c : listaCitta){
	    		Rilevamento r= c.getRilevamenti().get(i);
	    		SimpleCity sc= new SimpleCity(c.getNome(),r.getUmidita()*COST);
	    		
	    		if(controllaParziale(listaParziale,sc)){
	    		listaParziale.add(sc);
	    		c.setCounter(c.getCounter()+1);
	    		
	    		
	    		recursive(listaParziale,livello+1);	
	    		
	    		listaParziale.remove(livello);
	    		c.setCounter(c.getCounter()-1);
	    		}
	    		
	    	}
	    	
	    }
		
	    }
		
		
		
		
	  }
	
		
		
	
	
	

	private boolean verificaPresenza(List<SimpleCity> soluzioneCandidata) {
	//verificare non piu di 6 della stessa citta
	boolean bool= true;
		for(Citta c: listaCitta){
			if(c.getCounter()>NUMERO_GIORNI_CITTA_MAX||(c.getCounter()==0)){
				bool=false;
				break;
			}
		}
		return bool;
		
	}


	private Double punteggioSoluzione(List<SimpleCity> soluzioneCandidata) {
     double score=0;
     for(int k=0;k<soluzioneCandidata.size();k++){
    	 SimpleCity sc=soluzioneCandidata.get(k);
    	 score+=sc.getCosto();
    	 if(k!=soluzioneCandidata.size()-1){
    	 if(sc.getNome().compareTo(soluzioneCandidata.get(k+1).getNome())==0){
    		 score+=100;
    	 }	 
     }
     }
		
		return score;
	}
	



	private boolean controllaParziale(List<SimpleCity> parziale,SimpleCity sc) {
	boolean bool=false;
	String nuova= sc.getNome();
	int compare = nuova.compareTo(prec);

	
	if(compare==0){
		bool= true;
	}
	
	
	else{
     if((cont>=NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN)||(cont==0))   //è 0 solo al primo passaggio
    	 bool=true;
	}
	
	
	if(bool==true){
		if(compare!=0){
			prec=new String(nuova);
			cont=0;
		}
		cont++;}

	
	
	return bool;

}



	
		
	

	



	
	
	
	
	
	
	
}
