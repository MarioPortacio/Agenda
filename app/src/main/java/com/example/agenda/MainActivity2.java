package com.example.agenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.model.Tarea;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity {


    private List<Tarea> listTarea = new ArrayList<Tarea>();
    ArrayAdapter<Tarea> arrayAdapterTarea;

    Button bo, borrar;

    TextView tarea, anio;
    ListView list_t2;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Tarea tareaSelec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bo = (Button)findViewById(R.id.button);
        borrar = (Button)findViewById(R.id.button5);

        bo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent( MainActivity2.this, MainActivity.class);
                startActivity(i);
            }
        });

        String tarea1="hola";
        String anio1="adios";
        String mes1="jja";
        String dia1="pre";
        String hora1="h1";
        String min1="minnn";

        borrar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                Tarea t = new Tarea();

                t = tareaSelec;
                if(t==null){


                }else{
                    t.setTid(tareaSelec.getTid());




                    String texto_guarda1 = t.getTid().toString();

                    String texto = ""; //sin null?
                    String nuevo;

                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = openFileInput("id_tareas_d.txt");
                        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader); //para leer de linea en linea
                        String linea_texto;
                        StringBuilder stringBuilder = new StringBuilder();
                        while((linea_texto = bufferedReader.readLine()) != null){ //recorre linea a linea hasta el null
                            //stringBuilder.append(linea_texto).append("\n");

                            if(linea_texto.equals(texto_guarda1) || linea_texto.equals("") || linea_texto == null || linea_texto.equals("\n")){

                            }else{




                                FileOutputStream fileOutputStream = null;
                                try{
                                    nuevo = linea_texto + "\n";
                                    fileOutputStream = openFileOutput("id_tareas_d_aux.txt",MODE_APPEND); //MODE PRIVATE solo la aplicacion accede al archivo
                                    fileOutputStream.write(nuevo.getBytes());
                                    System.out.println("Fichero salvado en: " + getFilesDir() + "/" + "id_tareas_d_aux.txt");
                                }catch (Exception e){
                                    e.printStackTrace(); //borrar
                                }finally {
                                    if(fileOutputStream != null){
                                        try {
                                            fileOutputStream.close();

                                        }catch (Exception e){
                                            e.printStackTrace(); //borrar
                                        }
                                    }
                                }



                            }//else

















                        }
                        System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
                        System.out.println(stringBuilder);
                        //texto = String.valueOf(stringBuilder);
                    }catch (Exception e){

                    }finally {
                        if(fileInputStream != null){
                            try {
                                fileInputStream.close();
                            }catch (Exception e){

                            }
                        }
                    }




                    // ahora tengo 2 archivos un id_tareas.txt e id_tareas_aux.txt





                    deleteFile("id_tareas_d.txt");

                    fileInputStream = null;
                    try {
                        fileInputStream = openFileInput("id_tareas_d_aux.txt");
                        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader); //para leer de linea en linea
                        String linea_texto;
                        StringBuilder stringBuilder = new StringBuilder();
                        while((linea_texto = bufferedReader.readLine()) != null){ //recorre linea a linea hasta el null
                            //stringBuilder.append(linea_texto).append("\n");
                            //texto = texto + linea_texto + "\n";







                            FileOutputStream fileOutputStream = null;
                            try{
                                texto = linea_texto + "\n";
                                //texto_guarda = texto_guarda + "\n";
                                fileOutputStream = openFileOutput("id_tareas_d.txt",MODE_APPEND); //MODE PRIVATE solo la aplicacion accede al archivo
                                fileOutputStream.write(texto.getBytes());
                                System.out.println("Fichero salvado en: " + getFilesDir() + "/" + "id_tareas_d.txt");
                            }catch (Exception e){
                                e.printStackTrace(); //borrar
                            }finally {
                                if(fileOutputStream != null){
                                    try {
                                        fileOutputStream.close();

                                    }catch (Exception e){
                                        e.printStackTrace(); //borrar
                                    }
                                }
                            }












                        }
                        System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
                        //System.out.println(stringBuilder);
                        //texto = String.valueOf(stringBuilder);
                        System.out.println("Texto que va para el original: "+texto);
                    }catch (Exception e){

                    }finally {
                        if(fileInputStream != null){
                            try {
                                fileInputStream.close();
                            }catch (Exception e){

                            }
                        }
                    }



                    deleteFile("id_tareas_d_aux.txt");








                    databaseReference.child("Tarea_d").child(t.getTid()).removeValue();
                }

                //Toast.makeText(this, "Agregar", Toast.LENGTH_SHORT).show();
            }
        });





        tarea = findViewById(R.id.textView);
        anio = findViewById(R.id.textView2);

        list_t2 = findViewById(R.id.list_2);

        iniciaFirebase();

        listarDatos();


        list_t2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tareaSelec = (Tarea) parent.getItemAtPosition(position); //se hace un casting del parent(el cual es donde se guardan todas los registros de la bd) a tarea, pasa la posición
                tarea.setText(tareaSelec.getActividad()); // coloca los datos en las cajas de texto
                //txw.setText(tareaSelec.getActividad());
                anio.setText(tareaSelec.getAnio());
               // mes.setText(tareaSelec.getMes());
               // dia.setText(tareaSelec.getDia());
                //hora.setText(tareaSelec.getHora());
                //min.setText(tareaSelec.getMin());
                //num.setText(Integer.toString(tareaSelec.getNum()));

            }
        });

    }


    /* 3 cosas por cambiar
    private void listarDatos() {
        databaseReference.child("Tarea_d").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listTarea.clear();
                for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){ // en el objSnapshot almacena los datos
                    Tarea t = objSnapshot.getValue(Tarea.class); //de donde almacena los datos, obtenga el valor, el Tarea.class
                    listTarea.add(t); //a la lista de tareas se le añade la nueva tarea

                    arrayAdapterTarea = new ArrayAdapter<Tarea>(MainActivity2.this, android.R.layout.simple_list_item_1, listTarea);
                    list_t2.setAdapter(arrayAdapterTarea);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    */


     // 3 cosas por cambiar
     private void listarDatos() {
         databaseReference.child("Tarea_d").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 listTarea.clear();
                 for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){ // en el objSnapshot almacena los datos

                     Tarea t = objSnapshot.getValue(Tarea.class);
                     int aux1 = 1;

                     FileInputStream fileInputStream = null;
                     try {
                         fileInputStream = openFileInput("id_tareas_d.txt");
                         InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                         BufferedReader bufferedReader = new BufferedReader(inputStreamReader); //para leer de linea en linea
                         String linea_texto;
                         StringBuilder stringBuilder = new StringBuilder();
                         while((linea_texto = bufferedReader.readLine()) != null){ //recorre linea a linea hasta el null
                             //stringBuilder.append(linea_texto).append("\n");
                             System.out.println("---------------------------------------------------------");
                             System.out.println("linea_texto: " +linea_texto+ "tamaño: " + linea_texto.length());
                             System.out.println("t.getTid(): " +t.getTid()+  "tamaño: "  +t.getTid().length());
                             if(linea_texto.equals(t.getTid())){
                                 aux1 = 1;
                                 System.out.println("AUX: " +aux1);
                             }else{
                                 aux1 = 0;
                                 System.out.println("AUX: " +aux1);
                             }


                             if(aux1 == 1){
                                 //Tarea t = objSnapshot.getValue(Tarea.class); //de donde almacena los datos, obtenga el valor, el Tarea.class
                                 listTarea.add(t); //a la lista de tareas se le añade la nueva tarea

                                 arrayAdapterTarea = new ArrayAdapter<Tarea>(MainActivity2.this, android.R.layout.simple_list_item_1, listTarea);
                                 list_t2.setAdapter(arrayAdapterTarea);
                             }

                             aux1 = 0;



                         }

                         //texto = String.valueOf(stringBuilder);
                     }catch (Exception e){

                     }finally {
                         if(fileInputStream != null){
                             try {
                                 fileInputStream.close();
                             }catch (Exception e){

                             }
                         }
                     }




                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
     }












    private void iniciaFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);   //habilita la persistencia de datos, evita que los datos se pierdan al realizar acciones si conexiión a internet, detecta los ultmos cambios realizados y los coloca en la bd si no estan ya
        databaseReference = firebaseDatabase.getReference();
    }


}