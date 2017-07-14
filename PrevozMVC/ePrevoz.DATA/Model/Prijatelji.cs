using ePrevoz.DATA.Helper;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ePrevoz.DATA.Model
{
   public class Prijatelji:IEntity
    {
        public int Id { get; set; }
        public bool IsDeleted { get; set; }
  
      
        public int Korisnik1Id { get; set; }

        public int Korisnik2Id { get; set; }

        public virtual Korisnik Korisnik1 { get; set; }

        public virtual Korisnik Korisnik2 { get; set; }
        public int Status { get; set; }
        public virtual Korisnik PoslaoKorisnik { get; set; }
        public int PoslaoKorisnikId { get; set; }
        public DateTime DatumKreiranja { get; set; }

    }
}
