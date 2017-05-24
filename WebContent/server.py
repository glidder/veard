#!/usr/bin/python

import BaseHTTPServer, SimpleHTTPServer
import ssl

httpd = BaseHTTPServer.HTTPServer(('192.168.1.9', 4443), SimpleHTTPServer.SimpleHTTPRequestHandler)
httpd.socket = ssl.wrap_socket (httpd.socket, certfile='/home/glidder/Desktop/TODO/Augmented Reality/WebContent/server.pem', server_side=True)
httpd.serve_forever()
