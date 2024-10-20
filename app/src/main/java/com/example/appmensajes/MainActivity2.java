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
            if (mensajesRecibidos != null) {
                for (String mensaje : mensajesRecibidos) {
                    addMessage(mensaje, "recibido");
                }
            }
        }
    }

    // Método para agregar un mensaje al contenedor visual
    private void addMessage(String message, String tipo) {
        TextView messageView = new TextView(this);  // Crea una nueva vista de texto para mostrar el mensaje
        messageView.setText(message);  // Establece el texto del mensaje
        messageView.setTextSize(24);  // Tamaño de letra del mensaje
        messageView.setPadding(16, 16, 16, 16); // Añade padding interno al mensaje

        // Configura el estilo y alineación del mensaje según el tipo
        if (tipo.equals("enviado")) {
            messageView.setBackgroundColor(0xFFE1BEE7); // Color morado claro para mensajes enviados
            messageView.setText("Yo: " + message);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = android.view.Gravity.END; // Alinea el mensaje a la derecha
            messageView.setLayoutParams(params);
        } else {
            messageView.setBackgroundColor(0xFFB3E5FC); // Color azul claro para mensajes recibidos
            messageView.setText("Otros: " + message);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = android.view.Gravity.START; // Alinea el mensaje a la izquierda
            messageView.setLayoutParams(params);
        }

        messagesContainer.addView(messageView); // Añade el mensaje al contenedor visual

        if (tipo.equals("recibido")) {
            mensajesRecibidos.add(message); // Agrega el mensaje a la lista de recibidos
        }
    }

    // Método llamado cuando se hace clic en el botón de respuesta
    public void respuesta(View view) {
        Intent i_res = new Intent();  // Crea un nuevo Intent para devolver la respuesta
        EditText et = findViewById(R.id.MsgEnviar2);  // Obtiene el campo de texto donde el usuario escribe la respuesta
        String respuesta = et.getText().toString();  // Convierte el texto a String

        // Si la respuesta no está vacía, procede a enviarla
        if (!respuesta.isEmpty()) {
            addMessage(respuesta, "recibido"); // Añade la respuesta al contenedor como mensaje "recibido"
            et.setText(""); // Limpia el campo de texto para que el usuario pueda escribir otra respuesta
            i_res.putExtra("Respuesta", respuesta); // Añade la respuesta al Intent para enviarla a MainActivity
            setResult(RESULT_OK, i_res); // Establece el resultado como exitoso
            finish(); // Cierra la actividad y regresa a MainActivity
        }
    }

    // Método para guardar el estado de la actividad (mensajes recibidos) cuando se destruye temporalmente
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("mensajesRecibidos", mensajesRecibidos); // Guarda el historial de mensajes recibidos en el Bundle
    }
}
