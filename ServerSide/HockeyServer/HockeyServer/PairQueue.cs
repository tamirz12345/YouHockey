using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HockeyServer
{
    class PairQueue
    {
        public Queue<Pair> pairQueue;

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
                if (pair.isFull == true && pair.startedReaching == false)
                    return pair; 
            }

            return null;
        }

        public int getSpecifiedPairIndex(ClientInfo client)
        {
            int i = 0; 
            foreach (Pair pair in this.pairQueue)
            {
                if (pair.listener.isEqual(client))
                    return i; 

                else if (pair.initiator.isEqual(client))
                    return i;

                i++;
            }

            return i;
        }

        public void addPortToPair(int port, ClientInfo client)
        {
            int index = this.getSpecifiedPairIndex(client); 
            this.pairQueue.ElementAt(index).port = port;
        }

        public Pair getSpecifiedPair(ClientInfo client)
        {
            foreach (Pair pair in this.pairQueue)
            {
                if (pair.listener.isEqual(client))
                    return pair;

                else if (pair.initiator.isEqual(client))
                    return pair;
            }

            return null;
        }
    }
}
