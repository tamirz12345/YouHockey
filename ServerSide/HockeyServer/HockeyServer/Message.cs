using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HockeyServer
{
    class Message
    {
        public string message;

        public Message(byte[] arr)
        {
            try
            {
                this.message = Encoding.UTF8.GetString(arr, 0, arr.Length);
                this.message.Substring(0, message.LastIndexOf('-') + 1);
            }
            catch(Exception e)
            {
                this.message = null; 
            }
        }

        public Message(string message)
        {
           this.message = message;
        }

        public string cutOpcode() // returns the command number
        {
            try
            {
                return this.message.Substring(0, message.IndexOf('-'));
            }
            catch(Exception e)
            {
                return "";
            }
        }

        public List<string> cutParameters()
        {
            string[] words = this.message.Split('-');
            List<string> parameters = new List<string>();

            foreach (string word in words)
            {
                parameters.Add(word);
            }

            return parameters;
        }

    }
}
