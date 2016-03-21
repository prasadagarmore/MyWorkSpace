from __future__ import unicode_literals

from django.db import models


class Document(models.Model):
    #Document creation date
    date_created = models.DateTimeField(auto_now_add=True, blank=True)
    #Document updation date
    date_updated = models.DateTimeField(auto_now=True, blank=True)
    #blog text
    blog_text = models.CharField(max_length=2000, blank=True)
    #blog title
    blog_title = models.CharField(max_length=200, blank=True)
    #blog document file path
    #blog_file_path = models.CharField(max_length=1000)
    #user id of user who created the document
    #user = models.ForeignKey(User, on_delete=models.CASCADE)


class Annotation(models.Model):
    #many to one annotation relationship with document
    document = models.ForeignKey(Document, on_delete=models.CASCADE)
    #User entered text/data
    user_text = models.CharField(max_length=200, blank=True)
    TEXT = 'TEXT'
    IMAGE = 'IMAGE'
    LINK = 'LINK'
    ANNOTATION_TYPE = (
        (TEXT, 'Text'),
        (IMAGE, 'Image'),
        (LINK, 'Link')
    )

    #Type of annotation
    type = models.CharField(max_length=5, choices=ANNOTATION_TYPE, default=TEXT)
    #start of annotation
    start = models.IntegerField(default=0, null=True)
    #end of annotation
    end = models.IntegerField(default=0, null=True)
    #Hide comment for other users? If user wise blogging enabled
    #enternal_visible = models.BooleanField(default=False)
    #Event which will be used to display the annotation metadata
    #event = models.CharField(max_length=7, choices=EVENT_TYPE, default=ONCLICK)