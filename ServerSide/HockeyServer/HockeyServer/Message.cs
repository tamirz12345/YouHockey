using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HockeyServer
{
    class Message
    {
        string message;

        public Message(byte[] arr)
        {
            this.message = Encoding.UTF8.GetString(arr, 0, arr.Length);
        }

        public Message(string message)
        {
           this.message = message;
        }

        public string cutOpcode() // returns the command number
        {
            return this.message.Substring(0, message.IndexOf('-'));
        }
    }
}
