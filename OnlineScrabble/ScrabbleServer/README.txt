¡Bienvenido al código del juego Scrabble!

En este fichero encontrarás instrucciones sobre cómo configurar 
el proyecto ScrabbleServer en Eclipse para su correcto funcionamiento durante ejecución. 

El juego requiere varios parámetros iniciales 
(todos opcionales) para poder dar comienzo a la ejecución.

Estos parámetros deben introducirse (el orden no importa) en la línea de argumentos de Eclipse,
que se puede encontrar en la ruta: Project -> Run Configurations -> (#) -> Arguments.

(#) Si no tienes una configuración ya creada, haz doble clic en "Java Application". 
Después, si las casillas "Project" y/o "Main class" estén vacías, selecciona 
respectivamente el proyecto "Scrabble" y la clase Main "scrabble.Main".

Nota aclaratoria: la notación <...> que se usa a continuación indica el lugar donde debe 
introducirse el valor del parámetro (sin incluir "<" ni ">"), y dentro se indica de manera
esquemática el valor a introducir.

Los argumentos son los siguientes:

1. Número de jugadores humanos (-hp <número de humanos>): cantidad de jugadores humanos que habrá en la partida (debe ser entre 2 y 4).

2. Número de jugadores automáticos (-ap <número de jugadores automáticos>): cantidad de jugadores automáticos que habrá en 
   la partida (debe ser 1 o 2).

3. Estrategia (-s1 <estrategia>): estrategia del primer jugador automático (facil, media, dificil) (sin tilde).

4. Estrategia (-s2 <estrategia>): estrategia del segundo jugador automático (facil, media, dificil) (sin tilde).

5. Puerto del servidor (-p <puerto>): entero que indica el puerto del servidor que usarán los clientes para conectarse al mismo.

6. Ayuda (-a): este parámetro permite obtener una breve descripción de los anteriores parámetros.
   Este parámetro es prioritario sobre el resto, es decir, en caso de ser introducido, solo se
   mostrará el mensaje de ayuda, y la ejecución del programa terminará.

Una vez introducidos los parámetros, solo es necesario hacer clic en el botón "Run" de la esquina 
inferior derecha de la ventana.

OBSERVACIONES:

- Una entrada incorrecta de los parámetros generarán un comportamiento indeseado de la ejecución.
  Exceptuando al parámetro -a, todo el resto deben ir acompañados de su respectivo valor.

- Para evitar posibles complicaciones, es recomendable lanzar primero el proyecto del servidor
  antes de lanzar los proyectos de los clientes.

EJEMPLOS:

- Los argumentos: -hp 3 -ap 1 -s1 facil -p 9879 
  indican que el servidor que tendrá tres jugadores humanos, uno automático,
  de estrategia fácil, conectándose al puerto 9879.