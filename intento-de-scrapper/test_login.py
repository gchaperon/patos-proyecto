import requests
import pickle
from bs4 import BeautifulSoup

# tengo mis datos escondidos, porque obvio
with open('user_data.pic', 'rb') as f:
  payload = pickle.load(f)

# es importante agregarle esto a la wea que se envia pa poder loguearse
payload['servicio'] = 'ucursos'
# payload['debug'] = 0

# esta wea es a logearse con el usuario de cada uno y mantener la sesion
# abierta pa poder seguir SURFEANDO ucursos
post_url = 'https://www.u-cursos.cl/upasaporte/adi'
print(payload)
session = requests.Session()
response = session.post(post_url, data=payload)
print(response.status_code)


# ahora es pa empezar a traer comentarios
first_page = 'https://www.u-cursos.cl/ingenieria/2/foro_institucion/'
response = session.get(first_page)

# esto es pa parsear el html y poder buscar weas
soup = BeautifulSoup(response.text)
comment = soup.find('div', {'id': 'mensaje_2205098'})

print(comment) 
