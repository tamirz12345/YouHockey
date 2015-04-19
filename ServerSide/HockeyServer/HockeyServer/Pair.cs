using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HockeyServer
{
    class Pair
    {
        public ClientInfo listener, initiator;
        public Boolean isFull;

        public Pair(ClientInfo listener = null, ClientInfo initiator = null)
        {
            this.listener = listener;
            this.initiator = initiator;

            if (this.listener != null && this.initiator != null)
                this.isFull = true;
            else
                this.isFull = false; 
        }
    }
}
