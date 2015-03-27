﻿using System;
using System.Drawing;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Windows.Forms;
using System.IO;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.ComponentModel;
using System.Collections.Generic;
using System.Net;

namespace HockeyServer
{
    class Server
    {
        private Thread listenThread;
        UdpClient socket;
        Queue<string> clientIps = new Queue<string>(); 
        int port; 

        public Server(int port)
        {
            this.port = port;
            this.socket = new UdpClient(port);
            this.listenThread = new Thread(new ThreadStart(listen));
            this.listenThread.Start();
        }

        public void listen()
        {
            while (true)
            {
                var remoteEP = new IPEndPoint(IPAddress.Any, this.port);
                var data = this.socket.Receive(ref remoteEP);
                Console.Write("receive data from " + remoteEP.ToString());
                //udpServer.Send(new byte[] { 1 }, 1, remoteEP); // reply back
                
            }
        }

    } 
}

