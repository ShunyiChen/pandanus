# Pandanus(露兜树)-SITE-API

```java
管理员API微服务
```
```python
# 安装Django
pip install Django==4.1.7
# 安装Django REST Framework
pip install djangorestframework
# 创建一个MVT项目,命名为Services
django-admin startproject Services
# 创建app，命名为api
python manage.py startapp api
# 安装Postgres驱动
pip install psycopg2-binary
# 启动服务
cd \pandanus\admin-api\src\Services
python manage.py makemigrations
python manage.py migrate
python manage.py runserver
```

```python
# Pip list at my end, but not all of them are useful
Package              Version
-------------------- ---------
asgiref              3.6.0
authlete             1.2.1
authlete-django      1.0.1
certifi              2022.12.7
cffi                 1.15.1
charset-normalizer   3.0.1
cryptography         39.0.1
defusedxml           0.7.1
Django               4.1.7
django-allauth       0.52.0
django-keycloak-auth 0.9.5
djangorestframework  3.14.0
dnspython            2.3.0
idna                 3.4
ifaddr               0.2.0
josepy               1.13.0
mozilla-django-oidc  3.0.0
oauth2-provider      0.0
oauthlib             3.2.2
pip                  22.0.4
psycopg2-binary      2.9.5
py-eureka-client     0.11.7
pycparser            2.21
PyJWT                2.6.0
pyOpenSSL            23.0.0
python-consul        1.1.0
python3-openid       3.2.0
pytz                 2022.7.1
requests             2.28.2
requests-oauthlib    1.3.1
setuptools           58.1.0
six                  1.16.0
sqlparse             0.4.3
tzdata               2022.7
urllib3              1.26.14
```