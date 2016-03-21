import json
from django.http import HttpResponse

from annotate.models import Document, Annotation
from annotate.helpers import api_get, api_post


def check_type(field, default):
    if field and field.isdigit():
        field = int(field)
    else:
        field = default

    return field

@api_get
def read_blogs(request):
    start = request.GET.get('start')
    length = request.GET.get('length')
    start = check_type(start, 0)
    length = check_type(length, 5)

    documents = Document.objects.all()[start:start+length]

    result = dict()
    result['start'] = start
    result['length'] = length
    result['total'] = documents.count()
    result['blogs'] = [{'title': doc.blog_title, 'blog_id': doc.id} for doc in documents]  # not returning trimmed text
    return HttpResponse(json.dumps(result))

@api_get
def read_blog(request, doc_id):
    try:
        document = Document.objects.get(id=doc_id)
    except Document.DoesNotExist:
        return HttpResponse('Document with given ID does not exist', status=400)

    result = dict()
    result['blog_text'] = document.blog_text
    result['blog_title'] = document.blog_title
    result['annotations'] = [{'content': comment.user_text, 'start': comment.start, 'end': comment.end,
                              'type': comment.type, 'id': comment.id}
                             for comment in Annotation.objects.filter(document_id=doc_id)]
    return HttpResponse(json.dumps(result))

@api_post
def delete_blog(request, doc_id):
    try:
        Document.objects.get(id=doc_id).delete()
    except Document.DoesNotExist:
        return HttpResponse('Document with given ID does not exist', status=400)

    return HttpResponse('Record deleted successfully')

@api_post
def save_blog(request):
    """
    :param request:
    :return:
    """
    input_data = json.loads(request.body)
    doc_id = input_data.get('doc_id')
    if doc_id:
        try:
            document = Document.objects.get(id=doc_id)
        except Document.DoesNotExist:
            return HttpResponse('Document with given ID does not exist', status=400)
    else:
        document = Document()

    document.blog_text = input_data.get('blog_text')
    document.blog_title = input_data.get('blog_title')

    document.save()

    annotations = input_data.get('annotations')
    current_annotations = document.annotation_set.all()
    error_ids = []
    for annotation in annotations:
        # modified = False - For optimisation if object has changed then only update it.
        # But assuming UI will only send updated objects
        an_id = annotation.get('id')
        if an_id:
            found = False
            for an in current_annotations:
                if an.id == an_id:
                    an_object = an
                    found = True
                    break
            if not found:
                error_ids.append(an_id)
                continue
        else:
            an_object = Annotation()

        an_object.document_id = document.id
        an_object.user_text = annotation.get('content')
        an_object.start = annotation.get('start')
        an_object.end = annotation.get('end')
        an_object.type = annotation.get('type')

        an_object.save()

    return HttpResponse(json.dumps(error_ids))