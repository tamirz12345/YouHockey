using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HockeyServer
{
    class Pair
    {
        private string ipInitiator, ipListener;
        private int port;

        int getPort() { return this.port; }
        string getInitiator() { return this.ipInitiator; }
        string getListener() { return this.ipListener; }
        void setPort(int port) { this. port = port; }
        void setInitiator(string initiator) { this.ipInitiator = initiator; }
        void setListener(string listener) { this.ipListener = listener; }
    }
}
