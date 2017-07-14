using ePrevoz.DATA.Helper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ePrevoz.DATA.Model
{
   public class Prevoz : IEntity
    {
        public int Id { get; set; }

        public bool IsDeleted { get; set; }

   
       public String Telefon { get; set; }
        public String Opis { get; set; }
        public float Cijena { get; set; }
        public int BrojMjesta { get; set; }
        public int BrojZauzetihMjesta { get; set; }
        public bool SlobodnoMjesto { get; set; }
        public bool Aktivno { get; set; }

        public DateTime DatumKretanja { get; set; }
        public DateTime DatumKreiranja { get; set; }

        public int TipPrevoza { get; set; }

        public virtual Korisnik Korisnik { get; set; }
        public int KorisnikId { get; set; }
        public virtual PrevoznoSredstvo PrevoznoSredstvo { get; set; }
        public int PrevoznoSredstvoId { get; set; }


    }
}
