package src.externalServices;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import src.MacroGestioneCondivisioni.SendFileControl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

public class ExternalSharingBoundary {
    private HttpServer server;
    private final SendFileControl control;
    
    // Allineato a 8081 per evitare conflitti e blocchi legati alla porta 8080
    private static final int PORT = 8081; 

    public ExternalSharingBoundary(SendFileControl control) {
        this.control = control;
    }

    public void avviaServer() {
        try {
            // In ascolto su 0.0.0.0 per accettare dispositivi esterni dal Wi-Fi
            server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);
            server.createContext("/share", new RedirectHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("Boundary Server APERTO SUL WI-FI sulla porta " + PORT + "... In attesa di visualizzazioni.");
        } catch (IOException e) {
            System.err.println("Impossibile avviare il server.");
            e.printStackTrace();
        }
    }

    public void fermaServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    private class RedirectHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
        this.inviaRichiestaAccesso(exchange);
    }

    public void inviaRichiestaAccesso(HttpExchange exchange) throws IOException{
            URI requestURI = exchange.getRequestURI();
            String query = requestURI.getQuery();
            String token = null;
            if (query != null && query.contains("token=")) {
                token = query.split("token=")[1].split("&")[0];
            }

            if (token == null || token.trim().isEmpty()) {
                String rispostaErrore = "Richiesta non valida: Token di tracciamento mancante.";
                exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
                exchange.sendResponseHeaders(400, rispostaErrore.getBytes().length);
                exchange.getResponseBody().write(rispostaErrore.getBytes());
                exchange.getResponseBody().close();
                return;
            }

             control.verificaEsistenzaCondivisione(exchange,token);

        }

    }


    public void MandaErrore(Object exchangeOb) throws IOException{
        if(exchangeOb instanceof HttpExchange){
            HttpExchange exchange = (HttpExchange)exchangeOb;
            String rispostaNonTrovato = "Il link di condivisione e' scaduto, non valido o inesistente.";
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(404, rispostaNonTrovato.getBytes().length);
            exchange.getResponseBody().write(rispostaNonTrovato.getBytes());
            exchange.getResponseBody().close();
        }else{
            
            return;

        }
    }

    public void Reindirizza(Object exchangeOb,String url) throws IOException{
        if(exchangeOb instanceof HttpExchange){

            HttpExchange exchange = (HttpExchange)exchangeOb;
            exchange.getResponseHeaders().set("Location", url.trim());
            exchange.sendResponseHeaders(302, -1);
            exchange.getResponseBody().close();

        }else{

            return;

        }


    }

    
}