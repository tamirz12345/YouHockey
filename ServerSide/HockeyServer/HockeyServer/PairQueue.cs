using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HockeyServer
{
    class PairQueue
    {
        Queue<Pair> pairQueue;

        public PairQueue()
        {
            this.pairQueue = new Queue<Pair>();
        }


        public Boolean isExist(ClientInfo checkClient)
        {
            foreach (Pair pair in this.pairQueue)
            {
                if (pair.listener.ip == checkClient.ip.ToString())
                    return false;

                if (pair.initiator.ip == checkClient.ip.ToString())
                    return false;
            }

            return true; 
        }

        public void insertClient(ClientInfo newClient)
        {
            if (!isExist(newClient))
            {
                int indexAvailable = -1;
                int i = 0; 

                foreach (Pair pair in this.pairQueue)
                {
                    if (pair.isReady == false)
                    {
                        indexAvailable = i; 
                    }
                    i++;
                }

                if (i != -1)
                {
                    this.pairQueue.ElementAt(i).initiator = newClient;
                }
                else
                {
                    this.pairQueue.Enqueue(new Pair(newClient, null));
                }

            }
        }
    }
}
