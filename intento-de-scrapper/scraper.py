import asyncio
import aiohttp
import pickle
import csv
from bs4 import BeautifulSoup
import re
import argparse

def parse_arguments():
  


def extract_data(raw_html):
  """
  Esta wea devuelve un diccionario y una lista. El diccionario tiene las weas
  que vamos a guardar del OP y la lista contiene diccionarios con la info
  que vamos a guardar en cada comentario hijo de la publicacion
  """
  soup = BeautifulSoup(re.sub(r'>\s+<', '><', raw_html), features='html5lib')


  # para el OP
  raices = soup.find_all('div', class_='raiz')
  roots = []
  for raiz in raices:
    temp = {}
    temp['id'] = raiz.attrs['id'].split('_')[1]
    temp['titulo'] = raiz.h1.getText(strip=True)
    temp['autor'] = raiz.find('a', class_='usuario').getText(strip=True)
    temp['fecha'] = raiz.find('li', class_='fecha').getText(strip=True)
    temp['tema'] = raiz.find('li', class_='tema').a.getText(strip=True)
    # para sacar el texto de un comentario hay que eliminar la lista
    # de botones que tiene al final, como responder, padre, etc.
    comentario = raiz.find('div', class_='texto')
    # cuidado que esto modifica la sopa, el ul se borra definitivamente
    comentario.ul.decompose()
    text = comentario.getText(strip=True)
    temp['mensaje'] = text if len(text) > 0 else 'NO_TEXT'
    roots.append(temp)

  hijos = soup.find_all('div', class_='hijo')
  childs = []
  for hijo in hijos:
    temp = {}
    temp['id'] = hijo.attrs['id'].split('_')[1]
    temp['id_th'] = hijo.attrs['class'][1][1:]
    temp['id_p'] = hijo.parent.attrs['id'].split('_')[1]
    temp['autor'] = hijo.find('a', class_='usuario').getText(strip=True)
    temp['fecha'] = hijo.find('em').getText(strip=True)

    # mismos comentarios que arriba
    comentario = hijo.find('div', class_='texto')
    comentario.ul.decompose()
    text = comentario.getText(strip=True)
    temp['mensaje'] = text if len(text) > 0 else 'NO_TEXT'
    childs.append(temp)

  return roots, childs


# async def fetch(session, url):
#     async with session.get(url) as response:
#         return await response.text()


async def download_page(session, url, root_writer, child_writer):
  """
  Esta funcion recibe la sesion (que deberia estar logueada), la url y
  una wea pa escribir en un archivo, baja la pagina y la escribe en el archivo.

  PUM que sorpresa, no me lo esperaba.
  """
  async with session.get(url) as response:
    # por ahora voy a probar solo con example.com y me se donde esta el texto
    roots, childs = extract_data(await response.text())
    for root in roots:
      root_writer.writerow(root)

    for child in childs:
      child_writer.writerow(child)
    

async def download_batch(session, batch, root_writer, child_writer):
  tasks = []
  for url in batch:
    # print(f'\tDescargando url: {url}')
    task = asyncio.ensure_future(
      download_page(session, url, root_writer, child_writer)
    )
    tasks.append(task)
  await asyncio.gather(*tasks)


async def download_all(batches, root_writer, child_writer):
  async with aiohttp.ClientSession() as session:
    # conectar a cuent ade ucursos aqui
    # tengo mis datos escondidos, porque obvio
    with open('user_data.pic', 'rb') as f:
      payload = pickle.load(f)

    # es importante agregarle esto a la wea que se envia pa poder loguearse
    payload['servicio'] = 'ucursos'
    # payload['debug'] = 0

    # esta wea es a logearse con el usuario de cada uno y mantener la sesion
    # abierta pa poder seguir SURFEANDO ucursos
    post_url = 'https://www.u-cursos.cl/upasaporte/adi'

    async with session.post(post_url, data=payload) as resp:
      print('Respuesta login: ', resp.status)
      assert resp.status == 200, 'diablos, deberia ser 200'
    
    for i, batch in enumerate(batches):
      print(f'Descargando batch {i}')
      await download_batch(session, batch, root_writer, child_writer)


if __name__ == '__main__':
  # N es la cantidad de paginas que se quiere descargar (el ultimo offset)
  N = 9521
  # M es la cantidad de requests que se quieren hacer de una
  # WARNING: CUIDADO CON HACER ESTO MUY GRANDE, PUEDE QUEDAR LA CAGADA
  M = 20
  print(f'Cantidad total de requests: {N}')
  print(f'Cantidad de requests a la vez: {M}')
  print(f'Numero de batches: {(N + M - 1) // M}')
  print(f'\nAfirmense cabros...\n')


  # url base, los parentesis son pa puro quede mas bonito el codigo
  base_url = (
    'https://www.u-cursos.cl/ingenieria/2/foro_institucion/'
    '?id_tema=&offset={}'
  )
  # base_url = 'https://example.com/{}'

  # esta wea vuelve un generator pa todas las url que queremos descargar,
  # si fuera un lista normal pesaria como 100kb lo que no es mucho pero
  # igual es sacrilegio
  batches = (
    (
      base_url.format(j)
      for j
      in range(
        i * M,
        (i + 1) * M if (i + 1) * M < N else N
      )
    )
    for i
    in range((N + M - 1) // M)
  )

  # ahora empieza el mambo con I/O
  with open('root_comments.tsv', 'w') as f_root,\
      open('child_comments.tsv', 'w') as f_child:
    root_fields = ['id', 'titulo', 'autor', 'fecha', 'tema', 'mensaje']
    root_writer = csv.DictWriter(
      f_root,
      fieldnames=root_fields,
      delimiter='\t'
    )
    root_writer.writeheader()
    
    child_fields = ['id', 'id_th', 'id_p', 'autor', 'fecha', 'mensaje']
    child_writer = csv.DictWriter(
      f_child,
      fieldnames=child_fields,
      delimiter='\t'
    )
    child_writer.writeheader()
    
    asyncio.get_event_loop().run_until_complete(
      download_all(batches, root_writer, child_writer)
    )
    
