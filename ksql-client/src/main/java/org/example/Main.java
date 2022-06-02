package org.example;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ClientOptions;

public class Main {
    public static void main(String[] args) {
        String KSQLDB_SERVER_HOST = "localhost";
        int KSQLDB_SERVER_HOST_PORT = 8088;

        System.out.println("Hello world!");
        ClientOptions options = ClientOptions.create()
                .setHost(KSQLDB_SERVER_HOST)
                .setPort(KSQLDB_SERVER_HOST_PORT);
        Client client = Client.create(options);

        // Send requests with the client by following the other examples

        // Terminate any open connections and close the client

        client.streamQuery("SELECT * FROM MY_STREAM EMIT CHANGES;")
                .thenAccept(streamedQueryResult -> {
                    System.out.println("Query has started. Query ID: " + streamedQueryResult.queryID());

                    RowSubscriber subscriber = new RowSubscriber();
                    streamedQueryResult.subscribe(subscriber);
                }).exceptionally(e -> {
                    System.out.println("Request failed: " + e);
                    return null;
                });
        System.out.println("Hello world!");
        client.close();
    }
}