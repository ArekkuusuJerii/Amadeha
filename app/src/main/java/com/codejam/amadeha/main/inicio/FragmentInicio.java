package com.codejam.amadeha.main.inicio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codejam.amadeha.R;
import com.codejam.amadeha.main.contenido.MenuContenidoTemaCinco;
import com.codejam.amadeha.main.contenido.MenuContenidoTemaCuatro;
import com.codejam.amadeha.main.contenido.MenuContenidoTemaDos;
import com.codejam.amadeha.main.contenido.MenuContenidoTemaTres;
import com.codejam.amadeha.main.contenido.MenuContenidoTemaUno;

public class FragmentInicio extends Fragment {
    private Context context;

    public FragmentInicio() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_inicio, container, false);

        TextView conjuntos = v.findViewById(R.id.teoriaConjunto);
        TextView funcion = v.findViewById(R.id.teoriaFuncion);
        TextView ecuaciones = v.findViewById(R.id.teoriaEcuaciones);
        TextView matrices = v.findViewById(R.id.teoriaMatrices);
        TextView estadistica = v.findViewById(R.id.teoriaEstadistica);

        conjuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), MenuContenidoTemaUno.class);
                context.startActivity(intent);
            }
        });

        funcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), MenuContenidoTemaDos.class);
                context.startActivity(intent);
            }
        });

        ecuaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), MenuContenidoTemaTres.class);
                context.startActivity(intent);
            }
        });

        matrices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), MenuContenidoTemaCuatro.class);
                context.startActivity(intent);
            }
        });

        estadistica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), MenuContenidoTemaCinco.class);
                context.startActivity(intent);
            }
        });

        context = v.getContext();
        return v;

    }
}
