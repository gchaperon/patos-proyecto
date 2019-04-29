import requests
import pickle
from bs4 import BeautifulSoup as bs

# tengo mis datos escondidos, porque obvio
with open('user_data.pic', 'rb') as f:
  payload = pickle.load(f)

payload['servicio'] = 'ucursos'
# payload['debug'] = 0

post_url = 'https://www.u-cursos.cl/upasaporte/adi'

print(payload)

r = requests.post(post_url, data=payload)
print(r.status_code)