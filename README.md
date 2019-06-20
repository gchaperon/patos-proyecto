# patos-proyecto

## ideas de que chucha hacer

### Serio
1. DONE! pares de personas que mas han discutido (como definimos discutir ??)
    * Por ahora una discusion sera: A comenta -> B responde a A -> A responde a B
2. DONE! buscar top comenters
    * DONE! (por año)
    * DONE! ver los del curso
    * DONE! personas que han comentado una sola vez en el foro, cuantas?
<!-- 3. periodos de mayor actividad, con distintas granularidades (hora, dias, en un año) -->
4. DONE!conteo de palabras, palabras mas usadas, en titulos, cuerpo, etc (chap)
5. DONE! cantidad de personas distintas que han comentado por año (guido)
6. DONE!buscar raices con mas respuestas (lecaro)

### Pal meme
1. DONE! cuantas veces a publicado JULIO SALAS, comparacion pre post comentario rodilleras
2. DONE! numero de comentarios de aceituno antes y despues que le digan lo bacan que es su apellido
3. DONE! numero de votaciones  +1/-1, pre y post remocion de -1 (post julio 2018)

### TODOS
* DONE! poner los distinct
* DONE! hacer que reciba argumentos


# Como cresta correr
spark-submit --class PlusOneMinusOne --master spark://cluster-01:7077 casuario-1.0-SNAPSHOT.jar hdfs://cm:9000/uhadoop2019/casuario/proyecto/root_all_wrepeat_tsdate_fixed.tsv


## Argumentos por programa
### CountWords
0 - stop_words
1 - roots
2 - childs
### DuckOlives
0 - roots 
1 - childs
### JulyRooms
0 - roots 
1 - childs
### MostAnsweredRoots
0 - roots 
1 - childs
### PersonsPerYear
0 - roots 
1 - childs
### PlusOneMinusOne
0 - roots
### TopCommanders
0 - roots 
1 - childs
2 - comrades
### TopCommentersPerTheme
0 - roots 
1 - childs 
### TopFighters
0 - roots
1 - childs