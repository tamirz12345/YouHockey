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
        private Thread listenThread;

        public Server(string ip, int port)
        {

        }
    }

    /*
     * how to use a thread:
     * this.sendScreenshot = new Thread(new ThreadStart(sendData));
     * this.sendScreenshot.Start();
     */
      
}

