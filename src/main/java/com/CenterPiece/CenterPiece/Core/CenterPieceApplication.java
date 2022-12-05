package com.CenterPiece.CenterPiece.Core;

import com.CenterPiece.CenterPiece.TimeHandler;

import java.net.http.HttpClient;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class CenterPieceApplication {

	public static void main(String[] args) {
		//SpringApplication.run(CenterPieceApplication.class, args);

		HttpClient client = HttpClient.newBuilder().build();
		ScheduledExecutorService session = Executors.newSingleThreadScheduledExecutor();

		Calendar.getInstance();

		session.scheduleAtFixedRate(() -> {

//			DateTime dtoo = new DateTime();
//			DateTimeZone dtZone = DateTimeZone.forID("America/Chicago");
//			DateTime dt = dtoo.withZone(dtZone);
//
//			String currentHour;
//			if(dt.getHourOfDay()<10)
//				currentHour = "0" + dt.getHourOfDay();
//			else
//				currentHour = "" + dt.getHourOfDay();
//
//			String currentMinute;
//			if(dt.getMinuteOfHour()<10)
//				currentMinute = "0" + dt.getMinuteOfHour();
//			else
//				currentMinute = "" + dt.getMinuteOfHour();

			TimeHandler timeHandler = new TimeHandler();

			System.out.println("\n--- Ran at: " + timeHandler.getCurrentHour() + ":" + timeHandler.getCurrentMinuteOfHour() + " ---\n");

			CenterPieceSession centerPieceSessionCabinets = new CenterPieceSession("CABINETS", client);
			centerPieceSessionCabinets.mainProcess();

			CenterPieceSession centerPieceSessionFabrication = new CenterPieceSession("FABRICATION", client);
			centerPieceSessionFabrication.mainProcess();

		}, 0, 180, TimeUnit.SECONDS);
	}
}
