# Agenda
Programa que funciona como C.R.U.D de tareas, funcionando con almacenamiento en la nube(Firebase) y con almacenamiento en archivos locales .txt
**Para conocer las funciones de la app leer lo siguiente:
*Las funciones de los botones son las siguientes:
-El botón de arriba a la derecha que es una cruz '+' añade una tarea a la lista, despues de haber rellenado los campos a ingresar
-El botón que está a la derecha de la cruz '+' se utiliza para guardar los cambios si editaste una tarea (las tareas pueden seleccionarse para ser editadas dandole clic a cualquier
item de la lista, luego los campos se llenaran automaticamente con los datos de esa tarea en específico y podras editarlos a gusto, cuando tengas listo los cambios presiona el botón
mencionado para guardar, (arriba hay un mensaje que te indica qué tarea está actualmente seleccionada, pondra el nombre que le pusiste previamente para que indentifiques cual tarea estas
editando))
-El botón al lado del botón de guardar, el botón que tiene la imagen de un contenedor de basura es utilizado para borrar tareas, es necesario seleccionar las tareas para poder elimnarlas,
para seleccionar tareas seguir el mismo procedimiento para seleccionar ya mencionado, luego presionar el botón para borrar(esto borrará las tareas de la lista que se visualiza, pero 
esto también enviará las tareas a otra sección de la app, donde se listaran)
-El botón que dice "Deseleccionar tarea" es utilizado para limpiar los campos e indicarle a la app que ya no se quiere seleccionar esa tarea, cuando no haya tareas seleccionadas el texto
dirá: "Tarea seleccionada: (Ninguna)" y cuando haya tareas seleccionadas el texto dirá: "Tarea seleccionada: ejemplo1" asumiendo que el nombre de la tarea seleccionada es ejemplo1
-El botón que dice "Ir a tareas descartadas" te lleva a otra sección de la app donde se veran listadas las tareas que eliminaste previamente
*En la sección de "Tareas descartadas:
-El botón que dice "Ir a lista de tareas" te lleva devuelta a la sección en la que estabas previamente, donde puedes añadir, editar y eliminar tareas
-El botón que dice "BORRAR" borra definitivamente las tareas, es necesario seleccionar la tarea para borrarla, para seleccionar la tarea seguir los pasos ya mencionados, y presionar el boton
para borrar.

*Utilización del almacenamiento en la nube y el almacenamiento local en archivos .txt
-Con firebase se guardan las tareas y sus datos en un esquema que proporciona el servicio de firebase, todas las tareas de todos los dispositivos se guardan en un mismo lugar, para 
identificar a qué tareas corresponde qué dispositivo, se utilizan los archivos .txt, el dispositivo guarda las id(las id son únicos) de cada tarea en un archivo .txt generado, entonces
cuando se crea una tarea, se crea un id, y ese id se guarda en el dispositivo en el archivo .txt y así con cuantas tareas se desee guardar, estas id se utilizan para que la app y 
firebase lean ese archivo y muestren las tareas correspondientes, es decir, el dispositivo muestra las tareas cuyos id sean iguales en el archivo .txt y los guardados en la nube, naturalmente
cada dispositivo tendrá un archivo .txt propio y por lo tanto solo podrá visualizar las tareas que ese dispositivo cree.
