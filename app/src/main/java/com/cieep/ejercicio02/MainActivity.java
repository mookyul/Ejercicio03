package com.cieep.ejercicio02;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cieep.ejercicio02.modelos.Nota;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private final int EDIT_NOTA = 2;
    private final int AGREGAR_NOTA = 1;
    // Vista
    private LinearLayout contenedor;
    private Button btnAgregar;

    // Modelo Datos
    private ArrayList<Nota> listaNotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contenedor = findViewById(R.id.contenedorMain);
        btnAgregar = findViewById(R.id.btnAgregarMain);
        listaNotas = new ArrayList<>();

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AgregarActivity.class);
                startActivityForResult(intent, AGREGAR_NOTA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AGREGAR_NOTA && resultCode == RESULT_OK) {
            if (data != null){
                final Nota nota = data.getExtras().getParcelable("NOTA");
                listaNotas.add(nota);
                final int posicion = listaNotas.size() - 1;


                View filanota = LayoutInflater.from(this).inflate(R.layout.fila_nota,null);
                // 1. Textview para el titulo
                TextView txtTitulo = filanota.findViewById(R.id.txtTituloFilaNota);
                txtTitulo.setText(nota.getTitulo());


                txtTitulo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("NOTA", nota);
                        bundle.putInt("POS", posicion);
                        Intent intent = new Intent(MainActivity.this, EditNotaActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, EDIT_NOTA);
                    }
                });
                // 2. Button para eliminar
                ImageButton btnEliminar = filanota.findViewById(R.id.btnEliminarFilaNota);

                btnEliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listaNotas.remove(posicion);
                        repintaNotas();
                    }
                });

                // 4. Insertar el Linear en el contenedor
                contenedor.addView(filanota);

            }
        }
        if (requestCode == EDIT_NOTA && resultCode == RESULT_OK) {
            if (data != null) {
                Nota nota = data.getExtras().getParcelable("NOTA");
                int posicion = data.getExtras().getInt("POS");
                listaNotas.get(posicion).setTitulo(nota.getTitulo());
                listaNotas.get(posicion).setContenido(nota.getContenido());
                repintaNotas();
            }
        }
    }

    private void repintaNotas() {
        contenedor.removeAllViews();
        for (int i = 0; i < listaNotas.size(); i++) {

            final Nota nota = listaNotas.get(i);
            final int posicion = i;

            // 1. Textview para el titulo
            TextView txtTitulo = new TextView(this);
            txtTitulo.setText(nota.getTitulo());
            txtTitulo.setTextSize(24);
            txtTitulo.setTextColor(Color.BLUE);

            LinearLayout.LayoutParams paramsTXT = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3);
            paramsTXT.setMargins(15, 15,15, 15);
            paramsTXT.gravity = Gravity.CENTER;
            txtTitulo.setLayoutParams(paramsTXT);
            txtTitulo.setGravity(Gravity.CENTER);

            txtTitulo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("NOTA", nota);
                    bundle.putInt("POS", posicion);
                    Intent intent = new Intent(MainActivity.this, EditNotaActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, EDIT_NOTA);
                }
            });
            // 2. Button para eliminar
            Button btnEliminar = new Button(this);
            btnEliminar.setText("Eliminar");
            btnEliminar.setBackgroundColor(Color.RED);

            LinearLayout.LayoutParams paramsBTN = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            paramsBTN.setMargins(15, 15,15, 15);
            btnEliminar.setLayoutParams(paramsBTN);

            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listaNotas.remove(posicion);
                    repintaNotas();
                }
            });
            // 3. Linear Horizontal para posicionar los elementos
            LinearLayout contenedorNota = new LinearLayout(this);
            // 3.1 Insertar el txt y Button en el Horizontal
            contenedorNota.addView(txtTitulo);
            contenedorNota.addView(btnEliminar);
            // 4. Insertar el Linear en el contenedor
            contenedor.addView(contenedorNota);


        }
    }
}