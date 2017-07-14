using ePrevoz.DATA.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ePrevoz.WEB.Models
{
    public class PrijateljiVM
    {
        public int Id { get; set; }
  
        public int Korisnik1Id { get; set; }

        public int Korisnik2Id { get; set; }
     
        public int Status { get; set; }
   
        public int PoslaoKorisnikId { get; set; }
    }
}