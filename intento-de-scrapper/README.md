# H1
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

