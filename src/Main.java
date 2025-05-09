import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("""
                    **************************************
                    Seja bem-vindo/a ao Conversor de Moeda =]

                    1) Dólar => Peso argentino
                    2) Peso argentino => Dólar
                    3) Dólar => Real brasileiro
                    4) Real brasileiro => Dólar
                    5) Dólar => Peso colombiano
                    6) Peso colombiano => Dólar
                    7) Sair

                    Escolha uma opção válida: 
                    **************************************""");

            int opcao = input.nextInt();

            if (opcao == 7) {
                System.out.println("Programa finalizado.");
                break;
            }

            System.out.println("Digite o valor que deseja converter: ");
            double valor = input.nextDouble();

            String moedaOrigem = "USD";
            String moedaDestino = "ARS";

            switch (opcao) {
                case 1 -> { moedaOrigem = "USD"; moedaDestino = "ARS"; }
                case 2 -> { moedaOrigem = "ARS"; moedaDestino = "USD"; }
                case 3 -> { moedaOrigem = "USD"; moedaDestino = "BRL"; }
                case 4 -> { moedaOrigem = "BRL"; moedaDestino = "USD"; }
                case 5 -> { moedaOrigem = "USD"; moedaDestino = "COP"; }
                case 6 -> { moedaOrigem = "COP"; moedaDestino = "USD"; }
                default -> {
                    System.out.println("Opção inválida.");
                    continue;
                }
            }

            try {
                double taxa = obterTaxaCambio(moedaOrigem, moedaDestino);
                double convertido = valor * taxa;
                System.out.println("Valor convertido: " + convertido + " " + moedaDestino);
            } catch (Exception e) {
                System.out.println("Erro ao converter: " + e.getMessage());
            }
        }

        input.close();
    }

    public static double obterTaxaCambio(String moedaOrigem, String moedaDestino) throws IOException, InterruptedException {
        String urlStr = "https://v6.exchangerate-api.com/v6/1188b60f579e56d1909ac2cd/latest/" + moedaOrigem;


        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlStr))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject json = JsonParser.parseReader(new StringReader(response.body()))
                .getAsJsonObject();

        JsonObject taxas = json.getAsJsonObject("conversion_rates");

        return taxas.get(moedaDestino).getAsDouble();
    }
}
