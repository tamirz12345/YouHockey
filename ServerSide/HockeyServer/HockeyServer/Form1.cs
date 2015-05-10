using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Net;

namespace HockeyServer
{
    public partial class Form1 : Form
    {
        int port = 3000;
        public Form1()
        {
            InitializeComponent();
            this.Visible = false; 
            Server server = new Server(port);
            
            this.Hide(); 
        }

        // returns the ip address of current computer
        private string getIpAddress()
        {
            IPAddress[] localIP = Dns.GetHostAddresses(Dns.GetHostName());
            return Convert.ToString(localIP[localIP.Length - 2]);
        }

        public void appendLine(string s)
        {
            textBox1.Text += s; 
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }
    }
}
