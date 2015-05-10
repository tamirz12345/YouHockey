using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Sockets;

namespace HockeyServer
{
    class ClientInfo
    {
        public string ip;
        public Socket socket;

        public ClientInfo(Socket socket, string ip = "")
        {
            this.ip = ip;
            this.socket = socket;
        }

        public bool isEqual(ClientInfo clientInfo)
        {
            if (this.ip == clientInfo.ip)
                return true;

            return false;
        }
    }
}
