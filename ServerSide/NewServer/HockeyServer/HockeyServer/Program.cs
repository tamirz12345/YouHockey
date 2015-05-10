using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HockeyServer
{
    class Program
    {
        static void Main(string[] args)
        {
            int port = 3000;
            Server server = new Server(port);
        }
        
    }
}
