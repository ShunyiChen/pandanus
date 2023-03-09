from django.shortcuts import render
from django.shortcuts import get_object_or_404

# Create your views here.
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import serializers
from rest_framework import status
from .models import Item
from .serializers import ItemSerializer
from django.conf import settings
from django.shortcuts import redirect
from rest_framework import viewsets, views
from . import models, serializers
from rest_framework import status
from django.http.response import JsonResponse
from rest_framework.exceptions import PermissionDenied
from rest_framework import viewsets, views
from rest_framework.exceptions import PermissionDenied, AuthenticationFailed, NotAuthenticated


class ApiOverviewView(views.APIView):
    keycloak_roles = {
        'GET': ['viewer'],
    }

    def get(self, request, format=None):
        api_urls = {
            'all_items': '/',
            'Search by Category': '/?category=category_name',
            'Search by Subcategory': '/?subcategory=category_name',
            'Add': '/create',
            'Update': '/update/pk',
            'Delete': '/item/pk/delete'
        }
        # return JsonResponse({"detail": api_urls}, status=status.HTTP_200_OK)
        # list of token roles
        print(request.roles)

        # Optional: get userinfo (SUB attribute from JWT)
        # print(request.userinfo)
        return Response(api_urls)

class AddItemsView(views.APIView):
    keycloak_roles = {
        'POST': ['viewer'],
    }

    def post(self, request, format=None):
        # list of token roles
        print(request.roles)
        item = ItemSerializer(data=request.data)
        # validating for already existing data
        if Item.objects.filter(**request.data).exists():
            raise serializers.ValidationError('This data already exists')

        if item.is_valid():
            item.save()
            return Response(item.data)
        else:
            return Response(status=status.HTTP_404_NOT_FOUND)

class ViewItemsView(views.APIView):
    keycloak_roles = {
        'GET': ['viewer'],
    }
    def get(self, request, format=None):
        # list of token roles
        print(request.roles)
        # checking for the parameters from the URL
        if request.query_params:
            items = Item.objects.filter(**request.query_params.dict())
        else:
            items = Item.objects.all()

        # if there is something in items else raise error
        if items:
            serializer = ItemSerializer(items, many=True)
            # return Response(serializer.data)
            return JsonResponse({"detail": serializer.data}, status=status.HTTP_200_OK)
        else:
            return Response(status=status.HTTP_404_NOT_FOUND)


class UpdateItemsView(views.APIView):
    keycloak_roles = {
        'POST': ['viewer'],
    }
    def post(self, request, pk, format=None):
        # list of token roles
        print(request.roles)
        item = Item.objects.get(pk=pk)
        data = ItemSerializer(instance=item, data=request.data)

        if data.is_valid():
            data.save()
            return Response(data.data)
        else:
            return Response(status=status.HTTP_404_NOT_FOUND)

class DeleteItemsView(views.APIView):
    keycloak_roles = {
        'DELETE': ['viewer'],
    }
    def delete(self, request, pk, format=None):
        # list of token roles
        print(request.roles)
        item = get_object_or_404(Item, pk=pk)
        item.delete()
        return Response(status=status.HTTP_202_ACCEPTED)

# @api_view(['GET'])
# def ApiOverview(request):
#     keycloak_roles = {
#         'GET': ['viewer'],
#     }
#     api_urls = {
#         'all_items': '/',
#         'Search by Category': '/?category=category_name',
#         'Search by Subcategory': '/?subcategory=category_name',
#         'Add': '/create',
#         'Update': '/update/pk',
#         'Delete': '/item/pk/delete'
#     }
#     # return JsonResponse({"detail": api_urls}, status=status.HTTP_200_OK)
#     # list of token roles
#     print(request.roles)
#
#     # Optional: get userinfo (SUB attribute from JWT)
#     # print(request.userinfo)
#     return Response(api_urls)
#
# @api_view(['POST'])
# def add_items(request):
#     item = ItemSerializer(data=request.data)
#
#     # validating for already existing data
#     if Item.objects.filter(**request.data).exists():
#         raise serializers.ValidationError('This data already exists')
#
#     if item.is_valid():
#         item.save()
#         return Response(item.data)
#     else:
#         return Response(status=status.HTTP_404_NOT_FOUND)
#
#
# @api_view(['GET'])
# def view_items(request):
#
    # # checking for the parameters from the URL
    # if request.query_params:
    #     items = Item.objects.filter(**request.query_params.dict())
    # else:
    #     items = Item.objects.all()
    #
    # # if there is something in items else raise error
    # if items:
    #     serializer = ItemSerializer(items, many=True)
    #     return Response(serializer.data)
    #     # return JsonResponse({"detail": serializer.data}, status=status.HTTP_200_OK)
    # else:
    #     return Response(status=status.HTTP_404_NOT_FOUND)
#
# @api_view(['POST'])
# def update_items(request, pk):
#     item = Item.objects.get(pk=pk)
#     data = ItemSerializer(instance=item, data=request.data)
#
#     if data.is_valid():
#         data.save()
#         return Response(data.data)
#     else:
#         return Response(status=status.HTTP_404_NOT_FOUND)
#
# @api_view(['DELETE'])
# def delete_items(request, pk):
#     item = get_object_or_404(Item, pk=pk)
#     item.delete()
#     return Response(status=status.HTTP_202_ACCEPTED)


class JudgementView(views.APIView):
    """
    Judgement endpoint
    This endpoint has configured keycloak roles only GET method.
    Other HTTP methods will be allowed.
    """
    keycloak_roles = {
        'GET': ['message_read'],
    }

    def get(self, request, format=None):
        """
        Overwrite method
        You can especify your rules inside each method
        using the variable 'request.roles' that means a
        list of roles that came from authenticated token.
        See the following example bellow:
        """
        # list of token roles
        print(request.roles)
        # return super().get(self, request)
        return JsonResponse({"detail": request.roles}, status=status.HTTP_200_OK)