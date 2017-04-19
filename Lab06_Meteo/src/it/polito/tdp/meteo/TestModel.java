package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class TestModel {

	public static void main(String[] args) {

		Model m = new Model();
		
		System.out.println(m.getUmiditaMedia(12));
		
	
		
		MeteoDAO mDAO = new MeteoDAO();
	//	System.out.println(mDAO.getAllRilevamenti());
	
	
		
		
		
	System.out.println(m.trovaSequenza(5));
		
		System.out.println(m.trovaSequenza(4));
	
		
		
	}


}
