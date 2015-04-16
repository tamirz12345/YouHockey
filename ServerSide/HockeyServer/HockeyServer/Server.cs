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
        Queue<ClientInfo> clientInfo = new Queue<ClientInfo>(); 
        int port;
        IPEndPoint ip;
        Socket socket;

        public Server(int port)
        {
            /*this.port = port;
            this.socket = new UdpClient(port);
            this.listenThread = new Thread(new ThreadStart(listen));
            this.listenThread.Start();
             * */
            this.ip = new IPEndPoint(IPAddress.Any, 9050);
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
                Socket client = socket.Accept();
                IPEndPoint clientep = (IPEndPoint)client.RemoteEndPoint;
                Thread handleThread =  new Thread(() => handleCommand(client));
                handleThread.Start();
                // Console.WriteLine("Connected with {0} at port {1}", clientep.Address, clientep.Port);
                // string welcome = "Welcome to my test server";
                // data = Encoding.ASCII.GetBytes(welcome);
                // client.Send(data, data.Length, SocketFlags.None);
                /*
                byte[] data = new byte[1024];
                int recv = client.Receive(data);

                if (recv != 0)
                {
                    Message msg = new Message(data);
                    handleCommand(msg, ""); 
                }*/

               /* Console.WriteLine(
                         Encoding.ASCII.GetString(data, 0, recv));
                client.Send(data, recv, SocketFlags.None);
                */

                //Message msg = new Message(data);
                //handleCommand(msg, remoteEP.ToString());             
            }
        }

        void handleCommand(Socket client)
        {
            IPEndPoint clientep = (IPEndPoint)client.RemoteEndPoint;
            byte[] data = new byte[1024];
            int recv = client.Receive(data);
            Message msg = new Message(data);
            

            string opcode = msg.cutOpcode();
            List<string> parameters = msg.cutParameters();

            if (opcode == "660")
            {
                bool isExist = false;

                foreach (ClientInfo clientInfo in this.clientInfo)
                {
                    if (clientInfo.ip == clientep.ToString())
                        isExist = true;
                }

                if (!isExist)
                {
                    this.clientInfo.Enqueue(new ClientInfo(clientep.ToString()));
                }
            }
        }
    } 
}

