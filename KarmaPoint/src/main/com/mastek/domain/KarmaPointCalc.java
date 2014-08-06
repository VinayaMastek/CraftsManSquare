package com.mastek.domain;

public class KarmaPointCalc {

	public Double CalculateKP(int userId){
		PostgresImpl pg = new PostgresImpl();
		MsgSummary msgSummary = pg.getMsgSummary(userId);
		
		double thrdpoints = msgSummary.getThreads() * 1.5;
		double postReplied = msgSummary.getPostreplied() * 0.5;
		
		Double kp = thrdpoints + postReplied;
		
		if (msgSummary.getAvglikes() == 1)
			kp = (kp*0.1)+ kp;
		
		if (msgSummary.getAvglikes() == 2)
			kp = (kp*0.2)+ kp;
			
		if (msgSummary.getAvglikes() == 3)
			kp = (kp*0.3)+ kp;

		if (msgSummary.getAvglikes() > 3)
			kp = (kp*0.5)+ kp;
			
		Double noRespPercent = (double) (msgSummary.getNoresponsethreads()/msgSummary.getThreads());
		if (noRespPercent >= 0.05 && noRespPercent < 0.1)
			kp = kp - (kp*0.05); 
			
		if (noRespPercent >= 0.1 && noRespPercent < 0.25)
			kp = kp - (kp*0.1); 
		
		if (noRespPercent >= 0.25 && noRespPercent < 0.5)
			kp = kp - (kp*0.15);
		
		if (noRespPercent >0.5)
			kp = kp - (kp*0.30);

		return kp;
	}
	
}
