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
    private LinearLayout messagesContainer;
    private ArrayList<String> mensajesEnviados = new ArrayList<>();

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

        messagesContainer = findViewById(R.id.ContenedorMsg1);

        if (savedInstanceState != null) {
            mensajesEnviados = savedInstanceState.getStringArrayList("mensajesEnviados");
            for (String mensaje : mensajesEnviados) {
                addMessage(mensaje, "enviado");
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
            params.gravity = android.view.Gravity.END;
            messageView.setLayoutParams(params);
        } else {
            messageView.setBackgroundColor(0xFFB3E5FC); // Color azul claro para mensajes recibidos
            messageView.setText("Otros: " + message);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = android.view.Gravity.START;
            messageView.setLayoutParams(params);
        }

        messagesContainer.addView(messageView); // Añade el mensaje al contenedor visual

        if (tipo.equals("enviado")) {
            mensajesEnviados.add(message); // Agrega el mensaje a la lista de enviados
        }
    }

    // Método llamado al hacer clic en el botón de enviar
    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity2.class);  // Crea un Intent para lanzar MainActivity2
        EditText et = findViewById(R.id.MsgEnviar1);  // Obtiene el campo de texto donde el usuario escribe el mensaje
        String mensaje = et.getText().toString();  // Convierte el texto a String

        // Si el mensaje no está vacío, procede a enviarlo
        if (!mensaje.isEmpty()) {
            addMessage(mensaje, "enviado"); // Añade el mensaje a la pantalla como "enviado"
            et.setText(""); // Limpia el campo de texto para que el usuario pueda escribir otro mensaje
            intent.putExtra("TextoMensaje", mensaje); // Añade el mensaje al Intent para pasarlo a la siguiente actividad
            Log.i(TAG, "Enviando Mensaje"); // Log para indicar que el mensaje está siendo enviado
            startActivityForResult(intent, 1); // Inicia MainActivity2 esperando un resultado de vuelta
        }
    }

    // Método para gestionar el resultado cuando se vuelve de otra actividad
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verifica que el resultado venga de la actividad correcta y que haya sido exitoso
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String respuesta = data.getStringExtra("Respuesta");  // Obtiene la respuesta enviada desde MainActivity2
            if (respuesta != null) {
                addMessage(respuesta, "recibido"); // Añade la respuesta como un mensaje "recibido"
            }
        }
    }

    // Método para guardar el estado de la actividad (mensajes enviados) cuando se destruye temporalmente (rotación de pantalla, por ejemplo)
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("mensajesEnviados", mensajesEnviados); // Guarda el historial de mensajes enviados en el Bundle
    }
}
