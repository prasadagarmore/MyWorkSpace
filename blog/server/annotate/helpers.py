__author__ = 'prasad_agarmore'
from django.http import HttpResponseNotAllowed
# default django decorators are there for method type checking,
# still adding following decorators for json response & custom handling


def api_get(view):
    def wrapper(request, *args, **kwargs):
        if request.method != 'GET':
            return HttpResponseNotAllowed('GET Allowed')
        return view(request, *args, **kwargs)
    return wrapper


def api_post(view):
    def wrapper(request, *args, **kwargs):
        if request.method != 'POST':
            return HttpResponseNotAllowed('POST Allowed')
        return view(request, *args, **kwargs)
    return wrapper
