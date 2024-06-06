¡Bienvenido al código del juego Scrabble!

En este fichero encontrarás instrucciones sobre cómo configurar 
el proyecto en Eclipse para su correcto funcionamiento durante ejecución. 

El juego requiere varios parámetros iniciales 
(todos opcionales) para poder dar comienzo a la ejecución.

Estos parámetros deben introducirse (el orden no importa) en la línea de argumentos de Eclipse,
que se puede encontrar en la ruta: Project -> Run Configurations -> (#) -> Arguments.

(#) Si no tienes una configuración ya creada, haz doble clic en "Java Application". 
Después, en caso de que las casillas "Project" y/o "Main class" estén vacías, selecciona 
respectivamente el proyecto "Scrabble" y la clase Main "scrabble.Main".

Nota aclaratoria: la notación <...> que se usa a continuación indica el lugar donde debe 
introducirse el valor del parámetro (sin incluir "<" ni ">"), y dentro se indica de manera
esquemática el valor a introducir.

Los argumentos son los siguientes:

1. Fichero de entrada (-i <ruta_fichero/nombre_fichero>): fichero del que se extrae la entrada que 
   de otro modo sería obtenida a partir de un usuario. En caso de no ser introducido, 
   la entrada de datos en la aplicación se realizará por defecto por consola.

2. Fichero de salida (-o <ruta_fichero/nombre_fichero>): fichero en el que, una vez finalizada 
   la ejecución de una partida, se podrá observar la secuencia de resultados obtenidos durante 
   la partida. En caso de no ser introducido, la salida de datos se realizará por defecto por consola.

3. Modo de visualización del juego (-m <gui/console>): este parámetro permite decidir al usuario
   si se quiere jugar con la interfaz gráfica (gui), o a través de la consola por defecto de Eclipse
   (console). En caso de no ser introducido, la interfaz gráfica será mostrada por defecto.

4. Semilla (-s <entero>): un entero que permite generar partidas con ejecución idéntica, siempre 
   y cuando los parámetros dependientes de la elección del usuario, como el número de jugadores, o la 
   acción que se realiza en sus turnos, no varíen. En caso de no ser introducido, se le asignará un
   valor aleatorio.

5. Ayuda (-a): este parámetro permite obtener una breve descripción de los anteriores parámetros.
   Este parámetro es prioritario sobre el resto, es decir, en caso de ser introducido, solo se
   mostrará el mensaje de ayuda, y la ejecución del programa terminará.

Una vez introducidos los parámetros, solo es necesario hacer clic en el botón "Run" de la esquina 
inferior derecha de la ventana.

OBSERVACIONES:

- Una entrada incorrecta de los parámetros generarán un comportamiento indeseado de la ejecución.
  Exceptuando al parámetro -a, todo el resto deben ir acompañados de su respectivo valor.

- No es posible ejecutar una partida en la que la entrada se realice por consola, y la salida esté
  cableada a un fichero. En caso de intentarlo, la aplicación se ejecutará íntegramente por consola.

- El modo de interfaz gráfica (gui) no permite ni la entrada ni salida por fichero. En caso de ser
  estos parámetros introducidos en modo gui, la entrada y salida serán establecidos a consola.

EJEMPLOS:

- Los argumentos: -i /Users/<nombre_usuario>/Desktop/<nombre_fichero.txt> -m console
  indican que se introduce un fichero para la lectura de la entrada, y la partida se visualizará
  a través de la consola de Eclipse. Al no haber introducido "-o <...>" ni "-s <...>", la salida del
  programa se realiza por consola, y la semilla es generada aleatoriamente.