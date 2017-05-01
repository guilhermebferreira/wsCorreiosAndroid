package guilherme.androidwscorreios;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class CalcPreco extends AppCompatActivity {


    private FloatingActionButton btnDisplay;
    private RadioGroup radioGroupMao;
    private RadioButton radioButtonMao;
    private RadioGroup radioGroupAviso;
    private RadioButton radioButtonAviso;
    private EditText origemText, destinoText;
    private EditText pesoText, alturaText, larguraText, comprimentoText, diametroText;
    private EditText valorText;
    private Spinner servicoSpinner, formatoSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_preco);

        addListenerOnButton();
    }


    public void addListenerOnButton() {


        //private Spinner servicoSpinner, formatoSpinner;

        radioGroupMao = (RadioGroup) findViewById(R.id.sCdMaoPropria);
        radioGroupAviso = (RadioGroup) findViewById(R.id.sCdAvisoRecebimento);

        btnDisplay = (FloatingActionButton) findViewById(R.id.sendButton);

        pesoText = (EditText) findViewById(R.id.nVlPeso);
        larguraText = (EditText) findViewById(R.id.nVlLargura);
        alturaText = (EditText) findViewById(R.id.nVlAltura);
        comprimentoText = (EditText) findViewById(R.id.nVlComprimento);
        diametroText = (EditText) findViewById(R.id.nVlDiametro);
        origemText = (EditText) findViewById(R.id.sCepOrigem);
        destinoText = (EditText) findViewById(R.id.sCepDestino);
        valorText = (EditText) findViewById(R.id.nVlValorDeclarado);

        servicoSpinner = (Spinner) findViewById(R.id.nCdServico);
        formatoSpinner = (Spinner) findViewById(R.id.nCdFormato);



        btnDisplay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String resultado = " teste ";

                String pesoStr = pesoText.getText().toString();
                String alturaStr = alturaText.getText().toString();
                String larguraStr = larguraText.getText().toString();
                String comprimentotr = comprimentoText.getText().toString();
                String diametroStr = diametroText.getText().toString();
                String origemStr = origemText.getText().toString();
                String destinoStr = destinoText.getText().toString();
                String valorStr = valorText.getText().toString();
                String servicoStr = servicoSpinner.getSelectedItem().toString();
                String formatoStr = formatoSpinner.getSelectedItem().toString();
                String maoPropriaStr = "";
                String avisoRecebimentoStr = "";

                // get selected radio button from radioGroup
                int selectedId = radioGroupMao.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                if (selectedId > 0) {
                    radioButtonMao = (RadioButton) findViewById(selectedId);
                    maoPropriaStr = radioButtonMao.getText().toString();
                }

                // get selected radio button from radioGroup
                selectedId = radioGroupAviso.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                if (selectedId > 0) {
                    radioButtonAviso = (RadioButton) findViewById(selectedId);
                    avisoRecebimentoStr = radioButtonAviso.getText().toString();
                }


                Log.d("Send", "resultado" + resultado);

                setWarning("o IMC foi calculado");

                sendNotification("o IMC foi calculado.", resultado);


            }

        });

    }

    private void sendNotification(String s, String resultado) {
        new AlertDialog.Builder(CalcPreco.this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void setWarning(String s) {
    }

    public String  performPostCall(String requestURL,
                                   HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
