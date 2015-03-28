using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HockeyServer
{
    class ClientInfo
    {
        public string ip;
        public int port;

        public ClientInfo(string ip = "", int port = 0)
        {
            this.ip = ip;
            this.port = port; 
        }

        bool isEqual(ClientInfo clientInfo)
        {
            if (this.ip == clientInfo.ip && this.port == clientInfo.port)
                return true;

            return false; 
        }
    }
}
