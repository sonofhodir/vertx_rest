package io.vertx.rest.first;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class MyFirstVerticle extends AbstractVerticle {

    // Store our product
    private Map<String, Account> accounts = new LinkedHashMap<>();

    // Create some product
    private void createSomeData() {
        Account acc1 = new Account("email1@gmail.com", 200);
        accounts.put(acc1.getEmail(), acc1);
        Account acc2 = new Account("email2@gmail.com", 0);
        accounts.put(acc2.getEmail(), acc2);
    }

    @Override
    public void start(Future<Void> fut) {
        //in memory data store for now
        createSomeData();

        // Create a router object.
        Router router = Router.router(vertx);

        // Bind "/" to our hello message - so we are still compatible.
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("<h1>Hello from my first Vert.x 3 application</h1>");
        });

        //endpoints
        router.get("/api/accounts").handler(this::getAll);
        //explicitly enable to allow read request's body
        router.route("/api/accounts*").handler(BodyHandler.create());
        //make transfer from one account to another
        router.put("/api/accounts/transfer").handler(this::transfer);


        // Create the HTTP server and pass the "accept" method to the request handler.
        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        // Retrieve the port from the configuration,
                        // default to 8080.
                        config().getInteger("http.port", 8080),
                        result -> {
                            if (result.succeeded()) {
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );
    }

    private void getAll(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(accounts.values()));
    }

    private void transfer(RoutingContext routingContext) {
        JsonObject json = routingContext.getBodyAsJson();
        if (json == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final String accountFromEmail = json.getString("emailFrom");
            final String accountToEmail = json.getString("emailTo");

            if (accountFromEmail != null && accountToEmail !=null &&
                    accounts.containsKey(accountFromEmail) && accounts.containsKey(accountToEmail)) {
                Account accountFrom = accounts.get(accountFromEmail);
                Account accountTo = accounts.get(accountToEmail);
                Double accountFromBalance = accountFrom.getBalance();
                Double accountToBalance = accountTo.getBalance();
                Double amount = Double.valueOf(json.getString("amount"));
                if (accountFromBalance >= amount) {
                    try {
                        accountFrom.transfer(-1 * amount);
                        accountTo.transfer(amount);
                    } catch (Exception e) {
                        //rollback in case if database failure occurs (ideally should be wrapped in db transaction)
                        accountFrom.setBalance(accountFromBalance);
                        accountTo.setBalance(accountToBalance);
                    }
                }
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(accountFrom));
            } else {
                routingContext.response().setStatusCode(404).end();
            }
        }
    }

}