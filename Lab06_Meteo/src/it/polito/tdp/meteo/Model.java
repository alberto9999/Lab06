package it.polito.tdp.meteo;

import java.util.*;
import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {

	private final static int COST = 50;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI =15;
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
	 // 		System.out.println(best);
	    		
	    	}
	    	//copia nel best verificando il punteggio
	    }
	    else{
	    
	//    for(int i=livello; i<NUMERO_GIORNI_TOTALI;i++){
	    	for(Citta c : listaCitta){
	    		Rilevamento r= c.getRilevamenti().get(livello);
	    		SimpleCity sc= new SimpleCity(c.getNome(),r.getUmidita()*COST);
	    		
	    		if(controllaParziale(listaParziale,sc)){
	    		listaParziale.add(sc);
	    		c.setCounter(c.getCounter()+1);
	    		
	    		recursive(listaParziale,livello+1);	
    		
	    		listaParziale.remove(livello);
	    		c.setCounter(c.getCounter()-1);
	  		
	    	//	}	
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
     for(int k=0;k<NUMERO_GIORNI_TOTALI-1;k++){
    	 SimpleCity sc=soluzioneCandidata.get(k);
    	 score+=sc.getCosto();
    	
    	 if(sc.getNome().compareTo(soluzioneCandidata.get(k+1).getNome())!=0){
    		 score+=100;
    	 	 
     }
     }
		
		return score;
	}
	


	private boolean controllaParziale(List<SimpleCity> parziale,SimpleCity sc) {
    int dim=parziale.size();
	
    if(dim==0)               //passo 0
		return true;
	
    
    
    if(dim>0 && dim<=NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN){                   //primi 3 passi devono essere uguali
		if(parziale.get(dim-1).getNome().compareTo(sc.getNome())==0)
			return true;
	}
		

	if(dim>NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN-1){                          //dopo i primi 3 passi prima di ogni immissione verifico che i 3 precedenti siano uguali
		if(sc.getNome().compareTo(parziale.get(dim-1).getNome())==0){
			return true;
		}
		else{
			if((parziale.get(dim-1).getNome().compareTo(parziale.get(dim-2).getNome())==0)&&
			(parziale.get(dim-1).getNome().compareTo(parziale.get(dim-3).getNome())==0)){
				return true;
			}
		}
	}
		
		
		return false;
	
	
}


	
}
