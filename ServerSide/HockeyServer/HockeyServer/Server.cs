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
        private Thread listenThread, matchThread;
        PairQueue pairQueue = new PairQueue();
        IPEndPoint ip;
        Socket socket;

        public Server(int port)
        {
            this.ip = new IPEndPoint(IPAddress.Any, port);
            socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp); 
            this.socket.Bind(this.ip);
            this.listenThread = new Thread(new ThreadStart(listen));
            this.listenThread.Start();

            this.matchThread = new Thread(new ThreadStart(match));
            this.matchThread.Start();
        }

        public void listen()
        {
            while (true)
            {
                socket.Listen(10);
                Console.WriteLine("Listening...");
                Socket client = socket.Accept();
                IPEndPoint clientep = (IPEndPoint)client.RemoteEndPoint;
                Console.WriteLine(clientep.ToString() + " Accepted!");
                Thread handleThread =  new Thread(() => handleCommand(client)); // handle command thread for each client
                handleThread.Start();            
            }
        }

        void handleCommand(Socket client)
        {
            while (true)
            {
                IPEndPoint clientep = (IPEndPoint)client.RemoteEndPoint; 
                byte[] data = new byte[1024];
                int recv = client.Receive(data);
                Message msg = new Message(data);
                Mutex m = new Mutex;
                string opcode = msg.cutOpcode();
                m.WaitOne();
                Console.WriteLine("Message: '" + msg.message + "' Received from: " + clientep.ToString());
                m.ReleaseMutex();
                List<string> parameters = msg.cutParameters();
                

                if (opcode == "660")
                {
                    this.pairQueue.insertClient(new ClientInfo(client, clientep.ToString()));
                    data = Encoding.ASCII.GetBytes("300-\n");
                    client.Send(data, data.Length, SocketFlags.None);
                }

                if (opcode == "405")
                {
                    this.pairQueue.deleteClient((new ClientInfo(null, clientep.ToString())));
                }

                if (opcode == "661")
                {
                    string port = parameters[1];
                    Pair p = this.pairQueue.getSpecifiedPair(new ClientInfo(client, clientep.ToString()));
                    this.pairQueue.addPortToPair(Convert.ToInt32(port), new ClientInfo(client, clientep.ToString()));

                    // sending to the listener:
                    string ipInitiator = p.initiator.ip;
                    string[] words = ipInitiator.Split(':');
                    data = Encoding.ASCII.GetBytes("301-" + words[1] + "-" + port);
                    p.listener.socket.Send(data, data.Length, SocketFlags.None);
                    
                    // sending to the initiator:
                    string ipListener = p.listener.ip;
                    ipListener.Split(':');
                    data = Encoding.ASCII.GetBytes("301-" + words[1] + "-" + port);
                    p.initiator.socket.Send(data, data.Length, SocketFlags.None);
                    
                }
            }
        }

        void match()
        {
            Mutex m = new Mutex();
            while (true)
            {
                Pair pair = this.pairQueue.getFullPair();
                byte[] data = new byte[1024];

                if (pair != null && pair.startedReaching == false)
                {
                    m.WaitOne();
                    data = Encoding.ASCII.GetBytes("303-\n");
                    m.ReleaseMutex();
                    pair.listener.socket.Send(data, data.Length, SocketFlags.None);
                    pair.startedReaching = true; 
                }
            }
        }
    } 
}

