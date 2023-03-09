#!/usr/bin/env python
"""Django's command-line utility for administrative tasks."""
import os
import sys
import py_eureka_client.eureka_client as eureka_client
import py_eureka_client.netint_utils as netint_utils

def register_to_eureka_server():
    # # you can get the ip only
    # ip = netint_utils.get_first_non_loopback_ip("172.22.112.1/24")
    ip = "192.168.56.1"
    port = 8000
    host = "127.0.0.1"
    eureka_client.init(eureka_server="http://127.0.0.1:8070",
                   app_name="admin-api",
                   instance_ip=ip,
                   instance_host=host,
                   instance_port=port)

def main():
    """Run administrative tasks."""
    os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'Services.settings')
    try:
        from django.core.management import execute_from_command_line
    except ImportError as exc:
        raise ImportError(
            "Couldn't import Django. Are you sure it's installed and "
            "available on your PYTHONPATH environment variable? Did you "
            "forget to activate a virtual environment?"
        ) from exc
    execute_from_command_line(sys.argv)


if __name__ == '__main__':
    register_to_eureka_server()
    main()
