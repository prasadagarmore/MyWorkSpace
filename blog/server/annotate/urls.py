__author__ = 'prasad_agarmore'

from django.conf.urls import url

from . import views

urlpatterns = [
    url(r'^save$', views.save_blog, name='save'),
    url(r'^read$', views.read_blogs, name='readall'),
    url(r'^read/(?P<doc_id>[0-9])$', views.read_blog, name='read'),
    url(r'^delete/(?P<doc_id>[0-9])$', views.delete_blog, name='delete'),
]
