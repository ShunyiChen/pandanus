# from mozilla_django_oidc.auth import OIDCAuthenticationBackend
# from pprint import pprint
#
#
# class MyAuthenticationBackend(OIDCAuthenticationBackend):
#     def verify_claims(self, claims):
#         pprint(claims)
#         b = super().verify_claims(claims)
#         print("-----------------",b)
#         return True