using ePrevoz.DATA.Helper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ePrevoz.WEB.Models
{
    public class ZahtjevZaPrevozVM

    {

        public int Id { get; set; }

        public int KorisnikId { get; set; }

        public  KorisnikVM Korisnik { get; set; }

        public int Status { get; set; }

        public int Kolicina { get; set; }

        public DateTime DatumKreiranja { get; set; }

        public int PrevozId { get; set; }

        public  PrevozVM Prevoz { get; set; }
    }
}