package com.codejam.amadeha.main.inicio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codejam.amadeha.R;
import com.codejam.amadeha.main.contenido.MenuContenidoTemaCinco;
import com.codejam.amadeha.main.contenido.MenuContenidoTemaCuatro;
import com.codejam.amadeha.main.contenido.MenuContenidoTemaDos;
import com.codejam.amadeha.main.contenido.MenuContenidoTemaTres;
import com.codejam.amadeha.main.contenido.MenuContenidoTemaUno;
import com.codejam.amadeha.main.slider.ImageModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInicio.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentInicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInicio extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;
    private Context context;
    private ArrayList<ImageModel> imageModelArrayList;


    private ImageView imageView0;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;

    private TextView conjuntos, funcion, ecuaciones, matrices, estadistica;

    private int[] myImageListInicio = new int[]{R.drawable.slider1, R.drawable.slider2,
            R.drawable.slider3, R.drawable.slider4
            , R.drawable.slider1, R.drawable.slider2};

    private OnFragmentInteractionListener mListener;

    public FragmentInicio() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentInicio newInstance(String param1, String param2) {
        FragmentInicio fragment = new FragmentInicio();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment_inicio, container, false);

        imageView0 = (ImageView) v.findViewById(R.id._game0);
        imageView1 = (ImageView) v.findViewById(R.id._game1);
        imageView2 = (ImageView) v.findViewById(R.id._game2);
        imageView3 = (ImageView) v.findViewById(R.id._game3);
        imageView4 = (ImageView) v.findViewById(R.id._game4);

        conjuntos = (TextView) v.findViewById(R.id.teoriaConjunto);
        funcion = (TextView) v.findViewById(R.id.teoriaFuncion);
        ecuaciones = (TextView) v.findViewById(R.id.teoriaEcuaciones);
        matrices = (TextView) v.findViewById(R.id.teoriaMatrices);
        estadistica = (TextView) v.findViewById(R.id.teoriaEstadistica);

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
