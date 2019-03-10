package io.bankbridge;

import static spark.Spark.get;
import static spark.Spark.port;

import io.bankbridge.handler.BanksCacheBased;
import io.bankbridge.handler.BanksRemoteCalls;

/**
 *
 * Main class used to invoke initiate the application.
 *
 * @author XXX
 * @version 1.0
 * @since 09-03-2019
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {

		port(4567);

		BanksCacheBased.init();
		BanksRemoteCalls.init();

		get("/v1/banks/all", (request, response) -> BanksCacheBased.handle(request, response));
		get("/v2/banks/all", (request, response) -> BanksRemoteCalls.handle(request, response));
	}
}