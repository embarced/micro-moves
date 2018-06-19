package org.flexess.games.rulesclient;

import org.flexess.games.domain.Move;
import org.flexess.games.service.Position;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;


/**
 * Client gateway for the rules subsystem.
 */
@Component
public class RulesClient {

    private static final String HOSTNAME = "rules";
    private static final int PORT = 8081;

    @HystrixCommand(fallbackMethod = "serviceNotAvailable")
    public ValidateMoveResult validateMove(Position position, Move move)  {

        ValidateMoveResult validateMoveResult = new ValidateMoveResult();

        String sMove = move.getText();
        String fen = position.toString();

        try {

            String fenEncoded = URLEncoder.encode(fen, "UTF-8");

            URL rulesService = new URL("http", HOSTNAME, PORT,
                    "/validateMove?fen=" + fenEncoded + "&move=" + sMove);
            HttpURLConnection con = (HttpURLConnection) rulesService.openConnection();
            con.setRequestMethod("GET");

            con.connect();

            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                String result = readFromInputStream(con.getInputStream());

                JSONObject jsonObject = new JSONObject(result);
                validateMoveResult.setValid(jsonObject.getBoolean("valid"));

                if (jsonObject.has("resultingFen")) {
                    validateMoveResult.setResultingFen(jsonObject.getString("resultingFen"));
                }

                if (jsonObject.has("checkmateAfterMove")) {
                    validateMoveResult.setCheckmateAfterMove(jsonObject.getBoolean("checkmateAfterMove"));
                }

                if (jsonObject.has("stalemateAfterMove")) {
                    validateMoveResult.setStalemateAfterMove(jsonObject.getBoolean("stalemateAfterMove"));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        validateMoveResult.setValidationFailed(false);
        return validateMoveResult;
    }

    public ValidateMoveResult serviceNotAvailable(Position position, Move move) {

        ValidateMoveResult result = new ValidateMoveResult();
        result.setValid(false);
        result.setValidationFailed(true);
        result.setDescription("Rules service not available.");

        return result;
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        StringBuilder responseOutput = new StringBuilder();

        while ((line = br.readLine()) != null) {
            responseOutput.append(line);
        }
        br.close();

        return responseOutput.toString();
    }
}
