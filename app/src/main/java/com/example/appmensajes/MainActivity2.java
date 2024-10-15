package com.example.appmensajes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private final String TAG = MainActivity2.class.toString();
    private LinearLayout messagesContainer;  // Contenedor para los mensajes
    private ArrayList<String> mensajesRecibidos = new ArrayList<>(); // Historial de mensajes recibidos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        messagesContainer = findViewById(R.id.ContenedorMsg2); // Contenedor de mensajes

        // Verifica si hay mensajes enviados desde MainActivity
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            String textoMensaje = intent.getStringExtra("TextoMensaje");
            if (textoMensaje != null) {
                addMessage(textoMensaje, "enviado"); // Añade el mensaje enviado
            }
        }

        // Cargar mensajes anteriores al recrear la actividad
        if (savedInstanceState != null) {
            mensajesRecibidos = savedInstanceState.getStringArrayList("mensajesRecibidos");
            if (mensajesRecibidos != null) { // Verifica que no sea nulo
                for (String mensaje : mensajesRecibidos) {
                    addMessage(mensaje, "recibido"); // Muestra mensajes guardados
                }
            }
        }
    }

    private void addMessage(String message, String tipo) {
        TextView messageView = new TextView(this);
        messageView.setText(message);
        messageView.setTextSize(24);
        messageView.setPadding(16, 16, 16, 16); // Espaciado interno

        // Prepara el mensaje para que aparezca alineado según el tipo
        if (tipo.equals("enviado")) {
            messageView.setBackgroundColor(0xFFE1BEE7); // Color morado claro para mensajes enviados
            messageView.setText("Yo: " + message);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = android.view.Gravity.END; // Alinear a la derecha
            messageView.setLayoutParams(params);
        } else {
            messageView.setBackgroundColor(0xFFB3E5FC); // Color azul claro para mensajes recibidos
            messageView.setText("Otros: " + message);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = android.view.Gravity.START; // Alinear a la izquierda
            messageView.setLayoutParams(params);
        }
        messagesContainer.addView(messageView); // Añadir el mensaje al contenedor

        // Guardar el mensaje recibido
        if (tipo.equals("recibido")) {
            mensajesRecibidos.add(message);
        }
    }

    public void respuesta(View view) {
        Intent i_res = new Intent();
        EditText et = findViewById(R.id.MsgEnviar2);
        String respuesta = et.getText().toString();

        if (!respuesta.isEmpty()) { // Verifica que no esté vacío
            addMessage(respuesta, "recibido"); // Añade la respuesta al TextView
            et.setText(""); // Limpia el EditText
            i_res.putExtra("Respuesta", respuesta); // Envía la respuesta a MainActivity
            setResult(RESULT_OK, i_res);
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("mensajesRecibidos", mensajesRecibidos); // Guardar el historial de mensajes
    }
}
