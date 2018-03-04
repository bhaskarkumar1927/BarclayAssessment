package solution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceEngine {
	
	
public static void main(String args[]) throws IOException{
	
	String FileNames[] = {"Input1.txt","Input2.txt"};
	
	for(String file:FileNames){
		System.out.println();
		System.out.println("***********************************************************");
	System.out.println("For File :"+file);
	
	int noOfProducts = 0,noOfSurveys=0;
	List<String> completeData = new ArrayList<String>();
	Map<String,Integer> items = new HashMap<String,Integer>();
	Map<String,Double> itemPrices = new HashMap<String,Double>();
	FileReader freader = new FileReader("resources/"+file);  
    BufferedReader br = new BufferedReader(freader);  
    String s;  
    while((s = br.readLine()) != null) {  
    	if(s.trim().length()>0)
    	completeData.add(s);  
    }  
    freader.close();
    
    try{
    noOfProducts = Integer.parseInt(completeData.get(0).trim());
    
    noOfSurveys = Integer.parseInt(completeData.get(noOfProducts+1).trim());
    
    items = getItems(completeData,noOfProducts);
    itemPrices = getItemPricess(completeData,noOfProducts,noOfSurveys);
    
    System.out.println("--INPUT DATA--");
    for(String ss:completeData)
    	System.out.println(ss);
    
    System.out.println("--------------RESULT----------------------");
    
    for (Map.Entry<String, Double> entry : itemPrices.entrySet()) {
    	String item = entry.getKey();
		Double price = entry.getValue();
		Double markedPrice = price * (100 + items.get(item))/100;
		
		if(markedPrice == 0)
			System.out.println(item + " Add data is either promotion Or DataError");
		else
			System.out.println(item + " " +markedPrice);
    	
    }
    
    }catch(Exception e)
    {
    	e.printStackTrace();
    	System.out.println("Invalid Data File");
    	return;
    }
	

	}
	
}

private static Map<String, Double> getItemPricess(List<String> completeData, int noOfProducts, int noOfSurveys) throws Exception{
	Map<String,String> itemPrices = new HashMap<String,String>();
	Map<String,Double> itemLeastPrices = new HashMap<String,Double>();
	
	for(int i = noOfProducts+2;i<=noOfProducts+noOfSurveys+1;i++)
	{
		String itemInfo = completeData.get(i);
		String item[]=itemInfo.split(" ");
		String itemName = item[0].trim();
		String price = item[2].trim();
		if(itemPrices.containsKey(itemName)){
			String oldPrice =itemPrices.get(itemName);
			itemPrices.put(itemName, oldPrice+","+price);
		}
		else
			itemPrices.put(itemName, price);
	}
	
	for (Map.Entry<String, String> entry : itemPrices.entrySet()) {
		String item = entry.getKey();
		String pricesStr = entry.getValue();
		String prices[] = pricesStr.split(",");
		
		Double leastValue = getLeast(prices);
		itemLeastPrices.put(item,leastValue);

	}
	
	return itemLeastPrices;
}

private static Double getLeast(String[] prices) {
	Double least = 0.0;
	Double total = 0.0,avg = 0.0;
	Map<String,Integer> count = new HashMap<String,Integer>(); 
	
	for(String price:prices)
		total += Double.parseDouble(price);
	avg = total/prices.length;
	Double avgplus = avg * 1.5;
	Double avgminus = avg * 0.5;
	
	for(String price:prices)
		{
		Double p = Double.parseDouble(price);
		if(p>=avgminus && p<=avgplus)
		{
			if(count.containsKey(price))
				count.put(price, count.get(price)+1);
			else
				count.put(price, 1);
		}
		}
	int maxCount = 0;
	for (Map.Entry<String, Integer> entry : count.entrySet()) {
		Double price = Double.parseDouble(entry.getKey());
		int cnt =  entry.getValue();
		
		if(cnt>maxCount){
			maxCount = cnt;
			least = price;
		}
		else if(cnt == maxCount && price<least)
			least = price;
	}
	

	return least;
}

private static Map<String, Integer> getItems(List<String> completeData, int noOfProducts) throws Exception{
	
	Map<String,Integer> items = new HashMap<String,Integer>();
	
	for(int i=1;i<=noOfProducts;i++){
		String itemInfo = completeData.get(i);
		String item[]=itemInfo.split(" ");
		String itemName = item[0].trim();
		String supply = item[1].trim();
		String demand = item[2].trim();
		if(supply.equals("H")&&demand.equals("H"))
			items.put(itemName, 0);
		else if(supply.equals("H")&&demand.equals("L"))
			items.put(itemName, -5);
		else if(supply.equals("L")&&demand.equals("H"))
			items.put(itemName, 5);
		else 
			items.put(itemName, 10);
	}
	
	return items;
}

}