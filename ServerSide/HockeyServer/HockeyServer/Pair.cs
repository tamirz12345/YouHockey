using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HockeyServer
{
    class Pair
    {
        public ClientInfo listener, initiator;

        public Pair(ClientInfo listener, ClientInfo initiator)
        {
            this.listener = listener;
            this.initiator = initiator; 
        }
    }
}
