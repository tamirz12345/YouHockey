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

namespace HockeyServer
{
    class Server
    {
        public TcpListener tcpListener;
        private Thread listenThread;
        private NetworkStream clientStream;

        public Server(string ip, int port)
        {

        }

        private void ListenForClients()
        {
            //this.tcpListener.Start();

            while (true)
            {
                //blocks until a client has connected to the server
               // this.unmatchedClients.Enqueue(this.tcpListener.AcceptTcpClient());

                //create a thread to handle communication
                //with connected client
                //Thread clientThread = new Thread(new ParameterizedThreadStart(HandleClientComm));
                //clientThread.Start(client);
            }
        }


        private void HandleClientComm(object client)
        {
            TcpClient tcpClient = (TcpClient)client;
            this.clientStream = tcpClient.GetStream();

            //this.sendScreenshot = new Thread(new ThreadStart(sendData));
            //this.sendScreenshot.Start();

            byte[] message = new byte[4096];
            int bytesRead;

            while (true)
            {
                bytesRead = 0;

                try
                {
                    //blocks until a client sends a message

                    bytesRead = clientStream.Read(message, 0, 4096);
                }
                catch (Exception)
                {
                    //a socket error has occured
                    break;
                }

                if (bytesRead == 0)
                {
                    //the client has disconnected from the server
                    break;
                }

                //message has successfully been received
                ASCIIEncoding encoder = new ASCIIEncoding();
                //frm2.appendLine("Server recieved: " + encoder.GetString(message, 0, bytesRead));
            }
        }
    }
}

