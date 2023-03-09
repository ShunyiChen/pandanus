from django.urls import path,include
from . import views

urlpatterns = [
    path('', views.ApiOverviewView.as_view(), name='home'),
    path('create/', views.AddItemsView.as_view(), name='add-items'),
    path('all/', views.ViewItemsView.as_view(), name='view_items'),
    path('update/<int:pk>/', views.UpdateItemsView.as_view(), name='update-items'),
    path('item/<int:pk>/delete/', views.DeleteItemsView.as_view(), name='delete-items'),
]