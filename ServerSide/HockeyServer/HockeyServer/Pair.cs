using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HockeyServer
{
    class Pair
    {
        public ClientInfo listener, initiator;
        public Boolean isFull, startedReaching;
        public int port; 

        public Pair(ClientInfo listener = null, ClientInfo initiator = null)
        {
            this.listener = listener;
            this.initiator = initiator;
            this.startedReaching = false;

            if (this.listener != null && this.initiator != null)
                this.isFull = true;
            else
                this.isFull = false; 
        }
    }
}
