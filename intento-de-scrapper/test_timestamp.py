import datetime as dt
import re

# no encontre los meses y no me dio paja escribirlos asi que estos son los messesss
mes = {
    'Enero' : 1,
    'Febrero' : 2,
    'Marzo' : 3,
    'Abril' : 4,
    'Mayo' : 5,
    'Junio' : 6,
    'Julio' : 7,
    'Agosto' : 8,
    'Septiembre' : 9,
    'Octubre' : 10,
    'Noviembre' : 11;
    'Diciembre' : 12
}


def relative_date_2_timestamp(date_query: dt.datetime, typed_date: str):
    """
    Aqui entran las fechas relativas a la fecha de consulta
    date_query: datetime de la fecha de query
    typed_date: una fecha que es relativa a la de la query
    """
    matchDate = re.match(r'.*, hace (.*) (.*)', typed_date)
    if(matchDate):
        if(matchDate.group(2) == 'mins'):
            date_timestamp = date_query - dt.timedelta(minutes = int(matchDate.group(1)))
        elif(matchDate.group(2) == 'segs'):
            date_timestamp = date_query - dt.timedelta(seconds = int(matchDate.group(1)))
    else:
        matchDate = re.match(r'(.*), a las (.*):(.*) hrs.',typed_date)
        if(matchDate.group(1) == 'Hoy'):
            date_timestamp = dt.datetime(
                date_query.year,
                date_query.month,
                date_query.day,
                int(matchDate.group(2)),
                int(matchDate.group(3))
                )
        elif(matchDate.group(1) == 'Ayer'): #restar un dia
            date_timestamp = dt.datetime(
                (date_query - dt.timedelta(1,0,0,0,0)).year,
                (date_query - dt.timedelta(1,0,0,0,0)).month,
                (date_query - dt.timedelta(1,0,0,0,0)).day,
                int(matchDate.group(2)),
                int(matchDate.group(3))
                )
    return date_timestamp.timestamp()


def date_2_timestamp(date_query: dt.datetime, typed_date: str):
    """
    el timestamp nomasss
    date_query: fecha de la query, para sacar el año
    typed_date: fecha pa timestampear
    """
    matchDate = re.match(r'(.*) a las (.*):(.*) hrs.', typed_date)
    if(matchDate):
        matchThisYear = re.match(r'(.*) de (.*)', matchDate.group(1))
        matchOtherYear = re.match(r'(.*)/(.*)/(.*)', matchDate.group(1))
        if(matchThisYear):
            d = int(matchThisYear.group(1))
            m = mes[matchThisYear.group(2)]
            y = date_query.year
        elif(matchOtherYear):
            d = int(matchOtherYear.group(1))
            m = int(matchOtherYear.group(2))
            y = int(matchOtherYear.group(3))
        try:
            date_timestamp = dt.datetime(
                year= y,
                month= m,
                day= d,
                hours=int(matchDate.group(2)),
                minutes=int(matchDate.group(3)))

            return date_timestamp.timestamp()
        except:
            return 'uwu'


def str_2_timestamp(date_query: str, typed_date: str):
    """
    entrega el timestamp (float) del post o comentario
    date_query: el str del timestamp de consulta
    typed_date: la fecha que tiene escrita el postt en uwursos
    """
    matchDate = re.match(r'(.*), .*', typed_date)
    date_query_dt = dt.datetime.fromtimestamp(float(date_query))
    if(matchDate):
        return relative_date_2_timestamp(date_query_dt, typed_date)
    else:
        return date_2_timestamp(date_query_dt, typed_date)
        
tiempos_jejemplo = [
    "Hoy, hace 5 mins", 
    "Hoy, hace 13 segs",
    "Hoy, a las 00:02 hrs.",
    "Ayer, a las 01:13 hrs.",
    "Martes 7 de Mayo a las 20:43 hrs.",
    "2 de Mayo a las 13:49 hrs.",
    "16/12/05 a las 14:26 hrs."
    ]