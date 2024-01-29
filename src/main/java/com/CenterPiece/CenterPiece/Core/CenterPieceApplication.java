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

		String environment = "Production";
<<<<<<< HEAD
		//String environment = "Test";
=======
//		String environment = "Test";
>>>>>>> 44be36f8b45774f9c168f77640bb4bb4042133d5

		HttpClient client = HttpClient.newBuilder().build();
		ScheduledExecutorService session = Executors.newSingleThreadScheduledExecutor();

		Calendar.getInstance();

		session.scheduleAtFixedRate(() -> {

			TimeHandler timeHandler = new TimeHandler();

			System.out.println("\n--- Ran at: " + timeHandler.getCurrentHour() + ":" + timeHandler.getCurrentMinuteOfHour() + " ---\n");

//			CenterPieceSession centerPieceSessionCabinets = new CenterPieceSession("CABINETS", client, environment);
//			centerPieceSessionCabinets.mainProcess();

			CenterPieceSession centerPieceSessionFabrication = new CenterPieceSession("FABRICATION", client, environment);
			centerPieceSessionFabrication.mainProcess();

		}, 0, 300, TimeUnit.SECONDS);
	}
}
