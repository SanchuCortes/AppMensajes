package com.example.appmensajes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.toString();
    private LinearLayout messagesContainer;  // Contenedor para los mensajes
    private ArrayList<String> mensajesEnviados = new ArrayList<>(); // Historial de mensajes enviados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        messagesContainer = findViewById(R.id.ContenedorMsg1); // Contenedor de mensajes

        // Cargar mensajes enviados al recrear la actividad
        if (savedInstanceState != null) {
            mensajesEnviados = savedInstanceState.getStringArrayList("mensajesEnviados");
            for (String mensaje : mensajesEnviados) {
                addMessage(mensaje, "enviado"); // Muestra mensajes guardados
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

        // Guardar el mensaje enviado
        if (tipo.equals("enviado")) {
            mensajesEnviados.add(message);
        }
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        EditText et = findViewById(R.id.MsgEnviar1);
        String mensaje = et.getText().toString();

        if (!mensaje.isEmpty()) {
            addMessage(mensaje, "enviado"); // Añade el mensaje enviado al historial
            et.setText(""); // Limpia el EditText
            intent.putExtra("TextoMensaje", mensaje); // Envía el mensaje a MainActivity2
            Log.i(TAG, "Enviando Mensaje");
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String respuesta = data.getStringExtra("Respuesta");
            if (respuesta != null) {
                addMessage(respuesta, "recibido"); // Añade la respuesta al historial
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("mensajesEnviados", mensajesEnviados); // Guardar el historial de mensajes
    }
}
