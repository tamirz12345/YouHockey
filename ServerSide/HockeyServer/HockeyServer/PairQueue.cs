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
                if (pair.listener != null   && pair.listener.isEqual(checkClient))
                    return true;

                if (pair.initiator != null && pair.initiator.isEqual(checkClient))
                    return true;
            }

            return false; 
        }

        public void insertClient(ClientInfo newClient)
        {
            if (!isExist(newClient))
            {
                int indexAvailable = -1;
                int i = 0; 

                foreach (Pair pair in this.pairQueue)
                {
                    if (pair.isFull == false)
                    {
                        indexAvailable = i; 
                    }
                    i++;
                }

                if (indexAvailable != -1)
                {
                    this.pairQueue.ElementAt(indexAvailable).initiator = newClient;
                    this.pairQueue.ElementAt(indexAvailable).isFull = true; 
                }
                else
                {
                    this.pairQueue.Enqueue(new Pair(newClient, null));
                }

            }
        }

        public void deleteClient(ClientInfo client)
        {
            int index = 0;

            if (this.isExist(client))
            {
                foreach (Pair pair in this.pairQueue)
                {
                    if (pair.listener.isEqual(client))
                        this.pairQueue.ElementAt(index).listener = null;

                    else if (pair.initiator.isEqual(client))
                        this.pairQueue.ElementAt(index).initiator = null; 

                    index++;
                }
            }
        }

        // returns the first full pair that needs to be handled
        public Pair getFullPair()
        {
            foreach (Pair pair in this.pairQueue)
            {
                if (pair.isFull == true)
                    return pair; 
            }

            return null;
        }
    }
}
