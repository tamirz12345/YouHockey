using System;
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
        PairQueue pairQueue = new PairQueue();
        IPEndPoint ip;
        Socket socket;

        public Server(int port)
        {
            /*this.port = port;
            this.socket = new UdpClient(port);
            this.listenThread = new Thread(new ThreadStart(listen));
            this.listenThread.Start();
             * */
            this.ip = new IPEndPoint(IPAddress.Any, port);
            socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp); 
            this.socket.Bind(this.ip);
            this.listenThread = new Thread(new ThreadStart(listen));
            this.listenThread.Start();
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
                Thread handleThread =  new Thread(() => handleCommand(client));
                handleThread.Start();            
            }
        }

        void handleCommand(Socket client)
        {
            IPEndPoint clientep = (IPEndPoint)client.RemoteEndPoint;
            byte[] data = new byte[1024];
            int recv = client.Receive(data);
            Message msg = new Message(data);

            string opcode = msg.cutOpcode();
            Console.WriteLine("Message: '" + msg.message + "' Received from: " + clientep.ToString());
            List<string> parameters = msg.cutParameters();

            if (opcode == "660")
            {
                this.pairQueue.insertClient(new ClientInfo(clientep.ToString(), 0));
                data = Encoding.ASCII.GetBytes("300-\n");
                client.Send(data, data.Length, SocketFlags.None);
            }

            if (opcode == "405")
            {
                this.pairQueue.deleteClient((new ClientInfo(clientep.ToString())));
            }
        }
    } 
}

