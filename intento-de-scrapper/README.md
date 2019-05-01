# Como correr la wea
Pa correr la wea, primero instalar requisitos (usen ipython es bonito)

```shell
pip install -r requirements.txt
```

de ahi tienen que guardar un archivito con pickle con su nombre de usuario y contrasenna de ucursos pa poder loguearse (esto se puede hacer en una shell de python en la carpeta del escrip)
```python
import pickle
d = {
  "username": "mi_nombre_de_usuario",
  "password": "mi_contrasenna"
}
pickle.dump(d, open('user_data.pic', 'wb'))
```

despues de eso creo que deberia funcionar el mini escrip pa sacar el mensaje del foro
```shell
python3 test_login.py
```

# Notas sobre formatos de hora.
Aca voy a poner los posibles formatos de hora que he pillado en los mensajes de u-cursos:


Relativos a la fecha actual:
* Hoy, hace "MM" mins
* Hoy, hace "SS" segs
* Hoy, a las "HH:MM" hrs.
* Ayer, a las "HH:MM" hrs.


Absolutos:
* "NOMBRE_DIA" "DD" de "NOMBRE_MES" a las "HH:MM" hrs.
* "DD" de "NOMBRE_MES" a las "HH:MM" hrs.
* (parece que el cambio entre las dos anteriores fue el 24/04/19 xD)
* "DD/MM/YY" a las "HH:MM" hrs.
* (y parece que el cambio de esta a las anteriores fue el 01/01/19)



