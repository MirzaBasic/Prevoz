using ePrevoz.DATA.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ePrevoz.WEB.Models
{
    public class PorukeVM
    {

       
        public int Id { get; set; }
     

        public string Text { get; set; }

        public byte[] Data { get; set; }

        public string DataType { get; set; }

     
        public int KorisnikPoslaoId { get; set; }
        public int KorisnikPrimioId { get; set; }

        public DateTime DatumKreiranja { get; set; }
        public int Status { get; set; }
    }
}