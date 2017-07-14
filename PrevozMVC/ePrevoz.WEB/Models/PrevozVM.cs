using ePrevoz.DATA.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ePrevoz.WEB.Models
{
    public class PrevozVM
    {
        

        public class Info {
            public int Id { get; set; }

            public bool IsDeleted { get; set; }

        
           
      
            public float Cijena { get; set; }
           
            public string Opis { get; set; }

            public int BrojMjesta { get; set; }
            public bool SlobodnoMjesto { get; set; }
            public bool Aktivno { get; set; }

            public KorisnikVM Korisnik { get; set; }
            public int PrevoznoSredstvoId { get; set; }
            public String PrevoznoSredstvo { get; set; }

            public String Telefon { get; set; }

            public int TipPrevoza { get; set; }
            public int BrojZauzetihMjesta { get; set; }
     
            public DateTime DatumKretanja { get; set; }
            public DateTime DatumKreiranja { get; set; }

            public List<StanicaVM> Stanice { get; set; }

        }

        public List<Info> Prevozi { get; set; }
    }
}