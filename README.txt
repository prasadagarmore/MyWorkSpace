Get code from: https://github.com/prasadagarmore/MyWorkSpace
Change directory to 'blog'
sudo easy_install pip
sudo pip install virtualenv
virtualenv ./
source bin/activate
pip install django==1.9
python manage.py runserver

Deleting DB file and running migrations will create a fresh DB file. 
