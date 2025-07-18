package com.example.uteqrevista;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texto = findViewById(R.id.texto);
        obtenerRevistas();
    }

    private void obtenerRevistas() {
        new Thread(() -> {
            try {
                URL url = new URL("https://revistas.uteq.edu.ec/ws/journals.php");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                in.close();

                JSONArray jsonArray = new JSONArray(response.toString());
                StringBuilder resultado = new StringBuilder();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    resultado.append("Revista: ").append(obj.getString("name")).append("\n\n");
                }

                runOnUiThread(() -> texto.setText(resultado.toString()));

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> texto.setText("Error: " + e.getMessage()));
            }
        }).start();
    }
}
