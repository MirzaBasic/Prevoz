using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ePrevoz.WEB.Models
{
    public class StanicaVM
    {
        public int Id { get; set; }

        public int RednaOdznaka { get; set; }

        public double Lat { get; set; }
        public double Lng { get; set; }
        public String Grad { get; set; }

    
    }
}