using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using EccStatApiTest.EccStatServiceReference;
using System.ServiceModel;
namespace EccStatApiTest
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            eccstatapiClient api = new eccstatapiClient();
            api.Endpoint.Address = new EndpointAddress("http://localhost/ecc/eccservices/eccstatapi");
            string2intMap map = api.getStatisticsStatus("admin", "system", "1");
            foreach(entry el in map)
            {
                Console.WriteLine(el.key + " === " + el.value);
                  
            }
            api.Close();
        }
    }
}
