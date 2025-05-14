import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.util.*;

public class ConversorMoedasAPI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String apiKey = "71290ab9216e932ae5964e3a"; // Sua API Key
        String apiUrl = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/";

        List<String> moedasPermitidas = Arrays.asList("BRL", "USD", "GBP", "EUR", "ARS");

        System.out.println("Digite a moeda de origem (BRL, USD, GBP, EUR, ARS): ");
        String origem = scanner.next().toUpperCase();
        System.out.println("Digite a moeda de destino (BRL, USD, GBP, EUR, ARS): ");
        String destino = scanner.next().toUpperCase();

        if (!moedasPermitidas.contains(origem) || !moedasPermitidas.contains(destino)) {
            System.out.println("Moeda inválida. Utilize apenas BRL, USD, GBP, EUR ou ARS.");
            scanner.close();
            return;
        }

        System.out.println("Digite o valor a ser convertido: ");
        double valor = scanner.nextDouble();

        try {
            URL url = new URL(apiUrl + origem);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder resposta = new StringBuilder();
            String linha;

            while ((linha = reader.readLine()) != null) {
                resposta.append(linha);
            }
            reader.close();
            connection.disconnect();

            JSONObject json = new JSONObject(resposta.toString());

            if (!json.getString("result").equals("success")) {
                System.out.println("Erro na resposta da API. Verifique a moeda de origem.");
                scanner.close();
                return;
            }

            double taxa = json.getJSONObject("conversion_rates").getDouble(destino);
            double valorConvertido = valor * taxa;

            System.out.printf("Valor convertido: %.2f %s%n", valorConvertido, destino);

        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao conectar ou processar os dados da API:");
            System.out.println(e.getMessage());
        }

        scanner.close();
    }
}
