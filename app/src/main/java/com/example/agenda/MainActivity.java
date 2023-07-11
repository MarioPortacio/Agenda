package com.example.agenda;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private List<Tarea> listTarea = new ArrayList<Tarea>();
    private List<Tarea> tarea_docs = new ArrayList<Tarea>();
    //lista tarea_doc1 = new lista();
    //List<String> tarea_doc = new ArrayList<String>();
    ArrayAdapter<Tarea> arrayAdapterTarea;


   Button bo, dese;



    EditText tarea, anio, mes, dia, hora, min, desc;
    TextView txw;
    ListView list_t;
    Spinner spinner1;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Tarea tareaSelec; //variable utilizada para realizar los cambios en tareas creadas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bo = (Button)findViewById(R.id.button2);
        dese = (Button)findViewById(R.id.button3);

        bo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent( MainActivity.this, MainActivity2.class);
                startActivity(i);
            }
        });

        //Toast toast1 = Toast.makeText(this, "prueba", Toast.LENGTH_LONG);
        dese.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                tareaSelec = null;
                limpiar();
                txw.setText("Tarea seleccionada: (Ninguna)");

            }
        });

        txw = findViewById(R.id.textView3);
        tarea = findViewById(R.id.et_tarea);
        anio = findViewById(R.id.et_anio);
        mes = findViewById(R.id.et_mes);
        dia = findViewById(R.id.et_dia);
        hora = findViewById(R.id.et_hora);
        min = findViewById(R.id.et_minuto);
        desc = findViewById(R.id.et_desc);
        spinner1 = findViewById(R.id.spinner);
        //num = findViewById(R.id.et_num);

        list_t = findViewById(R.id.lista_tareas); //se señala la lista desde su id

        iniciaFirebase();

        listarDatos();

        String [] opciones = {"Seleccione","Baja", "Media", "Alta"};
        final int[] posicion1 = new int[1];



        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spinner1.setAdapter(adapter);

        final String[] nombre_s = new String[1];

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
                Object item = parent.getItemAtPosition(pos);
                nombre_s[0] = parent.getItemAtPosition(pos).toString();
                posicion1[0] = pos;
                //System.out.println("este es nombre_s[0]" + nombre_s[0]);
                //System.out.println("este es posicion1[0]" + posicion1[0]);
                //spinnerposition = adapter.getPosition(pos);

            }
            public void onNothingSelected(AdapterView<?> parent){

            }

        });

        list_t.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tareaSelec = (Tarea) parent.getItemAtPosition(position); //se hace un casting del parent(el cual es donde se guardan todas los registros de la bd) a tarea, pasa la posición
                tarea.setText(tareaSelec.getActividad()); // coloca los datos en las cajas de texto
                txw.setText("Tarea seleccionada: " + tareaSelec.getActividad());
                anio.setText(tareaSelec.getAnio());
                mes.setText(tareaSelec.getMes());
                dia.setText(tareaSelec.getDia());
                hora.setText(tareaSelec.getHora());
                min.setText(tareaSelec.getMin());
                desc.setText(tareaSelec.getDesc());
                String opcion1 = "Baja";
                String opcion2 = "Media";
                String opcion3 = "Alta";
                if(opcion1.equals(tareaSelec.getPrio())){
                    spinner1.setSelection(1);
                }else if(opcion2.equals(tareaSelec.getPrio())){
                    spinner1.setSelection(2);
                }else if(opcion3.equals(tareaSelec.getPrio())){
                    spinner1.setSelection(3);
                }
                //spinner1.setSelection(posicion1[0]);
                //tareaSelec.getPrio();
                //num.setText(Integer.toString(tareaSelec.getNum()));

            }
        });
    }


    private void listarDatos() {
        databaseReference.child("Tarea").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listTarea.clear();
                for(DataSnapshot objSnapshot : dataSnapshot.getChildren()){ // en el objSnapshot almacena los datos

                    Tarea t = objSnapshot.getValue(Tarea.class);



                    int aux1 = 1;

                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = openFileInput("id_tareas.txt");
                        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader); //para leer de linea en linea
                        String linea_texto;
                        StringBuilder stringBuilder = new StringBuilder();
                        while((linea_texto = bufferedReader.readLine()) != null){ //recorre linea a linea hasta el null
                            //stringBuilder.append(linea_texto).append("\n");
                            //System.out.println("---------------------------------------------------------");
                           // System.out.println("linea_texto: " +linea_texto+ "tamaño: " + linea_texto.length());
                            //System.out.println("t.getTid(): " +t.getTid()+  "tamaño: "  +t.getTid().length());
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

                                arrayAdapterTarea = new ArrayAdapter<Tarea>(MainActivity.this, android.R.layout.simple_list_item_1, listTarea);
                                list_t.setAdapter(arrayAdapterTarea);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String tarea1 = tarea.getText().toString();
        String anio1 = anio.getText().toString();
        String mes1 = mes.getText().toString();
        String dia1 = dia.getText().toString();
        String hora1 = hora.getText().toString();
        String min1 = min.getText().toString();
        String desc1 = desc.getText().toString();
        String spin1 = spinner1.getSelectedItem().toString(); //obtiene baja, media o alta
        System.out.println("Este es el spinner" + spin1);
        System.out.println("Esta es la desc" + desc1);
        //String num1 = num.getText().toString();

        switch (item.getItemId()){
            case R.id.icon_add:{
                if(tarea1.equals("") || anio1.equals("") || mes1.equals("") || dia1.equals("") || hora1.equals("") || min1.equals("") || desc1.equals("") || spin1.equals("Seleccione")){

                        validacion();

                    break;
                }else{//se crea la tarea
                    if(validacion2()==1){
                        Tarea t = new Tarea();
                        t.setTid(UUID.randomUUID().toString());  //  para generar un id aleatorio para tener un llave unica en la BD
                        t.setActividad(tarea1);
                        t.setAnio(anio1);
                        t.setMes(mes1);
                        t.setDia(dia1);
                        t.setHora(hora1);
                        t.setMin(min1);
                        t.setDesc(desc1);
                        t.setPrio(spin1);
                        //t.setNum(Integer.parseInt(num1));


                        saveFile(t);


                        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                        String texto;
                        texto = readFile();
                        System.out.println("Este es el texto: " +texto);


                        //System.out.println(tarea_docs.toString());
                        String hola;

                        databaseReference.child("Tarea").child(t.getTid()).setValue(t);
                        hola = databaseReference.child("Tarea").child(t.getTid()).getKey();
                        System.out.println("esta es la key: " + hola);
                        Toast.makeText(this, "Agregar", Toast.LENGTH_SHORT).show();
                        limpiar();
                        tareaSelec=null;
                        txw.setText("Tarea seleccionada: (Ninguna)");
                        break;


                    }else{
                        break;
                    }

                }

            }
            case R.id.icon_save:{

                //if(tarea1.equals("") || num1.equals("")){
                if(tarea1.equals("") || anio1.equals("") || mes1.equals("") || dia1.equals("") || hora1.equals("") || min1.equals("") || desc1.equals("") || spin1.equals("Seleccione")){
                    validacion();
                    //break;
                }else {
                    //if(tareaSelec.getTid() == null || tareaSelec.getActividad() == null){



                        Tarea t = new Tarea(); //para realizar los cambios al guardar
                        t = tareaSelec;
                    if(t == null){
                        System.out.println("tareaselec es null");
//----------------------------------------------------------------------------------------
                        /*
                        t.setTid(UUID.randomUUID().toString());  //  para generar un id aleatorio para tener un llave unica en la BD
                        t.setActividad(tarea1);
                        t.setAnio(anio1);
                        t.setMes(mes1);
                        t.setDia(dia1);
                        t.setHora(hora1);
                        t.setMin(min1);
                        //t.setNum(Integer.parseInt(num1));
                        databaseReference.child("Tarea").child(t.getTid()).setValue(t);

                        */
                        Toast.makeText(this, "No está guardada la tarea que se quiere editar", Toast.LENGTH_LONG).show();
                        break;

                        //---------------------------------------------------------------------
                    }else {

                        if(validacion2()==1){

                            t.setTid(tareaSelec.getTid());
                            t.setActividad(tarea.getText().toString().trim());
                            t.setAnio(anio.getText().toString().trim());
                            t.setMes(mes.getText().toString().trim());
                            t.setDia(dia.getText().toString().trim());
                            t.setHora(hora.getText().toString().trim());
                            t.setMin(min.getText().toString().trim());
                            t.setDesc(desc.getText().toString().trim());
                            t.setPrio(spinner1.getSelectedItem().toString());


                            //t.setNum(Integer.parseInt(num.getText().toString().trim().replaceAll(" ", "")));
                            databaseReference.child("Tarea").child(t.getTid()).setValue(t);//realiza los cambios en la bd
                            Toast.makeText(this, "Guardar", Toast.LENGTH_LONG).show();
                            limpiar();
                            tareaSelec=null;
                            txw.setText("Tarea seleccionada: (Ninguna)");
                            break;



                        }else{
                            break;
                        }



                    }

                }

            }
            case R.id.icon_delete:{


                if(tarea1.equals("") || anio1.equals("") || mes1.equals("") || dia1.equals("") || hora1.equals("") || min1.equals("") || desc1.equals("") || spin1.equals("Seleccione")){
                    validacion();
                    break;
                }else {

                    Tarea t = new Tarea();
                    t = tareaSelec;
                    //t.setTid(tareaSelec.getTid());
                    if(t==null){
                        System.out.println("-------T es nulo---------");
                        Toast.makeText(this, "No existe la tarea que se quiere eliminar", Toast.LENGTH_LONG).show();
                    }else {
                        System.out.println("-------T no nulo---------");
                        databaseReference.child("Tarea_d").child(t.getTid()).setValue(t);



                        String texto_guarda = t.getTid().toString();
                        String texto_guarda1 = t.getTid().toString();



                        FileOutputStream fileOutputStream = null;
                        try{
                            texto_guarda = texto_guarda + "\n";
                            fileOutputStream = openFileOutput("id_tareas_d.txt",MODE_APPEND); //MODE PRIVATE solo la aplicacion accede al archivo
                            fileOutputStream.write(texto_guarda.getBytes());
                            System.out.println("Fichero salvado en: " + getFilesDir() + "/" + "id_tareas_d");
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






                        //-----------------------------------------------

                        String texto = ""; //sin null?
                        String nuevo;

                        FileInputStream fileInputStream = null;
                        try {
                            fileInputStream = openFileInput("id_tareas.txt");
                            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader); //para leer de linea en linea
                            String linea_texto;
                            StringBuilder stringBuilder = new StringBuilder();
                            while((linea_texto = bufferedReader.readLine()) != null){ //recorre linea a linea hasta el null
                                //stringBuilder.append(linea_texto).append("\n");

                                if(linea_texto.equals(texto_guarda1) || linea_texto.equals("") || linea_texto == null || linea_texto.equals("\n")){

                                }else{




                                    fileOutputStream = null;
                                    try{
                                        nuevo = linea_texto + "\n";
                                        fileOutputStream = openFileOutput("id_tareas_aux.txt",MODE_APPEND); //MODE PRIVATE solo la aplicacion accede al archivo
                                        fileOutputStream.write(nuevo.getBytes());
                                        System.out.println("Fichero salvado en: " + getFilesDir() + "/" + "id_tareas_aux.txt");
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





                        deleteFile("id_tareas.txt");

                        fileInputStream = null;
                        try {
                            fileInputStream = openFileInput("id_tareas_aux.txt");
                            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader); //para leer de linea en linea
                            String linea_texto;
                            StringBuilder stringBuilder = new StringBuilder();
                            while((linea_texto = bufferedReader.readLine()) != null){ //recorre linea a linea hasta el null
                                //stringBuilder.append(linea_texto).append("\n");
                                //texto = texto + linea_texto + "\n";







                                fileOutputStream = null;
                                try{
                                    texto = linea_texto + "\n";
                                    //texto_guarda = texto_guarda + "\n";
                                    fileOutputStream = openFileOutput("id_tareas.txt",MODE_APPEND); //MODE PRIVATE solo la aplicacion accede al archivo
                                    fileOutputStream.write(texto.getBytes());
                                    System.out.println("Fichero salvado en: " + getFilesDir() + "/" + "id_tareas.txt");
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




                        /*
                        fileOutputStream = null;
                        try{
                            //texto_guarda = texto_guarda + "\n";
                            fileOutputStream = openFileOutput("id_tareas.txt",MODE_APPEND); //MODE PRIVATE solo la aplicacion accede al archivo
                            fileOutputStream.write(texto.getBytes());
                            System.out.println("Fichero salvado en: " + getFilesDir() + "/" + "id_tareas.txt");
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

                        */

                        deleteFile("id_tareas_aux.txt");









                        databaseReference.child("Tarea").child(t.getTid()).removeValue();
                        Toast.makeText(this, "Borrar", Toast.LENGTH_LONG).show();
                        limpiar();
                        tareaSelec = null;
                        txw.setText("Tarea seleccionada: (Ninguna)");
                    }
                    /*
                    Tarea t = new Tarea();
                    t.setTid(tareaSelec.getTid());
                    databaseReference.child("Tarea").child(t.getTid()).removeValue();
                    Toast.makeText(this, "Borrar", Toast.LENGTH_LONG).show();
                    limpiar();

                     */
                }
                break;
            }
            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void limpiar() {//limpiar las cajas de texto
        tarea.setText("");
        anio.setText("");
        mes.setText("");
        dia.setText("");
        hora.setText("");
        min.setText("");
        desc.setText("");
        spinner1.setSelection(0);
        //num.setText("");
    }

    private void saveFile(Tarea t){
        String FILE_NAME = "id_tareas.txt";
        String texto_guarda = t.getTid().toString();
        String contenido = "";

        /*
            try {
                ObjectOutputStream salida=new ObjectOutputStream(new FileOutputStream("media.obj"));
                tarea_doc.add(t);
                System.out.println(tarea_doc.toString());
                salida.writeObject(tarea_doc);
                salida.close();
            }catch (Exception e){

            }


        */

        FileOutputStream fileOutputStream = null;
        try{
            texto_guarda = texto_guarda + "\n";
            fileOutputStream = openFileOutput(FILE_NAME,MODE_APPEND); //MODE PRIVATE solo la aplicacion accede al archivo
            fileOutputStream.write(texto_guarda.getBytes());
            System.out.println("Fichero salvado en: " + getFilesDir() + "/" + FILE_NAME);
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

    private String readFile(){
        Tarea t = new Tarea();
        String FILE_NAME = "id_tareas.txt";
        String texto = "";
        /*
        List<Tarea> tarea_docs = new ArrayList<Tarea>();

        try {
            ObjectInputStream entrada=new ObjectInputStream(new FileInputStream("media.obj"));
            tarea_docs= (List<Tarea>) entrada.readObject();
            entrada.close();
        }catch (Exception e){

        }
        */












        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader); //para leer de linea en linea
            String linea_texto;
            StringBuilder stringBuilder = new StringBuilder();
            while((linea_texto = bufferedReader.readLine()) != null){ //recorre linea a linea hasta el null
                stringBuilder.append(linea_texto).append("\n");
            }
            System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
            System.out.println(stringBuilder);
            texto = String.valueOf(stringBuilder);
        }catch (Exception e){

        }finally {
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                }catch (Exception e){

                }
            }
        }

        //return tarea_docs;
        return texto;

    }





    public static boolean isNumeric(String cadena) {
        boolean resultado;
        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }
        return resultado;
    }

    //esta validación debe salir cada vez que se intente añadir una tarea y ...?
    @RequiresApi(api = Build.VERSION_CODES.O)
    private int validacion2(){ //validar si los ingresos en año, mes, día, hora y min son números y validar si la fecha ingresada es a futuro
        String anio1 = anio.getText().toString();
        String mes1 = mes.getText().toString();
        String dia1 = dia.getText().toString();
        String hora1 = hora.getText().toString();
        String min1 = min.getText().toString();

        int continuar = 0;




        int anioi;
        int mesi;
        int diai;
        int horai;
        int mini;
        if (isNumeric(anio1) && isNumeric(mes1) && isNumeric(dia1) && isNumeric(hora1) && isNumeric(min1)){//mensaje de errror si no son numeros


            //anioi con este_anio

            anioi = Integer.parseInt(anio1);
            mesi = Integer.parseInt(mes1);
            diai = Integer.parseInt(dia1);
            horai = Integer.parseInt(hora1);
            mini = Integer.parseInt(min1);



            //LocalDate current_date = LocalDate.now(); // el hoy
            LocalDateTime current_date = LocalDateTime.now();
            int este_anio = current_date.getYear();
            int este_mes = current_date.getMonthValue();
            int este_dia = current_date.getDayOfMonth();
            int este_hora = current_date.getHour();
            int este_min = current_date.getMinute();

            System.out.println("Fecha: " +este_anio+este_mes+este_dia);
            System.out.println("Hora: " +este_hora+ ";" + este_min);

            //si la fecha es coherente:

            if(mesi >= 1 && mesi <= 12){

            }else{
                mes.setError("Mes no valido (Escriba entre 1-12)");
                return 0;
            }


            if(mesi == 4 || mesi == 6 || mesi == 9 || mesi == 11 ){ //meses con 30

                if(diai >= 1 && diai <= 30){

                }else{
                    dia.setError("Día no valido para este mes y año");
                    continuar = 0;
                    return 0;
                }

            }else if(mesi == 1 || mesi == 3 || mesi == 5 || mesi == 7 || mesi == 8 || mesi == 10 || mesi == 12){//meses con 31
                if(diai >= 1 && diai <= 31){

                }else{
                    dia.setError("Día no valido para este mes y año");
                    continuar = 0;
                    return 0;
                }



            }else if(mesi == 2){
                if(anioi == 2024 || anioi == 2028 || anioi == 2032 || anioi == 2036 || anioi == 2040 || anioi == 2044 || anioi == 2048 || anioi == 2052){ //29 dias
                    if(diai >= 1 && diai <= 29){

                    }else{
                        dia.setError("Día no valido para este mes y año");
                        continuar = 0;
                        return 0;
                    }

                }else{
                    if(diai >= 1 && diai <= 28){

                    }else{
                        dia.setError("Día no valido para este mes y año");
                        continuar = 0;
                        return 0;
                    }

                }
            }


//----------------------------------------------------------------------------------------------------------------




            if(anioi >= este_anio && mesi >= este_mes && diai >= este_dia){// si la fecha es actual o proxima

                if(anioi < este_anio){
                    anio.setError("El año ingresado debe de ser actual o proximo");
                    return 0;
                }

                if(anioi == este_anio){
                    if(mesi < este_mes){
                        mes.setError("El mes ingresado debe de ser actual o proximo");
                        return 0;
                    }else if(mesi == este_mes){//si el mes es igual
                        if(diai< este_dia){
                            dia.setError("El día ingresado debe de ser actual o proximo");
                            return  0;
                        }else{

                            if(diai == este_dia){ // si el dia es igual
                                if(horai >= 0 && horai <= 23){

                                    if(horai == este_hora){
                                        if(mini <= este_min){
                                            min.setError("El minuto ingresado debe de ser proximo");
                                            return 0;
                                        }else{
                                            if(mini >= 0 && mini <= 59){
                                                return 1;
                                            }else{
                                                min.setError("El minuto ingresado no es valido, (Escriba entre 0-59)");
                                                return 0;
                                            }


                                        }

                                    }else if(horai < este_hora){
                                        hora.setError("La hora ingresada debe de ser actual o proxima");
                                        return 0;
                                    }else if(horai > este_hora){

                                            if(mini >= 0 && mini <= 59){
                                                return 1;
                                            }else{
                                                min.setError("El minuto ingresado no es valido, (Escriba entre 0-59)");
                                                return 0;
                                            }



                                    }

                                }else{
                                    hora.setError("Hora ingresada no valida (Ingrese entre 0-23)");
                                    return 0;

                                }



                            }else if(diai < este_dia){ //si el dia es proximo
                                dia.setError("El día ingresado debe de ser proxima");
                                return 0;

                            }else if(diai > este_dia){

                                return 1;
                            }



                        }

                    }else{//si el mes es mayor

                        if(horai >= 0 && horai <= 23){
                            if(mini >= 0 && mini <= 59){
                                return 1;

                            }else{
                                min.setError("Minuto ingresado no valido (Ingrese entre 0-59)");
                                return 0;
                            }


                        }else{
                            hora.setError("Hora ingresada no valida (Ingrese entre 0-23)");
                            return 0;
                        }


                    }


                }else if(anioi > este_anio){ // si el año es mayor

                    if(horai >= 0 && horai <= 23){
                        if(mini >= 0 && mini <= 59){
                            return 1;

                        }else{
                            min.setError("Minuto ingresado no valido (Ingrese entre 0-59)");
                            return 0;
                        }


                    }else{
                        hora.setError("Hora ingresada no valida (Ingrese entre 0-23)");
                        return 0;
                    }





                }





            }else{ //si alguno de la fecha no es futuro

                if(anioi < este_anio){
                    anio.setError("El año ingresado debe de ser actual o proximo");
                    return 0;
                }

                if(anioi == este_anio){
                    if(mesi < este_mes){
                        mes.setError("El mes ingresado debe de ser actual o proximo");
                        return 0;
                    }else if(mesi == este_mes){//si el mes es igual
                        if(diai< este_dia){
                            dia.setError("El día ingresado debe de ser actual o proximo");
                            return  0;
                        }else{

                            if(diai == este_dia){ // si el dia es igual
                                if(horai >= 0 && horai <= 23){

                                    if(horai == este_hora){
                                        if(mini <= este_min){
                                            min.setError("El minuto ingresado debe de ser proximo");
                                            return 0;
                                        }else{
                                            if(mini >= 0 && mini <= 59){
                                                return 1;
                                            }else{
                                                min.setError("El minuto ingresado no es valido, (Escriba entre 0-59)");
                                                return 0;
                                            }


                                        }

                                    }else if(horai < este_hora){
                                        hora.setError("La hora ingresada debe de ser actual o proxima");
                                        return 0;
                                    }else if(horai > este_hora){

                                            if(mini >= 0 && mini <= 59){
                                                return 1;
                                            }else{
                                                min.setError("El minuto ingresado no es valido, (Escriba entre 0-59)");
                                                return 0;
                                            }



                                    }


                                }else{
                                    hora.setError("Hora ingresada no valida (Ingrese entre 0-23)");
                                    return 0;
                                }

                            }else if(diai < este_dia){ //si el dia es proximo
                                dia.setError("El día ingresado debe de ser proxima");
                                return 0;

                            }else if(diai > este_dia){

                                return 1;
                            }



                        }

                    }else{//si el mes es mayor


                        if(horai >= 0 && horai <= 23){
                            if(mini >= 0 && mini <= 59){
                                return 1;

                            }else{
                                min.setError("Minuto ingresado no valido (Ingrese entre 0-59)");
                                return 0;
                            }


                        }else{
                            hora.setError("Hora ingresada no valida (Ingrese entre 0-23)");
                            return 0;
                        }


                    }


                }else if(anioi > este_anio){ // si el año es mayor

                    if(horai >= 0 && horai <= 23){
                        if(mini >= 0 && mini <= 59){
                            return 1;

                        }else{
                            min.setError("Minuto ingresado no valido (Ingrese entre 0-59)");
                            return 0;
                        }


                    }else{
                        hora.setError("Hora ingresada no valida (Ingrese entre 0-23)");
                        return 0;
                    }





                }

            return 0;

            }



        }else{

            if(isNumeric(anio1) == false){
                anio.setError("Solo se permiten números en este campo");
                return 0;
            }else if(isNumeric(mes1) == false){
                mes.setError("Solo se permiten números en este campo");
                return 0;
            }else if(isNumeric(dia1) == false){
                dia.setError("Solo se permiten números en este campo");
                return 0;
            }else if(isNumeric(hora1) == false){
                hora.setError("Solo se permiten números en este campo");
                return 0;
            }else if(isNumeric(min1) == false){
                min.setError("Solo se permiten números en este campo");
                return 0;
            }


            return 0;
        }







        return 0;

    }

    private void validacion() {//validar cuando las cajas de texto esten  vacias
        String tarea1 = tarea.getText().toString();
        String anio1 = anio.getText().toString();
        String mes1 = mes.getText().toString();
        String dia1 = dia.getText().toString();
        String hora1 = hora.getText().toString();
        String min1 = min.getText().toString();
        String desc1 = desc.getText().toString();
        String spin1 = spinner1.getSelectedItem().toString();



        //String num1 = num.getText().toString();

        if(tarea1.equals("")){
            tarea.setError("Requerido");
        }else if(anio1.equals("")){
            anio.setError("Requerido");
        }else if(mes1.equals("")){
            mes.setError("Requerido");
        }else if(dia1.equals("")){
            dia.setError("Requerido");
        }else if(hora1.equals("")){
            hora.setError("Requerido");
        }else if(min1.equals("")){
            min.setError("Requerido");
        }else if(desc1.equals("")){
            desc.setError("Requerido");
        }else if(spinner1.getSelectedItemPosition()==0){
            Toast.makeText(this, "Error: Seleccione la prioridad", Toast.LENGTH_SHORT).show();
        }

    }



}